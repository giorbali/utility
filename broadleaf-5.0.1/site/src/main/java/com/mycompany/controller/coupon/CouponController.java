package com.mycompany.controller.coupon;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.template.TemplateType;
import org.broadleafcommerce.common.web.TemplateTypeAware;
import org.broadleafcommerce.common.web.controller.BroadleafAbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.bali.core.promo.Coupon;
import com.bali.core.promo.CouponDao;

@org.springframework.stereotype.Controller("couponController")
public class CouponController extends BroadleafAbstractController implements Controller, TemplateTypeAware {
	
	@Resource(name = "couponDao")
	protected CouponDao couponDao;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		List<Coupon> coupons = couponDao.readAllCoupons();
		model.addObject("coupons", coupons);
		return model;
	}

	@Override
	public String getExpectedTemplateName(HttpServletRequest paramHttpServletRequest) {
		return "coupons";
	}

	@Override
	public TemplateType getTemplateType(HttpServletRequest paramHttpServletRequest) {
		return TemplateType.OTHER;
	}

}
