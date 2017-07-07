package com.bali.core.promo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BALI_UTILITY_CUSTOMER_COUPON")
@AdminPresentationClass(friendlyName = "CustomerCoupon")
public class CustomerCouponImpl implements CustomerCoupon {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CustomerCouponId")
	@GenericGenerator(name = "CustomerCouponId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "segment_value", value = "CustomerCouponImpl"),
			@Parameter(name = "entity_name", value = "com.bali.core.promo.CustomerCouponImpl") })
	@Column(name = "CUSTOMERCOUPON_ID")
	private Long id;

	@ManyToOne(targetEntity = CouponImpl.class, optional = false)
	@JoinColumn(name = "COUPON_ID", nullable = false)
	@AdminPresentation(friendlyName = "CustomerCouponImpl_Coupon", group = "General", order = 2000)
	@AdminPresentationToOneLookup
	private Coupon coupon;

	@ManyToOne(targetEntity = CustomerImpl.class, optional = false)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
	@AdminPresentation(friendlyName = "CustomerCouponImpl_Customer", group = "General", order = 2000)
	@AdminPresentationToOneLookup
	private Customer customer;

	@Column(name = "COUPON_COUNT")
	@AdminPresentation(friendlyName = "CustomerCouponImpl_Count", group = "General", order = 6000)
	private Long count;
	
	@Column(name = "DATE")
	@AdminPresentation(friendlyName = "CustomerCouponImpl_Date", group = "General", order = 1400, prominent = true)
	protected Date date;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long paramLong) {
		this.id = paramLong;
	}

	@Override
	public Coupon getCoupon() {
		return this.coupon;
	}

	@Override
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	@Override
	public Customer getCustomer() {
		return this.customer;
	}

	@Override
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Long getCount() {
		return this.count;
	}

	@Override
	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

}
