package com.bali.core.promo;

import java.io.Serializable;

import org.broadleafcommerce.core.order.domain.Order;

public interface CouponOrder extends Serializable {
	
	public abstract Long getId();

	public abstract void setId(Long paramLong);
	
	public abstract Coupon getCoupon();

	public abstract void setCoupon(Coupon coupon);
	
	public abstract Order getOrder();
	
	public abstract void setOrder(Order order);

}
