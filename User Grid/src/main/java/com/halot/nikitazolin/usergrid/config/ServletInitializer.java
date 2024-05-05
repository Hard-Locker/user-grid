package com.halot.nikitazolin.usergrid.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Profile;

import com.halot.nikitazolin.usergrid.UserGrid;

/**
 * Servlet initializer for deploying a Spring MVC application on an external
 * Tomcat server.
 *
 * This class extends SpringBootServletInitializer, allowing the creation of a
 * WAR archive for deployment on an external server. It is activated only when
 * the 'external' profile is used, providing flexibility in choosing the runtime
 * environment (embedded server or external server).
 *
 * The 'external' profile must be activated when deployment on an external
 * server is required. If running on an embedded server, this class is not used,
 * and the application runs as a standard Spring Boot application.
 *
 * Usage: - For running on an embedded server: do not set a profile or specify
 * the 'development' profile. - For running on an external server: activate the
 * 'external' profile.
 *
 * @author Nikita Zolin
 */
@Profile("external")
public class ServletInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(UserGrid.class);
  }
}
