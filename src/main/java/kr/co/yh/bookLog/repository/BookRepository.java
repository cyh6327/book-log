package kr.co.yh.bookLog.repository;

import kr.co.yh.bookLog.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
