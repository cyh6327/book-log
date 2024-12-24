package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.entity.UserStatus;
import kr.co.yh.bookLog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private User user;
    private Long userKey;

    void setUpUser() {
        LocalDateTime date = LocalDateTime.of(2024, 12, 15, 8, 0, 0);
        Double sortKey = 0.07853173718031747;
        User entity = User.builder().name("test").email("test@gmail.com").status(UserStatus.ACTIVE).sentenceCutoffDate(date).sortKey(sortKey).build();
        user = userRepository.save(entity);
        userKey = user.getUserKey();
    }

    @Test
    @Rollback(value = false)
    void testInsert() {
        User user = User.builder().name("최연희").email("cyh6327@gmail.com").status(UserStatus.ACTIVE).build();
        userRepository.save(user);
    }

    @Test
    void getActiveUsers() {
        List<User> users = userService.getActiveUsers();

        Assertions.assertThat(users).isNotNull();
    }

    @Test
    void updateLastFetchedRow() {
        setUpUser();

        List<Long> key = List.of(userKey);
        Integer increment = 5;

        int result = userService.updateLastFetchedRow(key, increment);

        Assertions.assertThat(result).isEqualTo(1);
    }

    @Test
    void findUserById() {
        setUpUser();

        User user = userService.findUserById(userKey);

        Assertions.assertThat(user).isNotNull();
    }
}