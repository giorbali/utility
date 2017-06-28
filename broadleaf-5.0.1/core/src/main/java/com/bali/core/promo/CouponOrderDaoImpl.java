package com.bali.core.promo;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.order.domain.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("couponOrderDao")
@Scope(SCOPE_PROTOTYPE)
public class CouponOrderDaoImpl implements CouponOrderDao {
	
	private static final Log logger = LogFactory.getLog(CouponOrderDaoImpl.class);
	
	@PersistenceContext(unitName="blPU")
    protected EntityManager em;

	@Override
	public List<CouponOrder> readAllCoupons() {
		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		CriteriaQuery<CouponOrder> criteria = builder.createQuery(CouponOrder.class);
		Root<CouponOrder> couponOrder = criteria.from(CouponOrder.class);
		criteria.select(couponOrder);
		TypedQuery<CouponOrder> query = this.em.createQuery(criteria);
		
	    return query.getResultList();
	}

	@Override
	public List<CouponOrder> findAllByOrder(Order order) {
		if(null == order){
			logger.error("Order cannot be null");
			return Collections.emptyList();
		}
		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		Metamodel metamodel = this.em.getMetamodel();
		EntityType<CouponOrderImpl> couponOrder_ = metamodel.entity(CouponOrderImpl.class);
		CriteriaQuery<CouponOrder> criteriaQuery = builder.createQuery(CouponOrder.class);
		Root<CouponOrderImpl> criteriaRoot = criteriaQuery.from(CouponOrderImpl.class);
		criteriaQuery.select(criteriaRoot);
		criteriaQuery.where(builder.equal(criteriaRoot.get(couponOrder_.getSingularAttribute("order")), order));
		TypedQuery<CouponOrder> query = this.em.createQuery(criteriaQuery);
		
		return query.getResultList();
	}

	@Override
	public void save(CouponOrder couponOrder) {
		em.persist(couponOrder);
	}

}
