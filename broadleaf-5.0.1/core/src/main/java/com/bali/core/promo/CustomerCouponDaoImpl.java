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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("customerCouponDao")
@Scope(SCOPE_PROTOTYPE)
public class CustomerCouponDaoImpl implements CustomerCouponDao {
	
	private static final Log logger = LogFactory.getLog(CustomerCouponDaoImpl.class);

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	private static final long serialVersionUID = 1L;

	@Override
	public List<CustomerCoupon> findAllByCustomer(Customer customer) {
		if ( null == customer){
			logger.error("Customer cannot be null");
			return Collections.emptyList();
		}
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		Metamodel metamodel = em.getMetamodel();
		EntityType<CustomerCouponImpl> customerCoupon_ = metamodel.entity(CustomerCouponImpl.class);
		CriteriaQuery<CustomerCoupon> criteriaQuery = criteriaBuilder.createQuery(CustomerCoupon.class);
		Root<CustomerCouponImpl> root = criteriaQuery.from(CustomerCouponImpl.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(customerCoupon_.getSingularAttribute("customer")), customer));
		TypedQuery<CustomerCoupon> query = em.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	public void save(CustomerCoupon customerCoupon) {
		em.persist(customerCoupon);
	}

	@Override
	public void saveAllCustomerCoupons(List<CustomerCoupon> customerCoupons) {
		if(CollectionUtils.isEmpty(customerCoupons)){
			return;
		}
		customerCoupons.stream().forEach(customerCoupon -> em.persist(customerCoupon));
	}

}
