package kr.co.yh.bookLog.repository;


import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // UserStatus가 ACTIVE인 모든 사용자들을 선택하는 메서드
    Optional<List<User>> findByStatus(UserStatus status);

    /*
    nativeQuery = true : 네이티브 SQL 쿼리 사용
    nativeQuery = false : JPQL (Java Persistence Query Language) 쿼리 사용. 엔티티 객체를 대상으로 하는 객체 지향 쿼리 언어
     */
    @Transactional
    @Modifying
    //@Query(value = "UPDATE users u SET u.lastFetchedRow = u.lastFetchedRow + :increment WHERE u.userKey IN :userKeys", nativeQuery = true)
    @Query(value = "UPDATE User u SET u.lastFetchedRow = u.lastFetchedRow + :increment WHERE u.userKey IN :userKeys")
    int incrementLastFetchedRow(@Param("userKeys") List<Long> userKeys, @Param("increment") Integer increment);
}
