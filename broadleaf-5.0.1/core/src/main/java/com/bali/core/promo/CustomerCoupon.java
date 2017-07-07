package com.bali.core.promo;

import java.io.Serializable;
import java.util.Date;

import org.broadleafcommerce.profile.core.domain.Customer;

public interface CustomerCoupon extends Serializable {
	
	Long getId();

	void setId(Long paramLong);
	
	Coupon getCoupon();

	void setCoupon(Coupon coupon);
	
	Customer getCustomer();
	
	void setCustomer(Customer customer);
	
	Long getCount();
	
	void setCount(Long count);
	
    Date getDate();
	
	void setDate(Date date);

}
