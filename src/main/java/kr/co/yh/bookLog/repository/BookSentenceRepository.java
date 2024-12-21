package kr.co.yh.bookLog.repository;

import kr.co.yh.bookLog.entity.BookSentence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookSentenceRepository extends JpaRepository<BookSentence, Long> {
}
