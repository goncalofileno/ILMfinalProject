package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="user_project")
public class UserProjectEntity implements Serializable{

        private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

        @ManyToOne
        @JoinColumn(name="user_id")
        private UserEntity user;

        @ManyToOne
        @JoinColumn(name="project_id")
        private ProjectEntity project;

        @Column(name="isManager", nullable = false, unique = false, updatable = true)
        private boolean isManager;

        @Column(name="isCreator", nullable = false, unique = false, updatable = false)
        private boolean isCreator;

        @Column(name="inviteStatus", nullable = true, unique = false, updatable = true)
        private int inviteStatus;

        public UserProjectEntity() {
        }
}
