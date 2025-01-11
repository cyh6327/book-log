package kr.co.yh.bookLog.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import kr.co.yh.bookLog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MailService {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Integer INCREMENT = 5;

    private final Session mailSession;

    @Autowired
    public MailService(@Qualifier("mailSession") Session mailSession) {
        this.mailSession = mailSession;
    }

    public void sendEmailsToActiveUsers() {
        List<User> users = userService.getActiveUsers();

        ConcurrentLinkedQueue<User> successUsers = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Long> failedUserKeys = new ConcurrentLinkedQueue<>();

        // 사용자 리스트를 병렬 스트림으로 처리하여 비동기적으로 이메일 전송
        users.parallelStream().forEach(user -> {
            try {
                User userToUpdate = sendEmail(user);
                successUsers.add(userToUpdate);
            } catch (Exception e) {
                failedUserKeys.add(user.getUserKey());
                e.printStackTrace();
            }
        });

        // 성공적으로 메일 전송한 사용자 업데이트
        List<User> usersToUpdate = new ArrayList<>(successUsers);
        if (!usersToUpdate.isEmpty()) {
            userService.updateUsers(usersToUpdate);
        }

        // TODO: 실패한 사용자 로그 파일 저장하도록
        for (Long key : failedUserKeys) {
            System.out.println(key);
        }
    }

    private User sendEmail(User user) {
        Map<String,Object> resultMap = bookService.getBookSentences(user.getUserKey(), INCREMENT);
        List<Object[]> sentences = (List<Object[]>) resultMap.get("sentences");
        User userToUpdate = (User) resultMap.get("user");

        // Thymeleaf 템플릿 데이터 설정
        Context context = new Context();
        context.setVariable("sentences", formatSentences(sentences));

        // 템플릿 파일을 사용하여 이메일 본문 생성
        String body = templateEngine.process("emailTemplate", context);

        String to = "cyh6327@gmail.com";

        // 이메일 전송
        send(to, "오늘의 문장이 도착했습니다.", body);

        return userToUpdate;
    }

    private List<MailContent> formatSentences(List<Object[]> sentences) {
        List<MailContent> sentenceList = new ArrayList<>();
        for (Object[] sentence : sentences) {
            MailContent s = new MailContent();
            s.setTitle((String) sentence[0]);
            s.setAuthor((String) sentence[1]);
            s.setText((String) sentence[2]);
            sentenceList.add(s);
        }
        return sentenceList;
    }

    private void send(String to, String subject, String body) {
        try {
            // 이메일 메시지 생성
            MimeMessage message = new MimeMessage(mailSession);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(body, "text/html;charset=UTF-8");

            // 이메일 전송
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

class MailContent {
    private String title;
    private String author;
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        // HTML 특수 문자 이스케이프
        String escapedText = StringEscapeUtils.escapeHtml4(text);

        // *문장*을 찾아서 <i>문장</i>으로 변경
        Pattern pattern = Pattern.compile("\\*(.*?)\\*");
        Matcher matcher = pattern.matcher(escapedText);

        this.text = matcher.replaceAll("<i>$1</i>").replaceAll("\n", "<br>");
    }
}