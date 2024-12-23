package kr.co.yh.bookLog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailServiceTest {

    @Autowired
    MailService mailService;

    @Test
    void sendMail() {
        mailService.sendEmailsToActiveUsers();
    }
}
