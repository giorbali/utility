package com.bali.core.catalog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;
import org.hibernate.annotations.SQLDelete;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@javax.persistence.Table(name = "BALI_UTILITY_PRODUCT")
@SQLDelete(sql = "UPDATE BALI_UTILITY_PRODUCT SET ARCHIVED = 'Y' WHERE PRODUCT_ID = ?")
public class UtilityProductImpl extends ProductImpl implements UtilityProduct {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "UTILITY_TYPE")
	@AdminPresentation(friendlyName = "Utility Type", group = "General")
	private String utilityType;

	@Override
	public String getUtilityType() {
		return utilityType;
	}

	@Override
	public void setUtilityType(String type) {
		this.utilityType = type;
	}

}
