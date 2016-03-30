package com.atlassian.plugins.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.atlassian.extras.common.log.Logger;
import com.atlassian.extras.common.log.Logger.Log;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.upm.api.license.PluginLicenseManager;

public class ProjectDashboard extends JiraWebActionSupport {
	

	
		/**
		 * The log serves to write out the description of the errors/ exception /
		 * warnings/ debug for particular class in the JIRA log file .
		 */
		private static Log log = Logger.getInstance(ProjectDashboard.class);


		/** The projects to display on filter. */
		private List<Project> projects;

		/** The visible true/false. */
		private boolean visible = true;

		/** The license manager is to perform operations with licenses. */
		private final PluginLicenseManager licenseManager;

		/** The web resource manager to operate the web resources. */
		private WebResourceManager webResourceManager;

		/**
		 * Instantiates a new main dashboard.
		 *
		 * @param webResourceManager
		 *            the web resource manager to operate the web resources
		 * @param licenseManager
		 *            the license manager is to work with licenses
		 */
		public ProjectDashboard(WebResourceManager webResourceManager,
				PluginLicenseManager licenseManager) {
			this.webResourceManager = webResourceManager;
			this.licenseManager = licenseManager;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.atlassian.jira.action.JiraActionSupport#execute()
		 */
		@Override
		public String execute() throws Exception {
			

			return SUCCESS;
		}

	

		/**
		 * Checks if the dashboard is visible.
		 *
		 * @return true, if the dashboard is visible
		 */
		public boolean isVisible() {
			return visible;
		}

		/**
		 * Sets the visible dashboard true/false.
		 *
		 * @param visible
		 *            the new visible dashboard true/false
		 */
		public void setVisible(boolean visible) {
			this.visible = visible;
		}


		/**
		 * Gets the projects.
		 *
		 * @return the projects
		 */
		@SuppressWarnings("deprecation")
		public List<Project> getProjects() {

			ProjectManager projectManager = ComponentAccessor.getProjectManager();
			List<Project> projectsAll = projectManager.getProjectObjects();
			List<Project> projectsList = new ArrayList<Project>();

			for (Project project : projectsAll) {
				if (!ComponentAccessor.getPermissionManager().hasPermission(
						Permissions.BROWSE,
						project,
						ComponentAccessor.getJiraAuthenticationContext()
								.getLoggedInUser(), true)) {
				} else {
					projectsList.add(project);
				}
			}
			setProjects(projectsList);
			return projects;
		}

		/**
		 * Gets the web resource manager.
		 *
		 * @return the web resource manager
		 */
		public WebResourceManager getWebResourceManager() {
			return webResourceManager;
		}

		/**
		 * Checks if is fisheye enabled.
		 *
		 * @return true, if is fisheye enabled
		 */

		public void setProjects(List<Project> projects) {
			this.projects = projects;
		}
	}
