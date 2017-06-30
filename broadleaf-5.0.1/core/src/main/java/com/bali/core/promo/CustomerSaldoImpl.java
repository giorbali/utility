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
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BALI_CUSTOMER_SALDO")
@AdminPresentationClass(friendlyName = "Saldo")
public class CustomerSaldoImpl implements CustomerSaldo {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CustomerSaldoId")
	@GenericGenerator(name = "CustomerSaldoId", strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator", parameters = {
			@Parameter(name = "segment_value", value = "CustomerSaldoImpl"),
			@Parameter(name = "entity_name", value = "com.bali.core.promo.CustomerSaldoImpl") })
	@Column(name = "CUSTOMERSALDO_ID")
	private Long id;

	@ManyToOne(targetEntity = CustomerImpl.class, optional=false)
    @JoinColumn(name = "CUSTOMER_ID")
	@AdminPresentation(friendlyName = "CustomerSaldoImpl_CustomerSaldo_Customer", group = "General", order = 1000, prominent = true)
	private Customer customer;
	
	@Column(name = "DESCRIPTION")
	@AdminPresentation(friendlyName = "CustomerSaldoImpl_CustomerSaldo_Description", group = "General", order = 1100, prominent = true)
	private String description;

	@Column(name = "TYPE")
	@AdminPresentation(friendlyName = "CustomerSaldoImpl_CustomerSaldo_Type", group = "General", order = 1200, prominent = true)
	private String type;

	@Column(name = "SALDO")
	@AdminPresentation(friendlyName = "CustomerSaldoImpl_CustomerSaldo_Saldo", group = "General", order = 1300, prominent = true)
	protected Double saldo;

	@Column(name = "DATE")
	@AdminPresentation(friendlyName = "CustomerSaldoImpl_CustomerSaldo_Date", group = "General", order = 1400, prominent = true)
	protected Date date;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Double getSaldo() {
		return saldo;
	}

	@Override
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public Customer getCustomer() {
		return this.customer;
	}

	@Override
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
