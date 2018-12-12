package org.jenkinsci.plugins.badge.actions;

import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.Jenkins;

import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.QueryParameter;
import org.jenkinsci.plugins.badge.*;

/**
* @author Kohsuke Kawaguchi
*/
@SuppressWarnings("rawtypes")
public class JobBadgeAction implements Action {
    public final Job project;
    
    public JobBadgeAction(Job project) {
        this.project = project;
    }

    public String getIconFileName() {
        return Jenkins.RESOURCE_PATH+"/plugin/embeddable-build-status/images/24x24/shield.png";
    }

    public String getDisplayName() {
        return Messages.JobBadgeAction_DisplayName();
    }

    public String getUrlName() {
        return "badge";
    }

    /**
     * Serves the badge image.
     */
    public HttpResponse doIcon(@QueryParameter String style, @QueryParameter String build, @QueryParameter String subject, @QueryParameter String status, @QueryParameter String color, @QueryParameter String config, @QueryParameter String animatedOverlayColor) {
        if (build != null) {
            Run<?, ?> run = PublicBuildStatusAction.getRun(this.project, build);
            if (run != null) {
                return PluginImpl.iconRequestHandler.handleIconRequestForRun(run, style, subject, status, color, animatedOverlayColor, config);
            }
        }
            
        return PluginImpl.iconRequestHandler.handleIconRequestForJob(project, style, subject, status, color, animatedOverlayColor, config);
    }

    /**
     * Serves text.
     */
    public String doText() {
        return project.getIconColor().getDescription();
    }
}