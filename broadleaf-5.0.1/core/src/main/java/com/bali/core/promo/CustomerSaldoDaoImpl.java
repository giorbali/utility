package com.bali.core.promo;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("customerSaldoDao")
@Scope(SCOPE_PROTOTYPE)
public class CustomerSaldoDaoImpl implements CustomerSaldoDao {

	private static final Log logger = LogFactory.getLog(CustomerSaldoDaoImpl.class);

	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Override
	public List<CustomerSaldo> allByCustomer(Customer customer) {
		if (customer == null) {
			logger.error("Customer cannot be null");
			return emptyList();
		}
		CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
		Metamodel metamodel = this.em.getMetamodel();
		EntityType<CustomerSaldoImpl> customerSaldo_ = metamodel.entity(CustomerSaldoImpl.class);
		CriteriaQuery<CustomerSaldo> criteriaQuery = criteriaBuilder.createQuery(CustomerSaldo.class);
		Root<CustomerSaldoImpl> root = criteriaQuery.from(CustomerSaldoImpl.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(customerSaldo_.getSingularAttribute("customer")), customer));
		TypedQuery<CustomerSaldo> query = this.em.createQuery(criteriaQuery);

		List<CustomerSaldo> resultList = query.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			logger.error(String.format("No Saldo found by Customer(id:%s).", customer.getId()));
			return emptyList();
		}
		return resultList;
	}

	@Override
	public List<CustomerSaldo> allByCustomerAndType(Customer customer, String type) {
		if (customer == null || isBlank(type)) {
			logger.error("Customer|type cannot be null");
			return emptyList();
		}
		CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
		Metamodel metamodel = this.em.getMetamodel();
		EntityType<CustomerSaldoImpl> customerSaldo_ = metamodel.entity(CustomerSaldoImpl.class);
		CriteriaQuery<CustomerSaldo> criteriaQuery = criteriaBuilder.createQuery(CustomerSaldo.class);
		Root<CustomerSaldoImpl> root = criteriaQuery.from(CustomerSaldoImpl.class);
		criteriaQuery.select(root);
		//Constructing list of parameters
	    List<Predicate> predicates = new ArrayList<Predicate>();
	    predicates.add(criteriaBuilder.equal(root.get(customerSaldo_.getSingularAttribute("customer")), customer));
	    predicates.add(criteriaBuilder.equal(root.get(customerSaldo_.getSingularAttribute("type")), type));
		criteriaQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<CustomerSaldo> query = this.em.createQuery(criteriaQuery);

		List<CustomerSaldo> resultList = query.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			logger.info(String.format("No Saldo found by Customer(id:%s) and type:%s", customer.getId(), type));
			return Collections.emptyList();
		}
		return resultList;
	}

	@Override
	public void save(CustomerSaldo customerSaldo) {
		em.persist(customerSaldo);
	}

	@Override
	public CustomerSaldo findBy(OrderItem orderItem) {
		if (orderItem == null) {
			logger.error("orderItem cannot be null");
			return null;
		}
		CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
		Metamodel metamodel = this.em.getMetamodel();
		EntityType<CustomerSaldoImpl> customerSaldo_ = metamodel.entity(CustomerSaldoImpl.class);
		CriteriaQuery<CustomerSaldo> criteriaQuery = criteriaBuilder.createQuery(CustomerSaldo.class);
		Root<CustomerSaldoImpl> root = criteriaQuery.from(CustomerSaldoImpl.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(customerSaldo_.getSingularAttribute("orderItem")), orderItem));
		TypedQuery<CustomerSaldo> query = this.em.createQuery(criteriaQuery);

		List<CustomerSaldo> resultList = query.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			logger.error(String.format("No Saldo found by orderItem(id:%s).", orderItem.getId()));
			return null;
		} else if (resultList.size() > 1) {
			logger.error(String.format("More then one Saldo found by orderItem(id:%s).", orderItem.getId()));
			return null;
		}
		return resultList.iterator().next();
	}

	@Override
	public void remove(CustomerSaldo customerSaldo) {
		if(customerSaldo == null) {
			return;
		}
		em.remove(customerSaldo);
	}

}
