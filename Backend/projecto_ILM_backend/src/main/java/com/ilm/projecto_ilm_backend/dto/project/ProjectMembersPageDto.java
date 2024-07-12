package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.util.List;

/**
 * Data Transfer Object for displaying a page of project members.
 * This class encapsulates information about project members, including their details,
 * the maximum number of members, the project's current state, name, skills required for the project,
 * and the type of user viewing the project.
 */
public class ProjectMembersPageDto {

     private List<ProjectMemberDto> projectMembers; // List of project members
     private int maxMembers; // Maximum number of members for the project
     private StateProjectENUM projectState; // Current state of the project
     private String projectName; // Name of the project
     private List<SkillDto> projectSkills; // Skills required for the project
     private UserInProjectTypeENUM userSeingProject; // Type of user viewing the project

     /**
      * Constructs a new ProjectMembersPageDto with specified details.
      *
      * @param projectMembers List of project members.
      * @param maxMembers Maximum number of members for the project.
      * @param projectState Current state of the project.
      * @param projectName Name of the project.
      * @param projectSkills Skills required for the project.
      * @param userSeingProject Type of user viewing the project.
      */
     public ProjectMembersPageDto(List<ProjectMemberDto> projectMembers, int maxMembers, StateProjectENUM projectState, String projectName, List<SkillDto> projectSkills, UserInProjectTypeENUM userSeingProject) {
          this.projectMembers = projectMembers;
          this.maxMembers = maxMembers;
          this.projectState = projectState;
          this.projectName = projectName;
          this.projectSkills = projectSkills;
          this.userSeingProject = userSeingProject;
     }

     /**
      * Default constructor.
      */
     public ProjectMembersPageDto() {
     }

     public List<ProjectMemberDto> getProjectMembers() {
          return projectMembers;
     }

     public void setProjectMembers(List<ProjectMemberDto> projectMembers) {
          this.projectMembers = projectMembers;
     }

     public int getMaxMembers() {
          return maxMembers;
     }

     public void setMaxMembers(int maxMembers) {
          this.maxMembers = maxMembers;
     }

     public StateProjectENUM getProjectState() {
          return projectState;
     }

     public void setProjectState(StateProjectENUM projectState) {
          this.projectState = projectState;
     }

     public String getProjectName() {
          return projectName;
     }

     public void setProjectName(String projectName) {
          this.projectName = projectName;
     }

     public List<SkillDto> getProjectSkills() {
          return projectSkills;
     }

     public void setProjectSkills(List<SkillDto> projectSkills) {
          this.projectSkills = projectSkills;
     }

     public UserInProjectTypeENUM getUserSeingProject() {
          return userSeingProject;
     }

     public void setUserSeingProject(UserInProjectTypeENUM userSeingProject) {
          this.userSeingProject = userSeingProject;
     }
}
