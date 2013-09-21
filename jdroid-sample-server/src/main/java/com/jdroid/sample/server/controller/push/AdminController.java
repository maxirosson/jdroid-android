package com.jdroid.sample.server.controller.push;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;
import com.jdroid.java.context.GitContext;
import com.jdroid.javaweb.context.DefaultApplication;

/**
 * @author Maxi Rosson
 */
@Path("admin")
@Controller
public class AdminController {
	
	@GET
	@Path("info")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAppInfo() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("App Name: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().getAppName());
		builder.append("\nApp Version: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().getAppVersion());
		builder.append("\nApp URL: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().getAppURL());
		builder.append("\nCommit Id: ");
		builder.append(GitContext.get().getCommitId());
		builder.append("\nCommit Time: ");
		builder.append(GitContext.get().getCommitTime());
		builder.append("\nCommit Build Time: ");
		builder.append(GitContext.get().getBuildTime());
		builder.append("\nHttp Mock Enabled: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().isHttpMockEnabled());
		builder.append("\nHttp Mock Sleep Duration: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().getHttpMockSleepDuration());
		return builder.toString();
	}
}
