package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.entity.UserStatus;
import kr.co.yh.bookLog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public LocalDateTime updateSentenceCutoffDateToNow(User user) {
        if (user.getSentenceCutoffDate() == null) {
            user.setSentenceCutoffDateToNow();
            userRepository.save(user);
        }
        return user.getSentenceCutoffDate();
    }

    public List<User> getActiveUsers() {
        return userRepository.findByStatus(UserStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("ACTIVE User not found"));
    }

    public int updateLastFetchedRow(List<Long> users, Integer increment) {
        return userRepository.incrementLastFetchedRow(users, increment);
    }

    public User findUserById(Long userKey) {
        return userRepository.findById(userKey)
                .orElseThrow(() -> new RuntimeException("User not found with user_key: " + userKey));
    }
}
