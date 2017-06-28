package com.bali.core.promo;

import java.util.List;

public interface CouponDao {
	
	List<Coupon> readAllCoupons();
	Coupon fetchById(Long id);

}
