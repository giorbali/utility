package com.bali.core.promo;

import java.util.List;

import org.broadleafcommerce.core.order.domain.Order;

public interface CouponOrderDao {
	
	List<CouponOrder> readAllCoupons();
	List<CouponOrder> findAllByOrder(Order order);
	void save(CouponOrder couponOrder);

}
