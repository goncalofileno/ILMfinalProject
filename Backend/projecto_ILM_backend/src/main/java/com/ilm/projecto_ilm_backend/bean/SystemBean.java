package com.ilm.projecto_ilm_backend.bean;


import com.ilm.projecto_ilm_backend.entity.SystemEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.ilm.projecto_ilm_backend.dao.SystemDao;



@ApplicationScoped
public class SystemBean {

    @Inject
    SystemDao systemDao;

    public void createDefaultSystemIfNotExistent() {
        if (systemDao.findConfigValueByName("timeout") == -1) {
            SystemEntity system = new SystemEntity();
            system.setName("timeout");
            system.setValue(24);
            systemDao.persist(system);
        }
    }
}
