package com.bali.core.order.service;

import java.io.File;
import java.util.List;

import org.broadleafcommerce.profile.core.domain.Customer;

import com.bali.core.promo.Coupon;
import com.bali.core.promo.CustomerCoupon;

public interface CouponService {
	
	List<Coupon> fetchAllCoupons();
	
	Coupon fetchById(Long id);
	
	void savePickedCoupons(List<Coupon> coupons, Customer customer);
	
	List<CustomerCoupon> fetchCustomerCoupons(Customer customer);
	
	File generateCouponQR(Coupon coupon);

}
