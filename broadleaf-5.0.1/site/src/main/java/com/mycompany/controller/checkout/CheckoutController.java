/*
  * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycompany.controller.checkout;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.checkout.model.BillingInfoForm;
import org.broadleafcommerce.core.web.checkout.model.CustomerCreditInfoForm;
import org.broadleafcommerce.core.web.checkout.model.GiftCardInfoForm;
import org.broadleafcommerce.core.web.checkout.model.OrderInfoForm;
import org.broadleafcommerce.core.web.checkout.model.ShippingInfoForm;
import org.broadleafcommerce.core.web.controller.checkout.BroadleafCheckoutController;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bali.core.catalog.domain.UtilityCustomer;
import com.bali.core.promo.Coupon;
import com.bali.core.promo.CouponDao;
import com.bali.core.promo.CouponOrder;
import com.bali.core.promo.CouponOrderDao;
import com.bali.core.promo.CouponOrderImpl;
import com.google.common.collect.Lists;
import com.mycompany.controller.coupon.CouponPickForm;

@Controller
public class CheckoutController extends BroadleafCheckoutController {
	
	private static final Log logger = LogFactory.getLog(CheckoutController.class);
	
	@Resource(name = "couponDao")
	private CouponDao couponDao;
	@Resource(name = "couponOrderDao")
	private CouponOrderDao couponOrderDao;

    @RequestMapping("/checkout")
    public String checkout(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
            @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm,
            @ModelAttribute("billingInfoForm") BillingInfoForm billingForm,
            @ModelAttribute("giftCardInfoForm") GiftCardInfoForm giftCardInfoForm,
            @ModelAttribute("customerCreditInfoForm") CustomerCreditInfoForm customerCreditInfoForm,
            RedirectAttributes redirectAttributes) {
    	model.addAttribute("saldo", fetchCustomerSaldo());
    	List<Coupon> coupons = couponDao.readAllCoupons();
    	model.addAttribute("coupons", coupons);
    	List<Coupon> pickedCoupons = fetchPickedCoupons();
    	long pickedCouponAmount = pickedCoupons.stream().mapToLong(c -> c.getAmount()).sum();
    	model.addAttribute("pickedCouponAmount", pickedCouponAmount);
        return super.checkout(request, response, model, redirectAttributes);
    }

    private double fetchCustomerSaldo() {
    	Customer customer = CartState.getCart().getCustomer();
    	if(customer instanceof UtilityCustomer){
    		return ((UtilityCustomer)customer).getSaldo();
    	}
		return 0d;
	}

	@RequestMapping(value = "/checkout/savedetails", method = RequestMethod.POST)
    public String saveGlobalOrderDetails(HttpServletRequest request, Model model,
            @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm,
            @ModelAttribute("billingInfoForm") BillingInfoForm billingForm,
            @ModelAttribute("giftCardInfoForm") GiftCardInfoForm giftCardInfoForm,
            @ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm, BindingResult result) throws ServiceException {
        return super.saveGlobalOrderDetails(request, model, orderInfoForm, result);
    }
    
    @RequestMapping(value = "/checkout/pickCoupon", method = RequestMethod.POST)
    public String pickCoupon(HttpServletRequest request, Model model,
    		@ModelAttribute("couponPickForm") CouponPickForm couponPickForm,
    		@ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm,
    		@ModelAttribute("billingInfoForm") BillingInfoForm billingForm,
    		@ModelAttribute("giftCardInfoForm") GiftCardInfoForm giftCardInfoForm,
    		@ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm, BindingResult result) throws ServiceException {
    	if(couponPickForm != null){
    		Coupon coupon = couponDao.fetchById(couponPickForm.getCouponId());
    		List<Coupon> pickedCoupons = fetchPickedCoupons();
    		pickedCoupons.add(coupon);
            storePickedCoupons(pickedCoupons);
    	}
    	return "redirect:/checkout";
    }
    
    @RequestMapping(value = "/checkout/clearCouponSelection", method = RequestMethod.POST)
    public String saveCoupons(HttpServletRequest request, Model model) throws ServiceException {
    	storePickedCoupons(Lists.newArrayList());
    	return "redirect:/checkout";
    }

	private void storePickedCoupons(List<Coupon> pickedCoupons) {
		WebRequest webRequest = BroadleafRequestContext.getBroadleafRequestContext().getWebRequest();
		webRequest.setAttribute("pickedCoupons", pickedCoupons, WebRequest.SCOPE_SESSION);
	}

	private List<Coupon> fetchPickedCoupons() {
		WebRequest webRequest = BroadleafRequestContext.getBroadleafRequestContext().getWebRequest();
		Object pickedCouponAttribute = webRequest.getAttribute("pickedCoupons", WebRequest.SCOPE_SESSION);
		if (pickedCouponAttribute instanceof List){
			return (List<Coupon>)pickedCouponAttribute;
		} else {
			return Lists.newArrayList();
		}
	}

    private void addCouponToCart(Long couponId, Order cart) {
    	if(couponId == null || cart == null){
    		logger.error("couponId or cart cannot be null");
    		return;
    	}
    	Coupon coupon = couponDao.fetchById(couponId);
    	if(coupon == null){
    		return;
    	}
    	List<CouponOrder> pickedCoupons = couponOrderDao.findAllByOrder(cart);
    	if(CollectionUtils.isEmpty(pickedCoupons)){
    		saveCouponOrder(coupon, cart);
    	}
    	List<Coupon> coupons = pickedCoupons.stream().map(item -> item.getCoupon()).collect(Collectors.toList());
    	Long couponAmountSum = coupons.stream().mapToLong(c -> c.getAmount()).sum();
    	BigDecimal rest = cart.getTotal().getAmount().subtract(BigDecimal.valueOf(couponAmountSum));
    	if(rest.compareTo(BigDecimal.valueOf(coupon.getAmount())) == -1){
    		logger.info(String.format("You can pick coupons by less then %s amount", rest.toString()));
    		return;
    	}
    	saveCouponOrder(coupon, cart);
	}

	private void saveCouponOrder(Coupon coupon, Order cart) {
		CouponOrder couponOrder = new CouponOrderImpl();
		couponOrder.setCoupon(coupon);
		couponOrder.setOrder(cart);
		couponOrderDao.save(couponOrder);
	}

	@RequestMapping(value = "/checkout/complete", method = RequestMethod.POST)
    public String processCompleteCheckoutOrderFinalized(RedirectAttributes redirectAttributes) throws PaymentException {
    	baseConfirmationRedirect = "redirect:/confirmation2";
        return super.processCompleteCheckoutOrderFinalized(redirectAttributes);
    }

    @RequestMapping(value = "/checkout/cod/complete", method = RequestMethod.POST)
    public String processPassthroughCheckout(RedirectAttributes redirectAttributes)
            throws PaymentException, PricingException {
        return super.processPassthroughCheckout(redirectAttributes, PaymentType.COD);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
    }
    
}
