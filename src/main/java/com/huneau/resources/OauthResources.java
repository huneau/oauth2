package com.huneau.resources;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.huneau.GrantType;
import com.huneau.responses.AccessResponse;
import com.huneau.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/oauth2")
public class OauthResources {

    private UserService userService;

    private String googleClientID = "693669071178-dl222skcqdd5s1enmap3clqjhrd6dl79.apps.googleusercontent.com";
    private String googleClientSecret = "mHX8wqZpxAU8WJSiXfM6vOBo";
    private String authorizeURL = "https://accounts.google.com/o/oauth2/auth";
    private String tokenURL = "https://www.googleapis.com/oauth2/v3/token";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        return "Got it!";
    }

    @POST
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(@QueryParam("username") String username,
                          @QueryParam("password") String password,
                          @QueryParam("code") String code,
                          @QueryParam("redirect_uri") String redirectURI,
                          @QueryParam("refresh_token") String refreshToken,
                          @QueryParam("scope") String scope,
                          @QueryParam("grant_type") GrantType grantType,
                          @QueryParam("client_id") String clientId,
                          @QueryParam("client_secret") String clientSecret) {
        Algorithm algorithmHS = Algorithm.HMAC512("passphrase");
        switch (grantType) {
            case password: {
                if (userService.isValid(username, password)) {
                    String token = JWT.create()
                            .withClaim("username", username)
                            .withClaim("client_id", clientId)
                            .withClaim("client_secret", clientSecret)
                            .sign(algorithmHS);
                    return Response.status(Response.Status.CREATED)
                            .entity(new AccessResponse(token, null, "Bearer", 3600, null))
                            .build();
                }
                break;
            }
            case authorization_code:
            case refresh_token:
            case client_credentials: {
                String token = JWT.create()
                        .withClaim("client_id", clientId)
                        .withClaim("client_secret", clientSecret)
                        .sign(algorithmHS);
                return Response.status(Response.Status.CREATED)
                        .entity(new AccessResponse(token, null, "Bearer", 3600, null))
                        .build();
            }
        }
        throw new WebApplicationException(400);
    }

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login() {
        Map<String, String> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", googleClientID);
        params.put("redirect_uri", "http://lvh.me:8080/oauth2/token");
        params.put("scope", "email");
        params.put("state", "123456");
        String paramsString = params.entrySet().stream().map(entry -> entry.getKey()+"="+entry.getValue()).collect(Collectors.joining("&"));
        return Response.status(301).header("Location", authorizeURL + "?"+paramsString).build();
    }
}
