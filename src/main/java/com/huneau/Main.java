package com.huneau;

import com.huneau.resources.OauthResources;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.util.HtmlHelper;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.grizzly.http.util.HttpUtils;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://localhost:8080";

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 *
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer getServer() {
		// create a resource config that scans for JAX-RS resources and providers
		// in com.huneau package
		final ResourceConfig rc = new ResourceConfig()
				.register(OauthResources.class)
				.register(DefaultException.class)
				.packages("org.glassfish.jersey.examples.jackson")
				.register(JacksonFeature.class);

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc, false);
	}

	/**
	 * Main method.
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final HttpServer server = getServer();
		server.getServerConfiguration().setDefaultErrorPageGenerator((request, status, reasonPhrase, description, exception) -> {
			System.out.println(request.getAuthorization() + " " + status + " " + reasonPhrase + " " + description + " " + exception.getMessage());
			if (status == 404) {
				return HtmlHelper.getErrorPage(HttpStatus.NOT_FOUND_404.getReasonPhrase(),
						"Resource identified by path '" +
								HttpUtils.filter(request.getRequestURI()) +
								"', does not exist.",
						request.getServerName());
			}

			return HtmlHelper.getExceptionErrorPage(reasonPhrase, description,
					request.getServerName(),
					exception);
		});

		server.start();
		System.out.println(String.format("Jersey app started with WADL available at "
				+ "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
		System.in.read();
		server.shutdown();
	}
}

