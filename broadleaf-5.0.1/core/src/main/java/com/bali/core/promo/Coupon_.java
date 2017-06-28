package com.bali.core.promo;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CouponImpl.class)
public class Coupon_ {
	
	public static volatile SingularAttribute<CouponImpl, Long> id;
	public static volatile SingularAttribute<CouponImpl, String> name;
	public static volatile SingularAttribute<CouponImpl, Long> count;
	public static volatile SingularAttribute<CouponImpl, Long> amount;

}
