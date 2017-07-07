package com.mycompany.controller.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.core.web.controller.account.AbstractAccountController;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bali.core.order.service.CouponService;
import com.bali.core.promo.Coupon;
import com.bali.core.promo.CustomerCoupon;

@Controller
public class CustomerCouponController extends AbstractAccountController {

	@Resource(name = "couponService")
	private CouponService couponService;

	@RequestMapping(value = "/account/customerCoupons", method = RequestMethod.GET)
	public String viewCustomerCoupons(HttpServletRequest request, HttpServletResponse response, Model model) {
		Customer customer = CustomerState.getCustomer(request);
		List<CustomerCoupon> customerCoupons = couponService.fetchCustomerCoupons(customer);
		model.addAttribute("customerCoupons", customerCoupons);
		return "account/customerCouponOverview";
	}

	@RequestMapping(value = "/coupon/download", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") Long id) {
		try {
			Coupon coupon = couponService.fetchById(id);
			File couponQR = couponService.generateCouponQR(coupon);
			InputStream inputStream = new FileInputStream(couponQR);
			org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
			response.setContentType("image/png");
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
