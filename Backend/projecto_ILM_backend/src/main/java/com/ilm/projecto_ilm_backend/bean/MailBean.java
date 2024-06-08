package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.dao.MailDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dto.mail.ContactDto;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.entity.MailEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MailBean {

    @Inject
    UserDao userDao;

    @Inject
    MailDao maildao;

    @Inject
    SessionDao sessionDao;

    public void createDefaultMailsIfNotExistent() {
        for (int i = 0; i < 30; i++) {
            if(maildao.findById(i) == null) {
                MailEntity mail = new MailEntity();
                mail.setSubject("Subject " + i);
                mail.setText("Text " + i);
                mail.setDeleted(false);
                mail.setDate(LocalDateTime.now());
                mail.setSeen(false);
                mail.setSender(userDao.findById(1));
                mail.setReceiver(userDao.findById(2));
                maildao.persist(mail);
            }
        }

        for (int i = 0; i < 30; i++) {
            if (maildao.findById(i + 30) == null) {
                MailEntity mail = new MailEntity();
                mail.setSubject("Subject " + i);
                mail.setText("Text " + i);
                mail.setDeleted(false);
                mail.setDate(LocalDateTime.now());
                mail.setSeen(false);
                mail.setSender(userDao.findById(2));
                mail.setReceiver(userDao.findById(1));
                maildao.persist(mail);
            }
        }
    }

    public List<MailDto> getMailsReceivedBySessionId(String sessionId) {
        List<MailDto> mails = new ArrayList<>();
        List<MailEntity> mailsReceived = maildao.getMailsReceivedByUserId(sessionDao.findBySessionId(sessionId).getUser().getId());
        for (MailEntity mail : mailsReceived) {
            MailDto mailDto = new MailDto();
            mailDto.setId(mail.getId());
            mailDto.setSubject(mail.getSubject());
            mailDto.setText(mail.getText());
            mailDto.setDate(mail.getDate());
            mailDto.setSenderName(mail.getSender().getFirstName() + " " + mail.getSender().getLastName());
            mailDto.setSenderMail(mail.getSender().getEmail());
            mailDto.setSenderPhoto(mail.getSender().getAvatarPhoto());
            mailDto.setReceiverName(mail.getReceiver().getFirstName() + " " + mail.getReceiver().getLastName());
            mailDto.setReceiverMail(mail.getReceiver().getEmail());
            mailDto.setReceiverPhoto(mail.getReceiver().getAvatarPhoto());
            mailDto.setSeen(mail.isSeen());
            mailDto.setDeleted(mail.isDeleted());
            mails.add(mailDto);
        }
        return mails;
    }

    public List<MailDto> getMailsSentBySessionId(String sessionId) {
        List<MailDto> mails = new ArrayList<>();
        List<MailEntity> mailsSent = maildao.getMailsSentByUserId(sessionDao.findBySessionId(sessionId).getUser().getId());
        for (MailEntity mail : mailsSent) {
            MailDto mailDto = new MailDto();
            mailDto.setId(mail.getId());
            mailDto.setSubject(mail.getSubject());
            mailDto.setText(mail.getText());
            mailDto.setDate(mail.getDate());
            mailDto.setSenderName(mail.getSender().getFirstName() + " " + mail.getSender().getLastName());
            mailDto.setSenderMail(mail.getSender().getEmail());
            mailDto.setSenderPhoto(mail.getSender().getAvatarPhoto());
            mailDto.setReceiverName(mail.getReceiver().getFirstName() + " " + mail.getReceiver().getLastName());
            mailDto.setReceiverMail(mail.getReceiver().getEmail());
            mailDto.setReceiverPhoto(mail.getReceiver().getAvatarPhoto());
            mailDto.setSeen(mail.isSeen());
            mailDto.setDeleted(mail.isDeleted());
            mails.add(mailDto);
        }
        return mails;
    }

    public boolean markMailAsSeen(String sessionId, int mailId) {
        UserEntity user = sessionDao.findBySessionId(sessionId).getUser();
        MailEntity mail = maildao.findById(mailId);

        if (mail != null && mail.getReceiver().getId() == user.getId()) {
            mail.setSeen(true);
            maildao.merge(mail);
            return true;
        } else {
            return false;
        }
    }

    public boolean markMailAsDeleted(String sessionId, int mailId) {
        UserEntity user = sessionDao.findBySessionId(sessionId).getUser();
        MailEntity mail = maildao.findById(mailId);

        if (mail != null && mail.getReceiver().getId() == user.getId()) {
            mail.setDeleted(true);
            maildao.merge(mail);
            return true;
        } else {
            return false;
        }
    }

    public boolean sendMail(String sessionId, MailDto mailDto) {
        UserEntity sender = sessionDao.findBySessionId(sessionId).getUser();
        UserEntity receiver = userDao.findByEmail(mailDto.getReceiverMail());

        if (sender != null && receiver != null) {
            MailEntity mail = new MailEntity();
            mail.setSubject(mailDto.getSubject());
            mail.setText(mailDto.getText());
            mail.setDate(LocalDateTime.now());
            mail.setSender(sender);
            mail.setReceiver(receiver);
            mail.setSeen(false);
            mail.setDeleted(false);

            maildao.persist(mail);
            return true;
        } else {
            return false;
        }
    }

    public List<ContactDto> getContactsBySessionId(String sessionId) {
        List<ContactDto> contacts = new ArrayList<>();
        UserEntity user = sessionDao.findBySessionId(sessionId).getUser();
        List<UserEntity> allUsers = userDao.findAll();
        for (UserEntity contact : allUsers) {
            if (!contact.equals(user)) {
                ContactDto contactDto = new ContactDto();
                contactDto.setName(contact.getFirstName() + " " + contact.getLastName());
                contactDto.setEmail(contact.getEmail());
                contacts.add(contactDto);
            }
        }
        return contacts;
    }

    public int getUnreadNumber(String sessionId) {
        UserEntity user = sessionDao.findBySessionId(sessionId).getUser();
        return maildao.getUnreadNumber(user.getId());
    }




}
