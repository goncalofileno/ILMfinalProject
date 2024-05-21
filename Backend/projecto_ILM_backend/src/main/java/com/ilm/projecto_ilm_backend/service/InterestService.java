package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import com.ilm.projecto_ilm_backend.bean.InterestBean;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/interest")
public class InterestService {

    @Inject
    InterestBean interestBean;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllInterests() {
        List<InterestDto> interests = interestBean.getAllInterests();
        return Response.ok(interests).build();
    }
}
