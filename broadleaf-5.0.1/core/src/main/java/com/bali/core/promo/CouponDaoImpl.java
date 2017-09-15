package com.bali.core.promo;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("couponDao")
@Scope(SCOPE_PROTOTYPE)
public class CouponDaoImpl implements CouponDao {

	private static final Log logger = LogFactory.getLog(CouponDaoImpl.class);

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Override
	public List<Coupon> fetchValidCouponsOn(Date date) {
		/*
		 * CriteriaQuery<Coupon> criteria = builder.createQuery(Coupon.class);
		 * Root<CouponImpl> coupon = criteria.from(CouponImpl.class);
		 * ParameterExpression<Date> parameterDate = builder.parameter(Date.class,
		 * "dateParam"); criteria.select(coupon).where(builder.between(parameterDate,
		 * coupon.get("validFrom"), coupon.get("validTo"))); TypedQuery<Coupon> query =
		 * this.em.createQuery(criteria); query.setParameter("dateParam",
		 * parameterDate);
		 */
		TypedQuery<Coupon> query = em.createQuery(
				"select c from CouponImpl as c where not exists (select cc from CustomerCouponImpl as cc where cc.coupon.id = c.id) "
				+ "and  :dateparam between validFrom and validTo group by c.provider, c.name",
				Coupon.class);
		query.setParameter("dateparam", date);
		return query.getResultList();
	}

	@Override
	public List<Coupon> readAllCoupons() {
		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		CriteriaQuery<Coupon> criteria = builder.createQuery(Coupon.class);
		Root<CouponImpl> coupon = criteria.from(CouponImpl.class);
		criteria.select(coupon);
		TypedQuery<Coupon> query = this.em.createQuery(criteria);

		return query.getResultList();
	}

	@Override
	public Coupon fetchById(Long id) {
		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		Metamodel metamodel = this.em.getMetamodel();
		EntityType<CouponImpl> coupon_ = metamodel.entity(CouponImpl.class);
		CriteriaQuery<CouponImpl> criteriaQuery = builder.createQuery(CouponImpl.class);
		Root<CouponImpl> criteriaRoot = criteriaQuery.from(CouponImpl.class);
		criteriaQuery.select(criteriaRoot);
		criteriaQuery.where(builder.equal(criteriaRoot.get(coupon_.getSingularAttribute("id")), id));
		TypedQuery<CouponImpl> query = this.em.createQuery(criteriaQuery);

		List<CouponImpl> resultList = query.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			logger.error(String.format("Coupon by id:%s not found", id));
			return null;
		}
		if (resultList.size() > 1) {
			logger.error(String.format("Found more then one (%s) Coupon by id:%s ", resultList.size(), id));
			return null;
		}
		return resultList.iterator().next();
	}

	@Override
	public void generateCouponsFrom(Coupon baseCoupon) {
		Long count = baseCoupon.getCount();
		if (count == null || count == 0) {
			return;
		}
		for (int i = 0; i < baseCoupon.getCount(); i++) {
			em.detach(baseCoupon);
			baseCoupon.setId(null);
			em.persist(baseCoupon);
		}
	}

}
