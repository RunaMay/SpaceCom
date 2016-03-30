package com.atlassian.plugins.project;


	import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.ofbiz.core.entity.GenericEntityException;

import com.atlassian.extras.common.log.Logger;
import com.atlassian.extras.common.log.Logger.Log;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.context.GlobalIssueContext;
import com.atlassian.jira.issue.context.JiraContextNode;
import com.atlassian.jira.issue.customfields.CustomFieldUtils;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigScheme;
import com.atlassian.jira.issue.fields.config.manager.FieldConfigSchemeManager;
import com.atlassian.jira.issue.fields.screen.FieldScreen;
import com.atlassian.jira.issue.fields.screen.FieldScreenTab;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.upm.api.license.PluginLicenseManager;

	/**
	 * The Class SynapseConfigActionSupport is perform configuration on admin-panel.
	 */
	public class ConfigActionSupport extends JiraWebActionSupport {

		private static final long serialVersionUID = 8404857305895598690L;

		/**
		 * The log serves to write out the description of the errors/ exception /
		 * warnings/ debug for particular class in the JIRA log file .
		 */
		private static final Log log = Logger.getInstance(ConfigActionSupport.class);

		
	
		/**
		 * The Constant DEFAULT_CF_NAME_REQUIREMENT determines the default
		 * Requirement custom field name.
		 */
		private static final String DEFAULT_CF_NAME_PROGRESS = "Progress";
		
		private static final String DEFAULT_CF_NAME_HOURS_LOGGED = "Logged Hours";
		
		private static final String DEFAULT_CF_NAME_HOURS_ESTIMATED = "Estimated Hours";



		/** The license for synapseRT plug-in. Can be valid and expired */
		private String license;

	
		/** The requirement custom field name. */
		private String progressReleaseCfName;
		
		private String hoursLoggedCfName;

		private String hoursEstimatedCfName;

		

		
		public String getHoursEstimatedCfName() {
			return hoursEstimatedCfName;
		}

		public void setHoursEstimatedCfName(String hoursEstimatedCfName) {
			this.hoursEstimatedCfName = hoursEstimatedCfName;
		}

		public String getHoursLoggedCfName() {
			return hoursLoggedCfName;
		}

		public void setHoursLoggedCfName(String hoursLoggedCfName) {
			this.hoursLoggedCfName = hoursLoggedCfName;
		}

		/** The properties. */
		private ApplicationProperties properties = ComponentAccessor
				.getApplicationProperties();

	

		/** The license manager performs operations with license. */
		private final PluginLicenseManager licenseManager;
		
		private String progressReleaseName;
		
		private String hoursLogged;
		private String hoursEstimated;


		public String getHoursEstimated() {
			hoursEstimated = getCFName(hoursEstimated,
				PluginUtil.getEstimatedHoursField(), DEFAULT_CF_NAME_HOURS_ESTIMATED);
			return hoursEstimated;
		}

		public void setHoursEstimated(String hoursEstimated) {
			this.hoursEstimated = hoursEstimated;
		}

		public String getHoursLogged() {
			hoursLogged = getCFName(hoursLogged,
					PluginUtil.getLoggedHoursField(), DEFAULT_CF_NAME_HOURS_LOGGED);
			return hoursLogged;
		}

		public void setHoursLogged(String hoursLogged) {
			this.hoursLogged = hoursLogged;
		}


		public String getProgressReleaseName() {
			progressReleaseName = getCFName(progressReleaseName,
					PluginUtil.getProgressReleaseField(), DEFAULT_CF_NAME_PROGRESS);
			return progressReleaseName;
		}

		public void setProgressReleaseName(String progressReleaseName) {
			this.progressReleaseName = PluginUtil
					.defaultString(progressReleaseName);
			
		}
		
		

		/**
		 * The Constructor for class SynapseConfigActionSupport.
		 * 
		 * @param licenseManager
		 *            the license manager performs operations with license.
		 * @throws Exception
		 *             is the general class of exceptions produced by failed or
		 *             interrupted operations
		 */
		public ConfigActionSupport(PluginLicenseManager  licenseManager)
				throws Exception {
			this.licenseManager = licenseManager;
		}

		/**
		 * Gets the license manager url.
		 * 
		 * @return the license manager url
		 */

		/**
		 * Do save advanced settings method is to set configuration for advanced
		 * admin-panel.
		 * 
		 * @return the string of response for saving request
		 */
		public String doSaveAdvancedSettings() {
			if (!hasPermission(Permissions.ADMINISTER)) {
				log.warn("No permission to configure the SYNAPSE plugin");
				return forceRedirect(request.getContextPath()
						+ "/secure/config.jspa");
			}

			return forceRedirect(request.getContextPath()
					+ "/secure/config.jspa");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see webwork.action.ActionSupport#doExecute()
		 */
		@Override
		protected String doExecute() throws Exception {

			if (!hasPermission(Permissions.ADMINISTER)) {
				log.warn("No permission to configure the SYNAPSE plugin.");
				
				return PERMISSION_VIOLATION_RESULT;
			}

			try {
				String lic = properties.getText(PluginConstants.PROGRESS_LICENSE_KEY); 

				license = lic;
				
			} catch (Exception ex) {

			}

			if (request.getMethod().equals("POST")) {
				return doInsertOrUpdate();
			}
		
			return INPUT;
		}



		/**
		 * Save the results from the submitted configuration form.
		 * 
		 * @return the string of response from saving request
		 * @throws Exception
		 *             is the general class of exceptions produced by failed or
		 *             interrupted operations
		 */
		private String doInsertOrUpdate() throws Exception {

			if (!hasPermission(Permissions.ADMINISTER)) {
				log.warn("No permission to configure the SYNAPSE plugin.");
			
				return PERMISSION_VIOLATION_RESULT;
			}

			

			progressReleaseName = PluginUtil.htmlEncodeStrLimit(request
					.getParameter("progressReleaseName"));
			
			hoursLogged = PluginUtil.htmlEncodeStrLimit(request
					.getParameter("hoursLogged"));
			hoursEstimated = PluginUtil.htmlEncodeStrLimit(request
					.getParameter("hoursEstimated"));


			setHoursLoggedCfName(hoursLogged);
			setProgressReleaseCfName(progressReleaseName);
			setHoursEstimatedCfName(hoursEstimated);
			
			installProgressCF();
			installLoggedCF();
			installEstimatedCF();
			return getRedirect("/secure/config.jspa");
		}

		/**
		 * Adjusts Test Suite Custom Field in SynapseRT plugin if such field exists.
		 * Else creates new Test Suite Custom Field and install it.
		 */
		private void installProgressCF() {
			if (progressReleaseCfName == null)
				progressReleaseCfName = DEFAULT_CF_NAME_PROGRESS;
			
			
			installSynapseCustomField(getIssueTypesFromProperties(), ProgressReleaseField.class,
					progressReleaseCfName,
					//"com.atlassian.jira.plugin.system.customfieldtypes:textfield",
					"com.atlassian.plugins.project:progress_field",
					"com.atlassian.jira.plugin.system.customfieldtypes:textsearcher",
				//"com.atlassian.plugins.project:progress_field_searcher",

					"com.atlassian.plugins.project.info", null);

		}

		/**
		 * Adjusts Test Suite Custom Field in SynapseRT plugin if such field exists.
		 * Else creates new Test Suite Custom Field and install it.
		 */
		private void installLoggedCF() {
			if (hoursLoggedCfName == null)
				hoursLoggedCfName = DEFAULT_CF_NAME_HOURS_LOGGED;
			
			
			installSynapseCustomField(getIssueTypesFromProperties(), LoggedHoursField.class,
					hoursLoggedCfName,
					//"com.atlassian.jira.plugin.system.customfieldtypes:textfield",
					"com.atlassian.plugins.project:logged_hours_field",
					"com.atlassian.jira.plugin.system.customfieldtypes:textsearcher",
				//"com.atlassian.plugins.project:progress_field_searcher",

					"com.atlassian.plugins.project.info", null);

		}
		private void installEstimatedCF() {
			if (hoursEstimatedCfName == null)
				hoursEstimatedCfName = DEFAULT_CF_NAME_HOURS_ESTIMATED;
			
			
			installSynapseCustomField(getIssueTypesFromProperties(), EstimatedHoursField.class,
					hoursEstimatedCfName,
					//"com.atlassian.jira.plugin.system.customfieldtypes:textfield",
					"com.atlassian.plugins.project:estimated_hours_field",
					"com.atlassian.jira.plugin.system.customfieldtypes:textsearcher",
				//"com.atlassian.plugins.project:progress_field_searcher",

					"com.atlassian.plugins.project.info", null);

		}
		
		
		
		private CustomField installSynapseCustomField(
				List<IssueType> issueTypes, Class<?> customFieldType,
				String defaultCustomFieldName, String customFieldPluginName,
				String customFieldSearcherPluginName,
				String i18nStringMessageResource, Object defaultValue) {
			List<CustomField> cfs = ComponentAccessor.getCustomFieldManager()
					.getCustomFieldObjects();
			log.error("Got all the custom fields");
			boolean exists = false;
			CustomField cf = null;
			if (cfs != null) {
				for (int i = 0; i < cfs.size(); i++) {
					cf = cfs.get(i);

					if (cf.getCustomFieldType().getClass().getName()
							.equals(customFieldType.getName())) {
						
						exists = true;
						break;
					}
				}
			}
			if (!exists) {
				try {
					List<JiraContextNode> contexts = new ArrayList<JiraContextNode>();
			        contexts.add(GlobalIssueContext.getInstance());

					CustomField newCF = ComponentAccessor.getCustomFieldManager()
							.createCustomField(
									defaultCustomFieldName,
									"Custom field  "
											+ defaultCustomFieldName
											+ " custom field type",
									ComponentAccessor.getCustomFieldManager()
											.getCustomFieldType(
													customFieldPluginName),
									ComponentAccessor.getCustomFieldManager()
											.getCustomFieldSearcher(
													customFieldSearcherPluginName),
													contexts, issueTypes);
					if (newCF != null) {
						if (defaultValue != null) {
							log.debug("Applying default value to "
									+ defaultCustomFieldName);
							List<FieldConfigScheme> configurations = newCF
									.getConfigurationSchemes();

							boolean defaultValueApplyed = false;
							for (int i = 0; i < configurations.size(); i++) {
								FieldConfigScheme scheme = configurations.get(i);
								if (scheme.isAllProjects()) {
									log.debug("Existing scheme found for "
											+ newCF.getName() + " custom field");
									FieldConfig fieldConfig = scheme
											.getOneAndOnlyConfig();
									if (defaultValue != null) {
										newCF.getCustomFieldType().setDefaultValue(
												fieldConfig, defaultValue);
										defaultValueApplyed = true;
										break;
									}
								}
							}
							if (!defaultValueApplyed)
								ModifyContextForCustomField(issueTypes, newCF,
										defaultValue);// unreal
							// situation

						}
						Collection<FieldScreen> screens = ComponentAccessor
								.getFieldScreenManager().getFieldScreens();

						FieldScreenTab tab = null;
						List<FieldScreenTab> tabs = null;
						if (screens != null) {
							for (FieldScreen screen : screens) {
								log.debug(" screen's name = " + screen.getName());
								if ("Default Screen".equals(screen.getName())) {
									tabs = ComponentAccessor.getFieldScreenManager()
											.getFieldScreenTabs(screen);

									if (tabs != null) {
										for (int i = 0; i < tabs.size(); i++) {
											tab = tabs.get(i);
											tab.addFieldScreenLayoutItem(newCF
													.getId());
											log.debug("Add "
													+ defaultCustomFieldName
													+ " custom field to tab("
													+ tab.getName() + ")");
										}
									}
								}
							}
							ComponentAccessor.getFieldScreenManager().refresh();
						}
						return newCF;
					}
				} catch (GenericEntityException e) {
					log.debug(defaultCustomFieldName
							+ " custom field create failed!", e);
				}
				return null;
			} else {
				if (StringUtils.isNotBlank(defaultCustomFieldName)) {
					CustomFieldManager cfM = ComponentAccessor.getCustomFieldManager();
					cfM.updateCustomField(cf.getIdAsLong(), defaultCustomFieldName,
							cf.getDescription(), cf.getCustomFieldSearcher());

				}

				ModifyContextForCustomField(issueTypes, cf, defaultValue);
			}
			return cf;
		}
		
		
		
		void ModifyContextForCustomField(List<IssueType> issueTypes,
				CustomField customField, Object defaultValue) {
			
			FieldConfigSchemeManager fieldConfigSchemeManager = ComponentAccessor
					.getFieldConfigSchemeManager();
			List<FieldConfigScheme> configurations = customField
					.getConfigurationSchemes();

			for (int i = 0; i < configurations.size(); i++) {
				FieldConfigScheme scheme = configurations.get(i);

				if (scheme.isAllProjects()) {
					log.debug("Readjusting existing global context for Synapse "
							+ customField.getName() + " custom field");
					FieldConfig fieldConfig = scheme.getOneAndOnlyConfig();
					if (defaultValue != null) {
						customField.getCustomFieldType().setDefaultValue(
								fieldConfig, defaultValue);
					}

					ProjectManager projectManager = ComponentAccessor.getProjectManager();
					List<Project> projects = projectManager.getProjectObjects();
					
					for (Project project : projects) {
						
					}
					
					List<JiraContextNode> newContexts = CustomFieldUtils
							.buildJiraIssueContexts(true, null, projectManager);
					Map<String, FieldConfig> newConfigs = new HashMap<String, FieldConfig>();
					for (int j = 0; j < issueTypes.size(); j++) {
						newConfigs.put(issueTypes.get(j).getId(),
								fieldConfig);
					}

					FieldConfigScheme newScheme = new FieldConfigScheme.Builder(
							scheme).setConfigs(newConfigs).toFieldConfigScheme();
					fieldConfigSchemeManager.updateFieldConfigScheme(newScheme,
							newContexts, customField);
					ComponentAccessor.getCustomFieldManager().refresh();
					return;
				}

			}
			String schemeName = "Default Configuration Scheme for "
					+ customField.getName();

			FieldConfigScheme configScheme = new FieldConfigScheme.Builder()
					.setName(schemeName).setDescription(schemeName)
					.toFieldConfigScheme();

			log.debug("Creating global context for Synapse "
					+ customField.getName() + " custom field");

			List<JiraContextNode> newContexts = CustomFieldUtils
					.buildJiraIssueContexts(true, null, null);

			configScheme = fieldConfigSchemeManager.createFieldConfigScheme(
					configScheme, newContexts, issueTypes, customField);
			if (defaultValue != null) {
				customField.getCustomFieldType().setDefaultValue(
						configScheme.getOneAndOnlyConfig(), defaultValue);
			}

		}

		/**
		 * Gets the issue types from application properties.
		 *
		 * @param issueTypePropertyEntries
		 *            the issue type property entries that are present in system.
		 * @return the issue types from properties
		 */
		private List<IssueType> getIssueTypesFromProperties() {
			List<IssueType> issueTypes = new ArrayList<IssueType>();
			Collection<IssueType> types = ComponentAccessor.getConstantsManager()
					.getAllIssueTypeObjects();
			log.debug("Got all issue types, " + types);
			
			for (IssueType type : types) {
				if (issueTypes == null)
					issueTypes = new ArrayList<IssueType>();
				issueTypes.add(type);
			}
			return issueTypes;
		}

		/**
		 * Sets the license.
		 * 
		 * @param license
		 *            the license
		 */
		public void setLicense(String license) {
			this.license = license;
		}

		/**
		 * Gets the license.
		 * 
		 * @return the license
		 */
		public String getLicense() {
			return license == null ? "" : license;
		}

	



		/**
		 * For a given custom field name, either return the already set value or set
		 * it to its initial value and return it. Note that null values mean the
		 * name has not yet been set.
		 * 
		 * @param cfName
		 *            the custom field name
		 * @param customField
		 *            the custom field
		 * @param defaultVal
		 *            the default value
		 * @return the custom field name
		 */
		private String getCFName(String cfName, CustomField customField,
				String defaultVal) {
			if (cfName == null) {
				cfName = (customField != null ? customField.getName() : defaultVal);
				cfName = StringUtils.defaultString(cfName); // Ensure the value is
															// never set to null
															// (null means
															// uninitialized)
			}
			return cfName;
		}



		/**
		 * Sets the parent requirement custom field name.
		 * 
		 * @param parentRequirementCfName
		 *            the parent requirement custom field name
		 */
		public void setProgressReleaseCfName(String progressReleaseCfName) {
			this.progressReleaseCfName = progressReleaseCfName; // Ensure we never set
																// value to null
																// (null means
																// uninitialized)
		}

		/**
		 * Gets the requirement custom field name.
		 * 
		 * @return the requirement custom field name
		 */
		public String getProgressReleaseCFName() {
			return progressReleaseCfName;
		}

		
		
	}