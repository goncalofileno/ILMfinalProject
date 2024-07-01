package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.UserInTaskTypeEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The UserTaskEntity class represents the "user_task" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "user_task")
@NamedQuery(name = "UserTask.findById", query = "SELECT ut FROM UserTaskEntity ut WHERE ut.id = :id"
)
@NamedQuery(name = "UserTask.findUsersByTaskId", query = "SELECT ut.user FROM UserTaskEntity ut WHERE ut.task.id = :taskId"
)
@NamedQuery(name = "UserTask.findUserTypeByTaskIdAndUserId", query = "SELECT ut.type FROM UserTaskEntity ut WHERE ut.task.id = :taskId AND ut.user.id = :userId"
)
public class UserTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the user task. This is the primary key in the "user_task" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The type of the user in the task. This is an enumerated type.
     */
    @Convert(converter = UserInTaskTypeEnumConverter.class)
    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private UserInTaskTypeENUM type;

    /**
     * The user associated with this user task. This is a many-to-one relationship with the UserEntity class.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * The task associated with this user task. This is a many-to-one relationship with the TaskEntity class.
     */
    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    /**
     * Default constructor.
     */
    public UserTaskEntity() {
    }

    /**
     * Returns the unique ID of this user task.
     *
     * @return the ID of this user task.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this user task.
     *
     * @param id the new ID of this user task.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the user associated with this user task.
     *
     * @return the user associated with this user task.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Sets the user associated with this user task.
     *
     * @param user the new user associated with this user task.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Returns the task associated with this user task.
     *
     * @return the task associated with this user task.
     */
    public TaskEntity getTask() {
        return task;
    }

    /**
     * Sets the task associated with this user task.
     *
     * @param task the new task associated with this user task.
     */
    public void setTask(TaskEntity task) {
        this.task = task;
    }

    /**
     * Returns the type of the user in the task.
     *
     * @return the type of the user in the task.
     */
    public UserInTaskTypeENUM getType() {
        return type;
    }

    /**
     * Sets the type of the user in the task.
     *
     * @param type the new type of the user in the task.
     */
    public void setType(UserInTaskTypeENUM type) {
        this.type = type;
    }
}
