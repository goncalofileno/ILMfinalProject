package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.SkillBean;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * REST service for skill-related operations.
 */
@Path("/skill")
public class SkillService {

    /**
     * Injected SkillBean EJB
     */
    @Inject
    SkillBean skillBean;

    /**
     * Retrieves all skills.
     *
     * @return HTTP response containing a list of all skills
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSkills() {
        List<SkillDto> skills = skillBean.getAllSkills();
        return Response.ok(skills).build();
    }
}
