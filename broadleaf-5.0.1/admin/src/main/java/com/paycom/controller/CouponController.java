package com.paycom.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.openadmin.dto.ClassMetadata;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.Property;
import org.broadleafcommerce.openadmin.dto.SectionCrumb;
import org.broadleafcommerce.openadmin.web.controller.entity.AdminBasicEntityController;
import org.broadleafcommerce.openadmin.web.controller.modal.ModalHeaderType;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bali.core.order.service.CouponService;
import com.bali.core.promo.Coupon;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/" + CouponController.SECTION_KEY)
@Secured("PERMISSION_OTHER_DEFAULT")
public class CouponController extends AdminBasicEntityController {
	private static final Log logger = LogFactory.getLog(CouponController.class);

	protected static final String SECTION_KEY = "coupons";
	// @Resource(name = "couponService")
	@Autowired
	private CouponService couponService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addEntity(HttpServletRequest request, HttpServletResponse response, Model model,
			@PathVariable Map<String, String> pathVars, @ModelAttribute(value = "entityForm") EntityForm entityForm,
			BindingResult result) throws Exception {
		String sectionKey = getSectionKey(pathVars);
		String sectionClassName = getClassNameForSection(sectionKey);
		List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, null, null);
		ClassMetadata cmd = service
				.getClassMetadata(getSectionPersistencePackageRequest(sectionClassName, sectionCrumbs, pathVars))
				.getDynamicResultSet().getClassMetaData();

		extractDynamicFormFields(cmd, entityForm);
		String[] sectionCriteria = customCriteriaService.mergeSectionCustomCriteria(sectionClassName,
				getSectionCustomCriteria());
		Entity entity = service.addEntity(entityForm, sectionCriteria, sectionCrumbs).getEntity();
		entityFormValidator.validate(entityForm, entity, result);

		if (result.hasErrors()) {
			entityForm.clearFieldsMap();
			formService.populateEntityForm(cmd, entity, entityForm, sectionCrumbs);

			formService.removeNonApplicableFields(cmd, entityForm, entityForm.getEntityType());

			modifyAddEntityForm(entityForm, pathVars);

			model.addAttribute("viewType", "modal/entityAdd");
			model.addAttribute("currentUrl", request.getRequestURL().toString());
			model.addAttribute("modalHeaderType", ModalHeaderType.ADD_ENTITY.getType());
			setModelAttributes(model, sectionKey);
			return "modules/modalContainer";
		}
		// generate coupons
		generateCoupons(entityForm, sectionCrumbs, sectionCriteria, entity);
		// Note that AJAX Redirects need the context path prepended to them
		return "ajaxredirect:" + getContextPath(request) + sectionKey + "/" + entity.getPMap().get("id").getValue();
	}

	private void generateCoupons(EntityForm entityForm, List<SectionCrumb> sectionCrumbs, String[] sectionCriteria,
			Entity entity) throws ServiceException {
		List<Property> properties = Lists.newArrayList(entity.getProperties()).stream()
				.filter(p -> p.getName().equals("count")).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(properties)) {
			return;
		}
		Long count = Long.valueOf(properties.get(0).getValue());
		if(count == null) {
			return;
		}
		for (int i = 1; i < count; i++) {
			logger.info("generate coupons for :" + entity.getType()[0]);
			service.addEntity(entityForm, sectionCriteria, sectionCrumbs).getEntity();
		}
	}

	@Override
	protected String getSectionKey(Map<String, String> pathVars) {
		if (super.getSectionKey(pathVars) != null) {
			return super.getSectionKey(pathVars);
		}
		return SECTION_KEY;
	}

}
