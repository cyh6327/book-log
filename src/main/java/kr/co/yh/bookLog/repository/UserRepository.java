package kr.co.yh.bookLog.repository;


import kr.co.yh.bookLog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
