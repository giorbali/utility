package com.mycompany.controller.account;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.core.web.controller.account.AbstractAccountController;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bali.core.order.service.CouponService;
import com.bali.core.promo.CustomerCoupon;

@Controller
@RequestMapping("/account/customerCoupons")
public class CustomerCouponController extends AbstractAccountController {
	
	@Resource(name = "couponService")
	private CouponService couponService;
	
	@RequestMapping(method = RequestMethod.GET)
    public String viewCustomerCoupons(HttpServletRequest request, HttpServletResponse response, Model model) {
    	Customer customer = CustomerState.getCustomer(request);
    	 List<CustomerCoupon> customerCoupons = couponService.fetchCustomerCoupons(customer);
    	 model.addAttribute("customerCoupons", customerCoupons);
        return "account/customerCouponOverview";
    }

}
