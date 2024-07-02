package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.TaskStatusEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The TaskEntity class represents the "task" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "task")
@NamedQueries({
        @NamedQuery(name = "Task.findById", query = "SELECT t FROM TaskEntity t WHERE t.id = :id"),
        @NamedQuery(name = "Task.findAll", query = "SELECT t FROM TaskEntity t"),
        @NamedQuery(name = "Task.findByProject", query = "SELECT t FROM TaskEntity t WHERE t.project.id = :id AND t.isDeleted = false"),
        @NamedQuery(name = "Task.findBySystemTitle", query = "SELECT t FROM TaskEntity t WHERE t.systemTitle = :systemTitle")
})
public class TaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the task. This is the primary key in the "task" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The title of the task.
     */
    @Column(name = "title", nullable = false, unique = false, updatable = true)
    private String title;

    /**
     * The system title of the task.
     */
    @Column(name = "systemTitle", nullable = false, unique = false, updatable = true)
    private String systemTitle;

    /**
     * The description of the task.
     */
    @Column(name = "description", nullable = false, unique = false, updatable = true)
    private String description;

    /**
     * The status of the task. This is an enumerated type.
     */
    @Convert(converter = TaskStatusEnumConverter.class)
    @Column(name = "status", nullable = false, unique = false, updatable = true)
    private TaskStatusENUM status;

    /**
     * The initial date of the task.
     */
    @Column(name = "initialDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime initialDate;

    /**
     * The final date of the task.
     */
    @Column(name = "finalDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime finalDate;

    /**
     * The out collaboration of the task.
     */
    @Column(name = "outColaboration", nullable = true, unique = false, updatable = true)
    private String outColaboration;

    @Column(name = "isDeleted", nullable = false, unique = false, updatable = true)
    private boolean isDeleted;

    /**
     * The tasks that this task depends on. This is a many-to-many relationship with the TaskEntity class.
     */
    @ManyToMany
    private List<TaskEntity> dependentTasks;

    @ManyToMany(mappedBy = "dependentTasks")
    private List<TaskEntity> childTasks;

    /**
     * The user tasks associated with this task. This is a one-to-many relationship with the UserTaskEntity class.
     */
    @OneToMany(mappedBy = "task")
    private List<UserTaskEntity> userTasks;

    /**
     * The project associated with this task. This is a many-to-one relationship with the ProjectEntity class.
     */
    @ManyToOne
    private ProjectEntity project;

    /**
     * Default constructor.
     */
    public TaskEntity() {
    }

    /**
     * Returns the unique ID of this task.
     *
     * @return the ID of this task.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this task.
     *
     * @param id the new ID of this task.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the title of this task.
     *
     * @return the title of this task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this task.
     *
     * @param title the new title of this task.
     */
    public void setTitle(String title) {
        this.title = title;
    }


    public String getSystemTitle() {
        return systemTitle;
    }


    public void setSystemTitle(String systemTitle) {
        this.systemTitle = systemTitle;
    }

    /**
     * Returns the description of this task.
     *
     * @return the description of this task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this task.
     *
     * @param description the new description of this task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the status of this task.
     *
     * @return the status of this task.
     */
    public TaskStatusENUM getStatus() {
        return status;
    }

    /**
     * Sets the status of this task.
     *
     * @param status the new status of this task.
     */
    public void setStatus(TaskStatusENUM status) {
        this.status = status;
    }

    /**
     * Returns the initial date of this task.
     *
     * @return the initial date of this task.
     */
    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    /**
     * Sets the initial date of this task.
     *
     * @param initialDate the new initial date of this task.
     */
    public void setInitialDate(LocalDateTime initialDate) {
        this.initialDate = initialDate;
    }

    /**
     * Returns the final date of this task.
     *
     * @return the final date of this task.
     */
    public LocalDateTime getFinalDate() {
        return finalDate;
    }

    /**
     * Sets the final date of this task.
     *
     * @param finalDate the new final date of this task.
     */
    public void setFinalDate(LocalDateTime finalDate) {
        this.finalDate = finalDate;
    }

    /**
     * Returns the out collaboration of this task.
     *
     * @return the out collaboration of this task.
     */
    public String getOutColaboration() {
        return outColaboration;
    }

    /**
     * Sets the out collaboration of this task.
     *
     * @param outColaboration the new out collaboration of this task.
     */
    public void setOutColaboration(String outColaboration) {
        this.outColaboration = outColaboration;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    /**
     * Returns the tasks that this task depends on.
     *
     * @return the tasks that this task depends on.
     */
    public List<TaskEntity> getDependentTasks() {
        return dependentTasks;
    }

    /**
     * Sets the tasks that this task depends on.
     *
     * @param dependentTasks the new tasks that this task depends on.
     */
    public void setDependentTasks(List<TaskEntity> dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    public List<TaskEntity> getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(List<TaskEntity> childTasks) {
        this.childTasks = childTasks;
    }

    /**
     * Returns the project associated with this task.
     *
     * @return the project associated with this task.
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * Sets the project associated with this task.
     *
     * @param project the new project associated with this task.
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    /**
     * Returns the user tasks associated with this task.
     *
     * @return the user tasks associated with this task.
     */
    public List<UserTaskEntity> getUserTasks() {
        return userTasks;
    }

    /**
     * Sets the user tasks associated with this task.
     *
     * @param userTasks the new user tasks associated with this task.
     */
    public void setUserTasks(List<UserTaskEntity> userTasks) {
        this.userTasks = userTasks;
    }

    public int getDurationDays() {
        return (int) (finalDate.toLocalDate().toEpochDay() - initialDate.toLocalDate().toEpochDay());
    }
}
