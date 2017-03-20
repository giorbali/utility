package com.bali.core.catalog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;

@Entity
@Table(name = "BALI_UTILITY_CUSTOMER")
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID")
@AdminPresentationClass(friendlyName="UtilityCustomerImpl_utilityOrderItem")
public class UtilityCustomerImpl extends CustomerImpl implements UtilityCustomer {

	private static final long serialVersionUID = 1L;

	@Column(name = "saldo")
	@AdminPresentation(friendlyName = "UtilityCustomer_saldo", fieldType = SupportedFieldType.MONEY)
	protected Money saldo;

	@Override
	public Money getSaldo() {
		return saldo;
	}

	@Override
	public void setSaldo(Money saldo) {
		this.saldo = saldo;
	}

}
