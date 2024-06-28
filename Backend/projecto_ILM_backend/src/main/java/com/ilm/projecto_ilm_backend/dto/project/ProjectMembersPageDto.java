package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.util.List;

public class ProjectMembersPageDto {

     private List<ProjectMemberDto> projectMembers;
     private StateProjectENUM projectState;
     private String projectName;
     private List<SkillDto> projectSkills;
     private UserInProjectTypeENUM userSeingProject;

     public ProjectMembersPageDto(List<ProjectMemberDto> projectMembers, StateProjectENUM projectState, String projectName, List<SkillDto> projectSkills, UserInProjectTypeENUM userSeingProject) {
          this.projectMembers = projectMembers;
          this.projectState = projectState;
          this.projectName = projectName;
          this.projectSkills = projectSkills;
          this.userSeingProject = userSeingProject;
     }

     public ProjectMembersPageDto() {
     }

     public List<ProjectMemberDto> getProjectMembers() {
          return projectMembers;
     }

     public void setProjectMembers(List<ProjectMemberDto> projectMembers) {
          this.projectMembers = projectMembers;
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
