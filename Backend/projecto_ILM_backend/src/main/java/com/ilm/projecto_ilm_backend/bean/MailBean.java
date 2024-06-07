package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.dao.MailDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
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
        for (int i = 0; i < 10; i++) {
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

        for (int i = 0; i < 10; i++) {
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
            mailDto.setReceiverName(mail.getReceiver().getFirstName() + " " + mail.getReceiver().getLastName());
            mailDto.setReceiverMail(mail.getReceiver().getEmail());
            mailDto.setSeen(mail.isSeen());
            mailDto.setDeleted(mail.isDeleted());
            mails.add(mailDto);
        }
        return mails;
    }
}
