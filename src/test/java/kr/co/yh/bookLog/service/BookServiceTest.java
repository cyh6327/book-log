package kr.co.yh.bookLog.service;

import jakarta.transaction.Transactional;
import kr.co.yh.bookLog.entity.Book;
import kr.co.yh.bookLog.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Rollback
    public void testImportBooksFromCSV() throws IOException {
        List<Book> result = bookService.insertBooksFromCSV("book_csv/test_book.csv");
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void getBookByTitle() {
        Book book = Book.builder().title("test_title").author("test_author").build();
        Book savedBook = bookRepository.save(book);

        Book result = bookService.getBookByTitle(savedBook.getTitle());
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @Rollback
    void getBookSentences() {
        Long userKey = 8L;
        List<Object[]> results = bookService.getBookSentences(userKey, 5);

        Assertions.assertThat(results).hasSize(5);
    }
}
