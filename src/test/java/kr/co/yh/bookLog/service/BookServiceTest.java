package kr.co.yh.bookLog.service;

import jakarta.transaction.Transactional;
import kr.co.yh.bookLog.entity.Book;
import kr.co.yh.bookLog.entity.BookSentence;
import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.entity.UserStatus;
import kr.co.yh.bookLog.repository.BookRepository;
import kr.co.yh.bookLog.repository.BookSentenceRepository;
import kr.co.yh.bookLog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookSentenceRepository bookSentenceRepository;

    private User user;
    private Long userKey;

    void setupUser() {
        LocalDateTime date = LocalDateTime.of(2024, 12, 15, 8, 0, 0);
        Double sortKey = 0.07853173718031747;
        Integer lastFetchedRow = 98;
        User entity = User.builder().name("test").email("test@gmail.com").status(UserStatus.ACTIVE).sentenceCutoffDate(date).sortKey(sortKey).lastFetchedRow(lastFetchedRow).build();
        user = userRepository.save(entity);
        userKey = user.getUserKey();
    }

    void setupBookSentences() {
        Book book = Book.builder().title("test_title").author("title_author").build();
        bookRepository.save(book);

        LocalDateTime baseDate  = LocalDateTime.of(2024, 12, 14, 8, 0, 0);
        List<BookSentence> sentences = new ArrayList<>();

        for(int i = 1; i <= 300; i++) {
            LocalDateTime createDate = baseDate.plusDays((i - 1) / 100);
            BookSentence bookSentence = BookSentence.builder().text("test_sentence"+i).book(book).createDate(createDate).favoriteFlag('N').build();
            sentences.add(bookSentence);
        }

        bookSentenceRepository.saveAll(sentences);
    }

    @Test
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
    void getBookSentences() {
        Long userKey = 8L;
        Map<String, Object> map = bookService.getBookSentences(userKey, 5);
        List<Object[]> results = (List<Object[]>) map.get("sentences");
        User user = (User) map.get("user");

        Assertions.assertThat(results).hasSize(5);
        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void getAdditionalSentences() {
        setupUser();
        setupBookSentences();

        Map<String, Object> map = bookService.getBookSentences(userKey, 5);
        List<Object[]> results = (List<Object[]>) map.get("sentences");
        User user = (User) map.get("user");

        Assertions.assertThat(results).hasSize(5);
        Assertions.assertThat(user).isNotNull();
    }
}
