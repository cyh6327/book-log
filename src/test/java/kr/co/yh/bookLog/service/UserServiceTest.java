package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.entity.UserStatus;
import kr.co.yh.bookLog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        User entity = User.builder().name("test").email("test@gmail.com").status(UserStatus.ACTIVE).build();
        user = userRepository.save(entity);
        userKey = user.getUserKey();
    }

    @Test
    void testInsert() {
        User user = User.builder().name("최연희").email("cyh6327@gmail.com").status(UserStatus.ACTIVE).build();
        userRepository.save(user);
    }

    @Test
    @Rollback
    void updateSentenceCutoffDateToNow() {
        LocalDateTime date = userService.updateSentenceCutoffDateToNow(user);

        Assertions.assertThat(date).isNotNull();
    }

    @Test
    void getActiveUsers() {
        List<User> users = userService.getActiveUsers();

        Assertions.assertThat(users).isNotNull();
    }

    @Test
    @Rollback
    void updateLastFetchedRow() {
        List<Long> key = List.of(userKey);
        Integer increment = 5;

        int result = userService.updateLastFetchedRow(key, increment);

        Assertions.assertThat(result).isEqualTo(1);

//        User updatedUser = userService.findUserById(userKey);
//        Integer updatedRow = updatedUser.getLastFetchedRow();
//
//        Assertions.assertThat(updatedRow).isEqualTo(lastFetchedRow+increment);
    }

    @Test
    void findUserById() {
        User user = userService.findUserById(userKey);

        Assertions.assertThat(user).isNotNull();
    }
}