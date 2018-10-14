package com.huneau;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Base64;

@Path("oauth")
public class OauthResources {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        return "Got it!";
    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public void login(@HeaderParam("Authorization")String basic){
        String[] login = String.valueOf(Base64.getDecoder().decode(basic)).split(":");
        String user = login[0];
        String password = login[1];
    }
}
