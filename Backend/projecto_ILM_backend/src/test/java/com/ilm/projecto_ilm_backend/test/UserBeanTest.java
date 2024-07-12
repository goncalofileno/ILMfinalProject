package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.ENUMS.LanguageENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.bean.TaskBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.user.RegisterUserDto;
import com.ilm.projecto_ilm_backend.dto.user.ShowProfileDto;
import com.ilm.projecto_ilm_backend.dto.user.UserProfileDto;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import com.ilm.projecto_ilm_backend.entity.SessionEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedException;
import com.ilm.projecto_ilm_backend.utilities.EmailService;
import com.ilm.projecto_ilm_backend.utilities.HashUtil;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserBeanTest {

    @InjectMocks
    private UserBean userBean;

    @Mock
    private UserDao  userDao;

    @Mock
    private InterestDao interestDao;

    @Mock
    private LabDao labDao;

    @Mock
    private SkillDao skillDao;

    @Mock
    private EmailService emailService;

    @Mock
    private SessionDao sessionDao;

    @Mock
    private UserProjectDao userProjectDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setMail("test@example.com");
        registerUserDto.setPassword("password");

        // Mocking dependencies
        doNothing().when(emailService).sendConfirmationEmail(anyString(), anyString());

        // Execute the method
        boolean result = userBean.registerUser(registerUserDto);

        // Assertions
        assertTrue(result);
        verify(userDao, times(1)).persist(any(UserEntity.class));
    }

    @Test
    public void testConfirmEmail_Success() {
        String token = "sampleToken";
        UserEntity user = new UserEntity();
        when(userDao.findByAuxiliarToken(token)).thenReturn(user);

        // Execute the method
        boolean result = userBean.confirmEmail(token);

        // Assertions
        assertTrue(result);
        verify(userDao, times(1)).merge(user);
    }

    @Test
    public void testCreateProfile_Success() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setFirstName("First");
        userProfileDto.setLastName("Last");
        userProfileDto.setUsername("username");
        userProfileDto.setLab("COIMBRA");
        userProfileDto.setBio("Bio");
        userProfileDto.setPublicProfile(true);
        userProfileDto.setSkills(new ArrayList<>());
        userProfileDto.setInterests(new ArrayList<>());
        String token = "sampleToken";

        UserEntity user = new UserEntity();
        when(userDao.findByAuxiliarToken(token)).thenReturn(user);
        when(labDao.findbyLocal(WorkLocalENUM.COIMBRA)).thenReturn(new LabEntity());
        when(skillDao.findByName(anyString())).thenReturn(Optional.empty());
        when(interestDao.findByName(anyString())).thenReturn(Optional.empty());

        // Execute the method
        boolean result = userBean.createProfile(userProfileDto, token);

        // Assertions
        assertTrue(result);
        verify(userDao, times(1)).merge(user);
    }

    @Test
    public void testLoginUser_Success() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setMail("test@example.com");
        registerUserDto.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("username");

        when(userDao.checkPassFromEmail(anyString(), anyString())).thenReturn(true);
        when(userDao.findByEmail(anyString())).thenReturn(userEntity);
        when(sessionDao.findByUserId(anyInt())).thenReturn(null);

        // Execute the method
        String sessionId = userBean.loginUser(registerUserDto, "127.0.0.1", "UserAgent");

        // Assertions
        assertNotNull(sessionId);
        verify(sessionDao, times(1)).persist(any(SessionEntity.class));
    }
    @Test
    public void testSendForgetPassLink_Success() throws MessagingException, UnsupportedEncodingException {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);

        when(userDao.findByEmail(email)).thenReturn(user);
        doNothing().when(emailService).sendResetPasswordEmail(anyString(), anyString());

        // Execute the method
        boolean result = userBean.sendForgetPassLink(email);

        // Assertions
        assertTrue(result);
        verify(userDao, times(1)).merge(user);
    }

    @Test
    public void testResetPassword_Success() {
        UserEntity user = new UserEntity();
        user.setAuxiliarToken("validToken");

        // Mock userDao.findByAuxiliarToken to return a user
        when(userDao.findByAuxiliarToken(anyString())).thenReturn(user);

        // Call the method
        boolean result = userBean.resetPassword("validToken", "newPassword");

        // Verify that userDao.merge was called
        verify(userDao, times(1)).merge(user);
        assertTrue(result);
    }

    @Test
    public void testGetUserBySessionId_Success() {
        UserEntity user = new UserEntity();
        SessionEntity session = new SessionEntity();
        session.setUser(user);

        // Mock sessionDao.findBySessionId to return a session
        when(sessionDao.findBySessionId(anyString())).thenReturn(session);

        // Call the method
        UserEntity result = userBean.getUserBySessionId("validSessionId");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void testGetUserBySystemUsername_Success() {
        UserEntity user = new UserEntity();

        // Mock userDao.findBySystemUsername to return a user
        when(userDao.findBySystemUsername(anyString())).thenReturn(user);

        // Call the method
        UserEntity result = userBean.getUserBySystemUsername("systemUsername");

        assertNotNull(result);
        assertEquals(user, result);
    }


    @Test
    public void testValidatePassword_Success() {
        UserEntity user = new UserEntity();
        user.setPassword(HashUtil.toSHA256("password"));

        // Call the method
        boolean result = userBean.validatePassword(user, "password");

        assertTrue(result);
    }

    @Test
    public void testUpdatePassword_Success() {
        UserEntity user = new UserEntity();

        // Call the method
        boolean result = userBean.updatePassword(user, "newPassword");

        // Verify that userDao.merge was called
        verify(userDao, times(1)).merge(user);
        assertTrue(result);
    }



    @Test
    public void testIsUserAdmin_Success() {
        UserEntity user = new UserEntity();
        user.setType(UserTypeENUM.ADMIN);

        // Mock userDao.findById to return a user
        when(userDao.findById(anyInt())).thenReturn(user);

        // Call the method
        boolean result = userBean.isUserAdmin(1);

        assertTrue(result);
    }
    @Test
    public void testIsUserAdmin_UserNotFound() {
        // Given
        int userId = 1;

        // Mock the behavior of the UserDao
        when(userDao.findById(userId)).thenReturn(null);

        // When
        boolean isAdmin = userBean.isUserAdmin(userId);

        // Then
        assertFalse(isAdmin);
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    public void testIsUserAdmin_UserNotAdmin() {
        // Given
        int userId = 2;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setType(UserTypeENUM.STANDARD_USER);

        // Mock the behavior of the UserDao
        when(userDao.findById(userId)).thenReturn(user);

        // When
        boolean isAdmin = userBean.isUserAdmin(userId);

        // Then
        assertFalse(isAdmin);
        verify(userDao, times(1)).findById(userId);
    }
    @Test
    public void testUserHasProjects_Success() {
        UserEntity user = new UserEntity();
        when(sessionDao.findBySessionId(anyString())).thenReturn(new SessionEntity() {{
            setUser(user);
        }});
        when(userProjectDao.userHasProjects(anyInt())).thenReturn(true);

        boolean result = userBean.userHasProjects("validSessionId");

        assertTrue(result);
    }
    @Test
    public void testUserHasProjects_InvalidSession() {
        when(sessionDao.findBySessionId(anyString())).thenReturn(null);

        boolean result = userBean.userHasProjects("invalidSessionId");

        assertFalse(result);
    }
    @Test
    public void testPromoteUserToAdmin_Success() {
        UserEntity adminUser = new UserEntity();
        adminUser.setType(UserTypeENUM.ADMIN);
        UserEntity userToPromote = new UserEntity();
        userToPromote.setType(UserTypeENUM.STANDARD_USER);

        when(sessionDao.findBySessionId(anyString())).thenReturn(new SessionEntity() {{
            setUser(adminUser);
        }});
        when(userDao.findBySystemUsername(anyString())).thenReturn(userToPromote);

        boolean result = userBean.promoteUserToAdmin("validSessionId", "userToPromote");

        assertTrue(result);
        verify(userDao, times(1)).merge(any(UserEntity.class));
    }
    @Test
    public void testPromoteUserToAdmin_NotAdmin() {
        UserEntity nonAdminUser = new UserEntity();
        nonAdminUser.setType(UserTypeENUM.STANDARD_USER);
        UserEntity userToPromote = new UserEntity();
        userToPromote.setType(UserTypeENUM.STANDARD_USER);

        when(sessionDao.findBySessionId(anyString())).thenReturn(new SessionEntity() {{
            setUser(nonAdminUser);
        }});
        when(userDao.findBySystemUsername(anyString())).thenReturn(userToPromote);

        boolean result = userBean.promoteUserToAdmin("validSessionId", "userToPromote");

        assertFalse(result);
        verify(userDao, times(0)).merge(any(UserEntity.class));
    }

    @Test
    public void testGetEditProfile_Unauthorized() {
        when(sessionDao.findBySessionId(anyString())).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> {
            userBean.getEditProfile("systemUsername", "invalidSessionId");
        });
    }

    @Test
    public void testUpdateLanguage_Success() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setLanguage(LanguageENUM.PORTUGUESE);

        when(sessionDao.findBySessionId(anyString())).thenReturn(new SessionEntity() {{
            setUser(user);
        }});
        when(userDao.findById(anyInt())).thenReturn(user);

        boolean result = userBean.updateLanguage("validSessionId", LanguageENUM.ENGLISH);

        assertTrue(result);
        verify(userDao, times(1)).merge(any(UserEntity.class));
        assertEquals(LanguageENUM.ENGLISH, user.getLanguage());
    }
    @Test
    public void testUpdateLanguage_InvalidSession() {
        when(sessionDao.findBySessionId(anyString())).thenReturn(null);

        boolean result = userBean.updateLanguage("invalidSessionId", LanguageENUM.ENGLISH);

        assertFalse(result);
        verify(userDao, times(0)).merge(any(UserEntity.class));
    }
}