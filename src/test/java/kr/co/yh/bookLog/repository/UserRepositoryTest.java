package kr.co.yh.bookLog.repository;

import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.entity.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testInsert() {
        User user = User.builder().name("최연희").email("cyh6327@gmail.com").status(UserStatus.ACTIVE).build();
        userRepository.save(user);
    }

}
