package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dao.LabDao;
import com.ilm.projecto_ilm_backend.dto.lab.LabDto;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import com.ilm.projecto_ilm_backend.mapper.LabMapper;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * The LabBean class is responsible for managing LabEntity instances.
 * It is an application scoped bean, meaning there is a single instance for the entire application.
 */
@ApplicationScoped
public class LabBean {

    /**
     * The LabDao instance used for accessing the database.
     */
    @EJB
    LabDao labDao;

    /**
     * Creates default labs for different locations if they do not exist.
     * The labs are created with predefined values for location and contact.
     */
    public void createDefaultLabsIfNotExistent() {
        if (labDao.findbyLocal(WorkLocalENUM.COIMBRA) == null) {
            LabEntity lab = new LabEntity();
            lab.setLocal(WorkLocalENUM.COIMBRA);
            lab.setContact("+351 239 247 000");
            labDao.persist(lab);
        }
        if (labDao.findbyLocal(WorkLocalENUM.LISBON) == null) {
            LabEntity lab = new LabEntity();
            lab.setLocal(WorkLocalENUM.LISBON);
            lab.setContact("+351 218 418 000");
            labDao.persist(lab);
        }
        if (labDao.findbyLocal(WorkLocalENUM.PORTO) == null) {
            LabEntity lab = new LabEntity();
            lab.setLocal(WorkLocalENUM.PORTO);
            lab.setContact("+351 220 408 000");
            labDao.persist(lab);
        }
        if (labDao.findbyLocal(WorkLocalENUM.TOMAR) == null) {
            LabEntity lab = new LabEntity();
            lab.setLocal(WorkLocalENUM.TOMAR);
            lab.setContact("+351 249 328 100");
            labDao.persist(lab);
        }
        if (labDao.findbyLocal(WorkLocalENUM.VISEU) == null) {
            LabEntity lab = new LabEntity();
            lab.setLocal(WorkLocalENUM.VISEU);
            lab.setContact("+351 232 480 000");
            labDao.persist(lab);
        }
        if (labDao.findbyLocal(WorkLocalENUM.VILA_REAL) == null) {
            LabEntity lab = new LabEntity();
            lab.setLocal(WorkLocalENUM.VILA_REAL);
            lab.setContact("+351 259 350 000");
            labDao.persist(lab);
        }
    }

    public List<LabDto> getAllLabs() {
        List<LabEntity> labs = labDao.findAll();
        List<LabDto> labDtos = new ArrayList<>();
        for (LabEntity lab : labs) {
            labDtos.add(LabMapper.toDto(lab));
        }
        return labDtos;
    }
}
