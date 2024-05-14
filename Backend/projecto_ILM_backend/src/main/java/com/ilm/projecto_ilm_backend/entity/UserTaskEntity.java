package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_task")
public class UserTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private UserInTaskTypeENUM type;

    public UserTaskEntity() {
    }
}
