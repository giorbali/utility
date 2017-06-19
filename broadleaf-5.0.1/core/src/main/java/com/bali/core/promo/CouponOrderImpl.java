package com.bali.core.promo;

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
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BALI_UTILITY_COUPON_ORDER")
@AdminPresentationClass(friendlyName = "CouponOrder")
public class CouponOrderImpl implements CouponOrder {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CouponOrderId")
	@GenericGenerator(name = "CouponOrderId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "segment_value", value = "CouponOrderImpl"),
			@Parameter(name = "entity_name", value = "com.bali.core.promo.CouponOrderImpl") })
	@Column(name = "COUPONORDER_ID")
	private Long id;

	@ManyToOne(targetEntity = CouponImpl.class, optional = false)
	@JoinColumn(name = "COUPON_ID", nullable = false)
	@AdminPresentation(friendlyName = "CouponOrderImpl_Coupon", group = "General", order = 2000)
	@AdminPresentationToOneLookup
	private Coupon coupon;

	@ManyToOne(targetEntity = OrderImpl.class, optional = false)
	@JoinColumn(name = "ORDER_ID", nullable = false)
	@AdminPresentation(friendlyName = "CouponOrderImpl_Order", group = "General", order = 2000)
	@AdminPresentationToOneLookup
	private Order order;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
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
	public Order getOrder() {
		return this.order;
	}

	@Override
	public void setOrder(Order order) {
		this.order = order;
	}

}
