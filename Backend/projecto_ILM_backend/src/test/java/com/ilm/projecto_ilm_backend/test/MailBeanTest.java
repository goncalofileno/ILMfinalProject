package com.ilm.projecto_ilm_backend.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ilm.projecto_ilm_backend.bean.MailBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dao.MailDao;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dto.mail.ContactDto;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.entity.MailEntity;
import com.ilm.projecto_ilm_backend.entity.SessionEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;


import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

class MailBeanTest
{
    @InjectMocks
    private MailBean mailBean;

    @Mock
    private UserDao userDao;

    @Mock
    private MailDao maildao;

    @Mock
    private SessionDao sessionDao;

    @Mock
    private UserBean userBean;

    private UserEntity mockUser;
    private MailEntity mockMail;
    private SessionEntity mockSession;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new UserEntity();
        mockUser.setId(1);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        mockMail = new MailEntity();
        mockMail.setId(1);
        mockMail.setSubject("Test Subject");
        mockMail.setText("Test Text");
        mockMail.setDate(LocalDateTime.now());
        mockMail.setSender(mockUser);
        mockMail.setReceiver(mockUser);

        mockSession = new SessionEntity();
        mockSession.setSessionId("session123");
        mockSession.setUser(mockUser);
    }

    @Test
    public void testGetMailsReceivedBySessionId() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.getMailsReceivedByUserId(1, 0, 10, false)).thenReturn(List.of(mockMail));

        List<MailDto> mailDtos = mailBean.getMailsReceivedBySessionId("session123", 0, 10, false);

        assertEquals(1, mailDtos.size());
        assertEquals("Test Subject", mailDtos.get(0).getSubject());
        assertEquals("John Doe", mailDtos.get(0).getSenderName());
    }

    @Test
    public void testGetTotalMailsReceivedBySessionId() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.getTotalMailsReceivedByUserId(1, false)).thenReturn(5);

        int totalMails = mailBean.getTotalMailsReceivedBySessionId("session123", false);

        assertEquals(5, totalMails);
    }

    @Test
    public void testMarkMailAsSeen() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.findById(1)).thenReturn(mockMail);

        boolean result = mailBean.markMailAsSeen("session123", 1);

        assertTrue(result);
        verify(maildao).merge(mockMail);
        assertTrue(mockMail.isSeen());
    }
    @Test
    public void testMarkMailAsDeleted() {
        when(userBean.getUserBySessionId("session123")).thenReturn(mockUser);
        when(maildao.findById(1)).thenReturn(mockMail);

        boolean result = mailBean.markMailAsDeleted("session123", 1);

        assertTrue(result);
        verify(maildao).merge(mockMail);
        assertTrue(mockMail.isDeletedByReceiver());
    }

    @Test
    public void testGetUnreadNumber() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.getUnreadNumber(1)).thenReturn(3);

        int unreadCount = mailBean.getUnreadNumber("session123");

        assertEquals(3, unreadCount);
    }

    @Test
    public void testSearchMailsBySessionId() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.searchMails(1, "query", 0, 10, false)).thenReturn(List.of(mockMail));

        List<MailDto> mailDtos = mailBean.searchMailsBySessionId("session123", "query", 0, 10, false);

        assertEquals(1, mailDtos.size());
        assertEquals("Test Subject", mailDtos.get(0).getSubject());
    }

    @Test
    public void testGetTotalSearchResultsBySessionId() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.getTotalSearchResults(1, "query", false)).thenReturn(5);

        int totalResults = mailBean.getTotalSearchResultsBySessionId("session123", "query", false);

        assertEquals(5, totalResults);
    }

    @Test
    public void testSearchSentMailsBySessionId() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.searchSentMails(1, "query", 0, 10)).thenReturn(List.of(mockMail));

        List<MailDto> mailDtos = mailBean.searchSentMailsBySessionId("session123", "query", 0, 10);

        assertEquals(1, mailDtos.size());
        assertEquals("Test Subject", mailDtos.get(0).getSubject());
    }
    @Test
    public void testGetTotalSentSearchResultsBySessionId() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.getTotalSentSearchResults(1, "query")).thenReturn(5);

        int totalResults = mailBean.getTotalSentSearchResultsBySessionId("session123", "query");

        assertEquals(5, totalResults);
    }
    @Test
    public void testGetMailsSentBySessionId() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.getMailsSentByUserId(1, 0, 10)).thenReturn(List.of(mockMail));

        List<MailDto> mailDtos = mailBean.getMailsSentBySessionId("session123", 0, 10);

        assertEquals(1, mailDtos.size());
        assertEquals("Test Subject", mailDtos.get(0).getSubject());
    }

    @Test
    public void testGetTotalMailsSentBySessionId() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.getTotalMailsSentByUserId(1)).thenReturn(5);

        int totalMails = mailBean.getTotalMailsSentBySessionId("session123");

        assertEquals(5, totalMails);
    }

    @Test
    public void testMarkMailAsSeenFailure() {
        when(sessionDao.findBySessionId("session123")).thenReturn(mockSession);
        when(maildao.findById(1)).thenReturn(null);

        boolean result = mailBean.markMailAsSeen("session123", 1);

        assertFalse(result);
    }



    @Test
    public void testSendMailFailure() {
        when(userDao.findByEmail("invalidReceiver@example.com")).thenReturn(null);
        MailDto mailDto = new MailDto();
        mailDto.setReceiverMail("invalidReceiver@example.com");
        mailDto.setSubject("Test Subject");
        mailDto.setText("Test Text");
        boolean result = mailBean.sendMail(mockUser, mailDto);
        assertFalse(result);
    }


}