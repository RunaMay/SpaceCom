package com.atlassian.plugins.project;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JProgressBar;

import com.atlassian.extras.common.log.Logger;
import com.atlassian.extras.common.log.Logger.Log;
import com.atlassian.gzipfilter.org.tuckey.web.filters.urlrewrite.Conf;
import com.atlassian.jira.bc.issue.worklog.WorklogService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.GenericTextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.upm.api.license.PluginLicenseManager;

import cz.vutbr.web.css.CSSProperty.Speak;


public class ProgressReleaseField extends GenericTextCFType {

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
	public ProgressReleaseField(PluginLicenseManager licenseManager,
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


		super.updateValue(customField, issue, value);

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
		CustomFieldManager cfManager = ComponentAccessor.getCustomFieldManager();
		String width = "";
		String color = "";
		
		if(issue!=null){
			String estim = "100";
			String logg = "0";
			CustomField estimateField = PluginUtil.getEstimatedHoursField();
			if(estimateField!=null){
				String estimHours = (String) issue.getCustomFieldValue(estimateField);
				if(estimHours!=null&&!estimHours.isEmpty()&&Integer.parseInt(estimHours)!=0){
					estim = estimHours;
				}
			}
				
			CustomField loggedField = PluginUtil.getLoggedHoursField();
			if(loggedField!=null){
				String logHours = (String) issue.getCustomFieldValue(loggedField);
				if(logHours!=null&&!logHours.isEmpty()){
					logg = logHours;
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


String val = (String) issue.getCustomFieldValue(PluginUtil.getProgressReleaseField());
log.error(val+"-----issue val");
			//createValue
			
		}

/*		if(issue!=null){
			String progressBarPic = "";
			CustomField customField = null;
			List<CustomField> cfs = cfManager.getCustomFieldObjects(issue);
			for (CustomField cf : cfs) {
				if(cf.getName().equalsIgnoreCase("Story Points")){
					customField = cf;
				}
			}
			String value = "";
			if(customField!=null){
				value = (String) issue.getCustomFieldValue(customField);
				
				
			}else if (issue.getEstimate()!=null){
				Long time = issue.getEstimate();
				log.error(time+"------time");
				if(time!=null)
				value = time.toString();
			}else{
				log.error(PluginUtil
						.getProgressReleaseField() +"------custF");
				//value = "8";
				//(String) issue.getCustomFieldValue(PluginUtil
					//	.getProgressReleaseField());
			}
			Integer logged = 0;
			WorklogManager wlogM = ComponentAccessor.getWorklogManager();
			List<Worklog> logs =wlogM.getByIssue(issue);
			for (Worklog worklog : logs) {
				Long spent = worklog.getTimeSpent();
				log.error(spent+"-----spent");
				if(spent!=null)
				logged = spent.intValue();
			}
			
			log.error(value +"------"+logged);
			int estim = 0;
			if(value!=null)
				estim = Integer.parseInt(value);
			Integer progress = (estim/100)*logged;


		velocityParameters.put("progress", progress);
		}else{
			velocityParameters.put("progress", "");

		}*/
		velocityParameters.put("value", width);
		velocityParameters.put("width", width);
		velocityParameters.put("color", color);

		return velocityParameters;
	}

}