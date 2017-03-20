package com.bali.core.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.domain.SkuImpl;
import org.broadleafcommerce.core.order.domain.DiscreteOrderItemImpl;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BALI_UTILITY_ORDER_ITEM")
@AdminPresentationClass(friendlyName="UtilityOrderItemImpl_utilityOrderItem")
public class UtilityOrderItemImpl extends DiscreteOrderItemImpl implements UtilityOrderItem {

	private static final long serialVersionUID = 1L;

	@Column(name = "ACCOUNT_NUMBER")
	@AdminPresentation(friendlyName = "UtilityOrderItemImpl_account_number", fieldType = SupportedFieldType.STRING)
	protected String accountNumber;

	@Column(name = "amount")
	@AdminPresentation(friendlyName = "UtilityOrderItemImpl_amount", fieldType = SupportedFieldType.MONEY)
	protected Money amount;
	
	@Column(name = "address")
	@AdminPresentation(friendlyName = "UtilityOrderItemImpl_address", fieldType = SupportedFieldType.STRING)
	protected String address;
	
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=SkuImpl.class, optional=false)
	@JoinColumn(name="SKU_ID", nullable=false)
	@AdminPresentation(friendlyName="DiscreteOrderItemImpl_Sku", order=3000, group="OrderItemImpl_Catalog", groupOrder=3000)
	@AdminPresentationToOneLookup
	protected Sku sku;

	@Override
	public String getAccountnumber() {
		return accountNumber;
	}

	@Override
	public void setAccountnumber(String accountnumber) {
		this.accountNumber = accountnumber;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public Money getAmount() {
		return amount;
	}

	@Override
	public void setAmount(Money amount) {
		this.amount = amount;
	}

	@Override
	public Sku getSku() {
		return sku;
	}

	@Override
	public void setSku(Sku sku) {
		this.sku = sku;
	}

}
