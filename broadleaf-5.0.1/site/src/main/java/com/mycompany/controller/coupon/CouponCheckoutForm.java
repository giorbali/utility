package com.mycompany.controller.coupon;

import java.io.Serializable;
import java.util.Map;

public class CouponCheckoutForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Map<Long, Long> coupons;

	public Map<Long, Long> getCoupons() {
		return coupons;
	}

	public void setCoupons(Map<Long, Long> coupons) {
		this.coupons = coupons;
	}

}
