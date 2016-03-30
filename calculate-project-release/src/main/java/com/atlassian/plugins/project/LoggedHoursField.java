package com.atlassian.plugins.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.extras.common.log.Logger;
import com.atlassian.extras.common.log.Logger.Log;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.bc.issue.IssueService.UpdateValidationResult;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.UpdateIssueRequest;
import com.atlassian.jira.issue.UpdateIssueRequest.UpdateIssueRequestBuilder;
import com.atlassian.jira.issue.customfields.impl.GenericTextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.upm.api.license.PluginLicenseManager;

public class LoggedHoursField extends GenericTextCFType {

	/**
	 * The log serves to write out the description of the errors/ exception /
	 * warnings/ debug for particular class in the JIRA log file .
	 */
	private static final Log log = Logger.getInstance(ProgressReleaseField.class);

 
	private final PluginLicenseManager licenseManager;
	/** The properties. */
	private ApplicationProperties properties = ComponentAccessor
			.getApplicationProperties();


	/**
	 * Constructor. All of the arguments are passed by JIRA.
	 *
	 * @param jiraBaseUrls
	 */
	public LoggedHoursField(PluginLicenseManager licenseManager,
			CustomFieldValuePersister customFieldValuePersister,
			GenericConfigManager genericConfigManager) {

		super(customFieldValuePersister, genericConfigManager);

		this.licenseManager = licenseManager;
	}


	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.atlassian.jira.issue.customfields.impl.AbstractCustomFieldType#
	 * getChangelogString(com.atlassian.jira.issue.fields.CustomField,
	 * java.lang.Object)
	 */
	@Override
	public String getChangelogString(CustomField field, String value) {

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.atlassian.jira.issue.customfields.impl.AbstractSingleFieldType#
	 * getChangelogValue(com.atlassian.jira.issue.fields.CustomField,
	 * java.lang.Object)
	 */
	@Override
	public String getChangelogValue(CustomField field, String value) {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.atlassian.jira.issue.customfields.impl.AbstractSingleFieldType#
	 * createValue(com.atlassian.jira.issue.fields.CustomField,
	 * com.atlassian.jira.issue.Issue, java.lang.Object)
	 */
	@Override
	public void createValue(CustomField field, Issue issue, String value) {
		super.createValue(field, issue, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.atlassian.jira.issue.customfields.impl.AbstractSingleFieldType#
	 * updateValue(com.atlassian.jira.issue.fields.CustomField,
	 * com.atlassian.jira.issue.Issue, java.lang.Object)
	 */
	@Override
	public void updateValue(CustomField customField, Issue issue, String value) {

		IssueManager issM = ComponentAccessor.getIssueManager();
		MutableIssue mutable = issM.getIssueByCurrentKey(issue.getKey());
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();

		///mutable.setCustomFieldValue(PluginUtil.getProgressReleaseField(), "");
		super.updateValue(customField, issue, value);
		//updateField(mutable, PluginUtil.getProgressReleaseField(),value, user);
	}
	
	public boolean updateField(MutableIssue mutableIssue, CustomField customField, String string, ApplicationUser user){
		
		Collection<String> fieldsToBeValidated = new ArrayList<String>();
		fieldsToBeValidated.add(customField.getId());
		IssueService issueService = ComponentAccessor.getIssueService();
		IssueInputParameters issueInputParameters = issueService
				.newIssueInputParameters();
		issueInputParameters.setSkipScreenCheck(true);
		issueInputParameters.setRetainExistingValuesWhenParameterNotProvided(
				true, true);
		issueInputParameters.setProvidedFields(fieldsToBeValidated);
		
		String width = "";
		String color = "";
		
		if(mutableIssue!=null){
			String estim = "100";
			String logg = "0";
			logg = string;
			
			CustomField estimateField = PluginUtil.getEstimatedHoursField();
			if(estimateField!=null){
				String estimHours = (String) mutableIssue.getCustomFieldValue(estimateField);
				if(estimHours!=null&&!estimHours.isEmpty()&&Integer.parseInt(estimHours)!=0){
					estim = estimHours;
				}
			}
		
			
			int percent = (Integer.parseInt(logg)*100)/Integer.parseInt(estim);
			
if(percent<60){
	color = "green";
}else if(percent>=60&&percent<85){
	color ="yellow";
}else{
	color = "red";
}

width = Integer.toString(percent);
		}

		issueInputParameters.addCustomFieldValue(customField.getIdAsLong(),width);

		UpdateValidationResult updateValidationResult = issueService
				.validateUpdate(user, mutableIssue.getId(),
						issueInputParameters);

		if (updateValidationResult.isValid()) {
			log.debug("update validation passed");
			IssueResult updateResult = issueService.update(user,
					updateValidationResult);
			if (updateResult.isValid()) {
				log.debug("update result valid");
				try {
					
					PluginUtil.reIndexIssue(updateResult.getIssue());
					return true;
				} catch (Exception e) {
					log.error("Error reindexing issue " + mutableIssue.getKey());
				}
			} else {
				ErrorCollection errorCollection = updateResult
						.getErrorCollection();
				log.warn("update result invalid: "
						+ errorCollection.getErrorMessages());
			}
		} else {
			ErrorCollection errorCollection = updateValidationResult
					.getErrorCollection();
			log.warn("update validation NOT passed: "
					+ errorCollection.getErrorMessages() + " | "
					+ errorCollection.getErrors());
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.atlassian.jira.issue.customfields.impl.AbstractCustomFieldType#
	 * getVelocityParameters(com.atlassian.jira.issue.Issue,
	 * com.atlassian.jira.issue.fields.CustomField,
	 * com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem)
	 */
	@Override
	public Map<String, Object> getVelocityParameters(Issue issue,
			CustomField field, FieldLayoutItem fieldLayoutItem) {

		Map<String, Object> velocityParameters = new HashMap<String, Object>();
		String value = "0";
		if(issue!=null){
			value = (String) issue.getCustomFieldValue(PluginUtil.getLoggedHoursField());
			log.error(value+"-----issue value LOGGED");

		}
		velocityParameters.put("value", value);

		return velocityParameters;
	}

}
