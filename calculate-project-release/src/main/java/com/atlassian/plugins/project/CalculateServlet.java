package com.atlassian.plugins.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.core.entity.GenericEntityException;

import com.atlassian.extras.common.log.Logger;
import com.atlassian.extras.common.log.Logger.Log;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.util.json.JSONObject;

public class CalculateServlet extends HttpServlet {

	private static Log log = Logger.getInstance(CalculateServlet.class);

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			doService(req, resp);
		} catch (NumberFormatException e) {
			log.error(e);
		} catch (JSONException e) {
			log.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			doService(req, resp);
		} catch (NumberFormatException e) {
			log.error(e);
		} catch (JSONException e) {
			log.error(e);
		}
	}

	/**
	 * Do service method performs operation with comments for test field in
	 * issue view mode.
	 *
	 * @param req
	 *            the request parameter in json form that is recieved by the
	 *            servlet In the request-URL, there are such parameters as
	 *            action, issueId, test.
	 * @param resp
	 *            the response parameter in json form that is sent from the
	 *            servlet In the response the output is written to the model
	 * @throws JSONException
	 *             the JSON exception in case of wrong formed JSON
	 * @throws IOException
	 *             is the general class of exceptions produced by failed or
	 *             interrupted I/O operations
	 */
	public void doService(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, NumberFormatException, JSONException {
		JSONObject model = new JSONObject();
		String projectName = PluginUtil.htmlEncodeStrLimit(req
				.getParameter("projectName"));
		ProjectManager projectManager = ComponentAccessor.getProjectManager();
		Project pr = projectManager.getProjectObjByName(projectName);
		IssueManager issueM = ComponentAccessor.getIssueManager();
		Collection<Long> issueIds;
		try {
			issueIds = issueM.getIssueIdsForProject(pr.getId());
			List<Issue> issues = issueM.getIssueObjects(issueIds);
			List<JSONObject> objects = new ArrayList<JSONObject>();

			for (Issue issue : issues) {
				JSONObject issueO = new JSONObject();
				issueO.put("key", issue.getKey());
				issueO.put("summary", issue.getSummary());
				String progress = "";
				CustomFieldManager cfM = ComponentAccessor.getCustomFieldManager();
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
					
	

				 progress = Integer.toString(percent);
						 
				}
				issueO.put("progress", progress);

				objects.add(issueO);
			}
			model.put("issues", objects);

		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		



		PrintWriter out = resp.getWriter();
		resp.setContentType("plain/text; charset=\"utf-8\"");
		out.write(model.toString());
	}
}

