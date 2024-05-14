package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.InviteStatusENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_project")
public class UserProjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private UserInProjectTypeENUM type;

    @Enumerated(EnumType.STRING)
    @Column(name = "inviteStatus", nullable = true, unique = false, updatable = true)
    private InviteStatusENUM inviteStatus;

    public UserProjectEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public UserInProjectTypeENUM getType() {
        return type;
    }

    public void setType(UserInProjectTypeENUM type) {
        this.type = type;
    }

    public InviteStatusENUM getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(InviteStatusENUM inviteStatus) {
        this.inviteStatus = inviteStatus;
    }
}
