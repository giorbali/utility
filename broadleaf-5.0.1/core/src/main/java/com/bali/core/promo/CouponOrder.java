package com.bali.core.promo;

import java.io.Serializable;

import org.broadleafcommerce.core.order.domain.Order;

public interface CouponOrder extends Serializable {
	
	Long getId();

	void setId(Long paramLong);
	
	Coupon getCoupon();

	void setCoupon(Coupon coupon);
	
	Order getOrder();
	
	void setOrder(Order order);
	
	Long getCount();
	
	void setCount(Long count);

}
