package com.ilm.projecto_ilm_backend.dao;

import jakarta.ejb.Stateless;
import com.ilm.projecto_ilm_backend.entity.NoteEntity;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Stateless
public class NoteDao extends AbstractDao<NoteEntity>{

    private static final long serialVersionUID = 1L;

    public NoteDao() {
        super(NoteEntity.class);
    }

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void persist(NoteEntity note) {
        em.persist(note);
    }

    public NoteEntity findById(int id) {
        try {
            return em.createNamedQuery("Note.findById", NoteEntity.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}
