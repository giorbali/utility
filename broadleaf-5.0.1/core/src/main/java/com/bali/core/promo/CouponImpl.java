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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.bali.core.id.CouponCodeGenerator;

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

	@GeneratedValue(generator = "CouponCode")
	@GenericGenerator(name = "CouponCode", strategy = "com.bali.core.id.CouponCodeGenerator", parameters = {
			@Parameter(name = "segment_value", value = "CouponImpl"),
			@Parameter(name = "entity_name", value = "com.bali.core.promo.CouponImpl") })
	@Column(name = "CODE")
	@AdminPresentation(friendlyName = "Code", group = "General", order = 1000, prominent = true)
	private String code;
	
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
	@AdminPresentation(friendlyName = "Coupon Media", group = "General", order = 6000, prominent = true, gridOrder = 3000)
	private Media media;
	
	@ManyToOne(targetEntity = CustomerImpl.class, optional = true)
	@JoinColumn(name = "COUPON_PROVIDER")
	@AdminPresentation(friendlyName = "Provider", group = "General", order = 1000, prominent = true)
	@AdminPresentationToOneLookup()
	private Customer provider;
	
	@Column(name = "VALID_FROM")
	@AdminPresentation(friendlyName = "Valid from", group = "General", order = 1400, prominent = true)
	protected Date validFrom;
	
	@Column(name = "VALID_TO")
	@AdminPresentation(friendlyName = "Valid to", group = "General", order = 1400, prominent = true)
	protected Date validTo;
	
	@PrePersist 
	@PreUpdate
	void onPrePersist() {
		if(this.code == null) {
			this.code = String.valueOf(CouponCodeGenerator.generate());
			System.out.println("Coupon.code = " + this.code);
		}
	}

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

	@Override
	public Customer getProvider() {
		return this.provider;
	}

	@Override
	public void setProvider(Customer provider) {
		this.provider = provider;
	}

	@Override
	public Date getValidFrom() {
		return this.validFrom;
	}

	@Override
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	@Override
	public Date getValidTo() {
		return this.validTo;
	}

	@Override
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

}
