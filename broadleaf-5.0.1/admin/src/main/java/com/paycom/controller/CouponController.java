package com.paycom.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.email.domain.EmailTargetImpl;
import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.openadmin.dto.ClassMetadata;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.Property;
import org.broadleafcommerce.openadmin.dto.SectionCrumb;
import org.broadleafcommerce.openadmin.web.controller.entity.AdminBasicEntityController;
import org.broadleafcommerce.openadmin.web.controller.modal.ModalHeaderType;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
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
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/" + CouponController.SECTION_KEY)
@Secured("PERMISSION_OTHER_DEFAULT")
public class CouponController extends AdminBasicEntityController {
	private static final Log logger = LogFactory.getLog(CouponController.class);

	protected static final String SECTION_KEY = "coupons";
	@Resource(name = "blCustomerService")
	private CustomerService customerService;
	@Resource(name = "blEmailService")
	private EmailService emailService;
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
		List<Entity> newEntities = Lists.newArrayList(entity);
		Long count = Long.valueOf(properties.get(0).getValue());
		if (count == null) {
			return;
		}
		for (int i = 1; i < count; i++) {
			logger.info("generate coupons for :" + entity.getType()[0]);
			newEntities.add(service.addEntity(entityForm, sectionCriteria, sectionCrumbs).getEntity());
		}
		sendGeneratedCouponInfoToProvider(newEntities);
	}

	private void sendGeneratedCouponInfoToProvider(List<Entity> newEntities) {
		if (CollectionUtils.isEmpty(newEntities)) {
			return;
		}
		// read provider info
		String providerId = "";
		String couponName = "";
		String validFrom = "";
		String validTo = "";
		for (Property property : newEntities.get(0).getProperties()) {
			switch (property.getName()) {
			case "provider":
				providerId = property.getValue();
				break;
			case "name":
				couponName = property.getValue();
				break;
			case "validFrom":
				validFrom = property.getValue();
				break;
			case "validTo":
				validTo = property.getValue();
				break;
			default:
				break;
			}
		}
		if(StringUtils.isBlank(providerId)) {
			return;
		}
		// read coupon codes
		List<String> couponCodes = newEntities.stream().map(e -> e.getProperties())
				.flatMap(p -> Arrays.asList(p).stream()).filter(p -> "code".equals(p.getName())).map(p -> p.getValue())
				.collect(Collectors.toList());
		
		HashMap<String, Object> vars = new HashMap<String, Object>();
		Customer provider = customerService.readCustomerById(Long.valueOf(providerId));
		vars.put("name", provider.getFirstName() + " " + provider.getLastName());
		vars.put("comments", "");
		vars.put("emailAddress", provider.getEmailAddress());
		
		EmailInfo emailInfo = new EmailInfo();
		
		emailInfo.setFromAddress("info@paycom.ge");
		emailInfo.setSubject("Paycom generated coupons ");
		StringBuffer messageBodyBuilder = new StringBuffer();
		messageBodyBuilder.append("Generated coupons: " + newEntities.size());
		messageBodyBuilder.append("<br />");
		messageBodyBuilder.append("Coupon name: " + couponName);
		messageBodyBuilder.append("<br />");
		messageBodyBuilder.append("valid from: " + validFrom + " until: " + validTo);
		messageBodyBuilder.append("<br />");
		messageBodyBuilder.append("Codes:");
		messageBodyBuilder.append(String.join("<br />", couponCodes));
		emailInfo.setMessageBody(messageBodyBuilder.toString());
		EmailTargetImpl emailTarget = new EmailTargetImpl();
		
		emailTarget.setEmailAddress(provider.getEmailAddress());
		try {
			emailService.sendBasicEmail(emailInfo, emailTarget, vars);
		} catch (Exception e) {
			e.printStackTrace();
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
