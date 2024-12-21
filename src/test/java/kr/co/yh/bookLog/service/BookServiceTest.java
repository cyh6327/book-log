package kr.co.yh.bookLog.service;

import jakarta.transaction.Transactional;
import kr.co.yh.bookLog.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    public void testImportBooksFromCSV() throws IOException {
        List<Book> result = bookService.insertBooksFromCSV("book_csv/test_book.csv");
        Assertions.assertThat(result).isNotNull();
    }
}
