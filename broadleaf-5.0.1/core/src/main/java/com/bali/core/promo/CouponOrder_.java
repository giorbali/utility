package com.bali.core.promo;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.broadleafcommerce.core.order.domain.Order;

@StaticMetamodel(CouponOrder.class)
public class CouponOrder_ {
	
	public static volatile SingularAttribute<CouponOrderImpl, Long> id;
	public static volatile SingularAttribute<CouponOrderImpl, Coupon> coupon;
	public static volatile SingularAttribute<CouponOrderImpl, Order> order;
	public static volatile SingularAttribute<CouponOrderImpl, Long> count;

}
