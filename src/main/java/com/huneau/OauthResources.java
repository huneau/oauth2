package com.huneau;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/oauth")
public class OauthResources {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getIt() {
		return "Got it!";
	}

	@GET
	@Path("/token")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("username") String username,
	                      @QueryParam("password") String password,
	                      @QueryParam("code") String code,
	                      @QueryParam("redirect_uri") String redirectURI,
	                      @QueryParam("refresh_token") String refreshToken,
	                      @QueryParam("grant_type") GrantType grantType,
	                      @QueryParam("client_id") String clientId,
	                      @QueryParam("client_secret") String clientSecret) {
		Algorithm algorithmHS = Algorithm.HMAC512("passphrase");
		switch (grantType) {
			case password: {
				String token = JWT.create()
						.withClaim("username", username)
						.withClaim("client_id", clientId)
						.withClaim("client_secret", clientSecret)
						.sign(algorithmHS);
				return Response.ok().header("Authorization", "Bearer " + token).build();
			}
			case authorization_code:
			case refresh_token:
			case client_credentials: {
				String token = JWT.create()
						.withClaim("client_id", clientId)
						.withClaim("client_secret", clientSecret)
						.sign(algorithmHS);
				return Response.ok().header("Authorization", "Bearer " + token).build();
			}
		}
		throw new WebApplicationException(400);
	}
}
