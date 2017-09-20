package com.bali.core.promo;

import java.util.Date;
import java.util.List;

public interface CouponDao {
	
	List<Coupon> readAllCoupons();
	List<Coupon> fetchValidCouponsOn(Date date);
	List<Coupon> fetchAllValidCouponsOn(Date date);
	Coupon fetchById(Long id);
	void generateCouponsFrom(Coupon baseCoupon);

}
