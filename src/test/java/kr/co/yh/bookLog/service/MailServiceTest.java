package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.entity.Book;
import kr.co.yh.bookLog.entity.BookSentence;
import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.entity.UserStatus;
import kr.co.yh.bookLog.repository.BookRepository;
import kr.co.yh.bookLog.repository.BookSentenceRepository;
import kr.co.yh.bookLog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class MailServiceTest {

    @Autowired
    MailService mailService;

    @Test
    void sendMail() {
        mailService.sendEmailsToActiveUsers();
    }
}
