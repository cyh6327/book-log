package kr.co.yh.bookLog.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import kr.co.yh.bookLog.entity.Book;
import kr.co.yh.bookLog.entity.User;
import kr.co.yh.bookLog.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookSentenceService bookSentenceService;

    @Autowired
    private UserService userService;

    @Transactional
    public List<Book> insertBooksFromCSV(String path) throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(path);

            Reader reader = new InputStreamReader(resource.getInputStream());

            CSVReader csvReader = new CSVReader(reader);

            String[] header = csvReader.readNext(); // 헤더는 처리하지 않음

            List<Book> books = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String title = line[0];
                String author = line[1];
                String category = line[2];

                Book book = Book.builder().title(title).author(author).category(category).build();
                books.add(book);
            }

            csvReader.close(); // 리소스를 닫음
            return bookRepository.saveAll(books);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public Book getBookByTitle(String title) {
        return bookRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Book not found with title: " + title));
    }

    public List<Object[]> getBookSentences(Long userKey, Integer limit) {
        User user = userService.findUserById(userKey);

        LocalDateTime sentenceCutoffDate = user.getSentenceCutoffDate();
        // 최초 서비스 요청인 경우
        if (sentenceCutoffDate == null) {
            sentenceCutoffDate = userService.updateSentenceCutoffDateToNow(user);
        }

        LocalDateTime getMinCreateDate = getMinCreateDate();
        List<Object[]> sentences = bookSentenceService.getSentences(userKey, limit, getMinCreateDate, sentenceCutoffDate.minusDays(1));
        if (sentences == null) {
            throw new RuntimeException("No sentences found for user with user_key: " + userKey);
        }

        if(sentences.size() < limit) {
            LocalDateTime now = LocalDateTime.now();
            Integer additionalCnt = limit-sentences.size();

            List<Object[]> additionalSentences = bookSentenceService.getSentences(userKey, additionalCnt, sentenceCutoffDate, now.minusDays(1));
            sentences.addAll(additionalSentences);

            List<Long> key = List.of(userKey);
            userService.updateSentenceCutoffDateToNow(user);
            userService.updateLastFetchedRow(key, additionalCnt);
        }

        return sentences;
    }

    private LocalDateTime getMinCreateDate() {
        return bookSentenceService.findMinCreateDate();
    }
}
