package com.mycompany.controller.coupon;

import java.io.Serializable;

public class CouponPickForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Long couponId;

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

}
