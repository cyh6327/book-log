package kr.co.yh.bookLog.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import kr.co.yh.bookLog.entity.Book;
import kr.co.yh.bookLog.entity.BookSentence;
import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.entity.UserStatus;
import kr.co.yh.bookLog.repository.BookRepository;
import kr.co.yh.bookLog.repository.BookSentenceRepository;
import kr.co.yh.bookLog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class MailServiceTest {
    @Autowired
    public MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private Session mailSession;

    @Test
    void sendMail() {
        mailService.sendEmailsToActiveUsers();
    }

    @Test
    void sendSpecificSentence() {
        String testSentence =
                "This is a *test* string with <html> tags\n" +
                "and new lines.\n";

        List<MailContent> sentenceList = new ArrayList<>();
        MailContent s = new MailContent();
        s.setTitle("test title");
        s.setAuthor("test author");
        s.setText(testSentence);
        sentenceList.add(s);

        // Thymeleaf 템플릿 데이터 설정
        Context context = new Context();
        context.setVariable("sentences", sentenceList);

        // 템플릿 파일을 사용하여 이메일 본문 생성
        String body = templateEngine.process("emailTemplate", context);

        String to = "cyh6327@gmail.com";

        try {
            // 이메일 메시지 생성
            MimeMessage message = new MimeMessage(mailSession);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(to);
            message.setContent(body, "text/html;charset=UTF-8");

            // 이메일 전송
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
