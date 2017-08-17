package com.bali.core.promo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BALI_UTILITY_COUPON")
@AdminPresentationClass(friendlyName="Coupons")
public class CouponImpl implements Coupon {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CouponId")
	@GenericGenerator(name = "CouponId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "segment_value", value = "CouponImpl"),
			@Parameter(name = "entity_name", value = "com.bali.core.promo.CouponImpl") })
	@Column(name = "COUPON_ID")
	private Long id;

	@Column(name = "NAME")
	@AdminPresentation(friendlyName = "Coupon Name", group = "General", order = 1000, prominent = true)
	private String name;

	@Column(name = "COUPON_COUNT")
	@AdminPresentation(friendlyName = "Coupon Count", group = "General", order = 6000, prominent = true, gridOrder = 3000)
	private Long count;
	
	@Column(name = "COUPON_AMOUNT")
	@AdminPresentation(friendlyName = "Coupon Amount", group = "General", order = 6000, prominent = true, gridOrder = 3000)
	private Long amount;
	
	@Column(name = "COUPON_MEDIA")
	@AdminPresentation(friendlyName = "Coupon Media", group = "Media", order = 6000, prominent = true, gridOrder = 3000)
	private Media media;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Long getCount() {
		return count;
	}

	@Override
	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public Long getAmount() {
		return this.amount;
	}

	@Override
	public void setAmount(Long paramLong) {
		this.amount = paramLong;
	}

	@Override
	public Media getMedia() {
		return this.media;
	}

	@Override
	public void setMedia(Media media) {
		this.media = media;
	}

}
