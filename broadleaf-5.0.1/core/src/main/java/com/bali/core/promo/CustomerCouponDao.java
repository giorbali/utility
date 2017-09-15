package com.bali.core.promo;

import java.io.Serializable;
import java.util.List;

import org.broadleafcommerce.profile.core.domain.Customer;

public interface CustomerCouponDao extends Serializable {
	
	List<CustomerCoupon> findAllByCustomer(Customer customer);
	void save(CustomerCoupon customerCoupon);
	void saveAllCustomerCoupons(List<CustomerCoupon> customerCoupons);
}
