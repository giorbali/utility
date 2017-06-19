package com.bali.core.promo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class CouponOrderDaoImpl implements CouponOrderDao {
	
	@PersistenceContext(unitName="blPU")
    protected EntityManager em;

	@Override
	public List<CouponOrder> readAllCoupons() {
		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(CouponOrder.class);
		Root couponOrder = criteria.from(CouponOrderImpl.class);
		criteria.select(couponOrder);
		TypedQuery query = this.em.createQuery(criteria);
		
	    return query.getResultList();
	}

}
