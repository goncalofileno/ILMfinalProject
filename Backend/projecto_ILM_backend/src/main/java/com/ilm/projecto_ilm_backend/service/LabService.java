package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.dto.lab.LabDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.ilm.projecto_ilm_backend.bean.LabBean;

import java.util.List;

/**
 * REST service for lab-related operations.
 */
@Path("/lab")
public class LabService {

    /**
     * Injected LabBean EJB
     */
    @Inject
    LabBean labBean;

    /**
     * Retrieves all labs.
     *
     * @return HTTP response containing a list of all labs
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLabs() {
        List<LabDto> labs = labBean.getAllLabs();
        return Response.ok(labs).build();
    }

}
