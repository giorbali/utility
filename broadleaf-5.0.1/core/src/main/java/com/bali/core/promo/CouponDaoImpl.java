package com.bali.core.promo;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("couponDao")
@Scope(SCOPE_PROTOTYPE)
public class CouponDaoImpl implements CouponDao {
	
    @PersistenceContext(unitName="blPU")
    protected EntityManager em;


	@Override
	public List<Coupon> readAllCoupons() {
		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(Coupon.class);
		Root coupon = criteria.from(CouponImpl.class);
		criteria.select(coupon);
		TypedQuery query = this.em.createQuery(criteria);
		
	    return query.getResultList();
	}

}
