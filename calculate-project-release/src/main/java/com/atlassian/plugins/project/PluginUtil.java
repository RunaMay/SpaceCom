package com.atlassian.plugins.project;

import java.util.Iterator;
import java.util.List;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.index.IssueIndexManager;
import com.atlassian.jira.util.ImportUtils;
import com.opensymphony.util.TextUtils;

public class PluginUtil {
	/**
	 * /** Html encode str limit method encode string in html format.
	 * 
	 * @param str
	 *            the string to be encoded
	 * @param limit
	 *            the string length limit to be encoded
	 * @return the encoded string
	 */
	public static String htmlEncodeStrLimit(String str, int limit) {
		if (str == null || str.isEmpty())
			return str;
		if (str.length() > limit)
			str = str.substring(0, limit);
		str = TextUtils.htmlEncode(str).replaceAll("`", "");
		return str;
	}
	 public static String defaultString(String str) {
	        return str == null ? "" : str;
	    }
	 
	/**
	 * Html encode str limit method encode string in html format.
	 * 
	 * @param str
	 *            the string to be encoded
	 * @return the encoded string
	 */
	public static String htmlEncodeStrLimit(String str) {
		return htmlEncodeStrLimit(str, 200);
	}

	public static CustomField getProgressReleaseField() {
		// TODO Auto-generated method stub
		CustomField cfTS = null;
		// if mercBugField == null ,get mercBugField by looking all the custom
		// fields
		List<CustomField> customfields = ComponentAccessor.getCustomFieldManager()
				.getCustomFieldObjects();
		for (Iterator<CustomField> iterator = customfields.iterator(); iterator
				.hasNext();) {
			CustomField customfield = iterator.next();
			if (customfield.getCustomFieldType() instanceof ProgressReleaseField) {
				cfTS = customfield;
			}
		}
		if (cfTS == null) {
		} else {
		}
		return cfTS;
	}
	
	public static CustomField getLoggedHoursField() {
		// TODO Auto-generated method stub
		CustomField cfTS = null;
		// if mercBugField == null ,get mercBugField by looking all the custom
		// fields
		List<CustomField> customfields = ComponentAccessor.getCustomFieldManager()
				.getCustomFieldObjects();
		for (Iterator<CustomField> iterator = customfields.iterator(); iterator
				.hasNext();) {
			CustomField customfield = iterator.next();
			if (customfield.getCustomFieldType() instanceof LoggedHoursField) {
				cfTS = customfield;
			}
		}
		if (cfTS == null) {
		} else {
		}
		return cfTS;
	}
	public static CustomField getEstimatedHoursField() {
		// TODO Auto-generated method stub
		CustomField cfTS = null;
		// if mercBugField == null ,get mercBugField by looking all the custom
		// fields
		List<CustomField> customfields = ComponentAccessor.getCustomFieldManager()
				.getCustomFieldObjects();
		for (Iterator<CustomField> iterator = customfields.iterator(); iterator
				.hasNext();) {
			CustomField customfield = iterator.next();
			if (customfield.getCustomFieldType() instanceof EstimatedHoursField) {
				cfTS = customfield;
			}
		}
		if (cfTS == null) {
		} else {
		}
		return cfTS;
	}
	
	
	/**
	 * Re index issue. Operation for setting indexes.
	 * 
	 * @param issue
	 *            the issue
	 */
	public static void reIndexIssue(MutableIssue issue) {
		// re-index this issue
		boolean imp = ImportUtils.isIndexIssues();
		try {

			ImportUtils.setIndexIssues(true);
			// ComponentUtil.getCacheManager().flush(CacheManager.ISSUE_CACHE,
			// issue);//removed from jira 4.3
			ComponentAccessor
			.getComponent(IssueIndexManager.class).reIndex(issue);

		} catch (Exception e) {
						} finally {
			ImportUtils.setIndexIssues(imp);
		}
	}
	}
