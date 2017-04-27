package com.bali.core.catalog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;

@Entity
@Table(name = "BALI_UTILITY_CUSTOMER")
public class UtilityCustomerImpl extends CustomerImpl implements UtilityCustomer {

	private static final long serialVersionUID = 1L;

	@Column(name = "saldo")
	@AdminPresentation(friendlyName = "UtilityCustomer_saldo")
	protected Double saldo;

	@Override
	public Double getSaldo() {
		return saldo;
	}

	@Override
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

}
