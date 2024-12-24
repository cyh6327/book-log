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
import java.util.*;

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

    public Map<String,Object> getBookSentences(Long userKey, Integer limit) {
        User user = userService.findUserById(userKey);

        LocalDateTime sentenceCutoffDate = user.getSentenceCutoffDate();

        LocalDateTime getMinCreateDate = getMinCreateDate();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime toDate = Objects.requireNonNullElse(sentenceCutoffDate, now);

        List<Object[]> sentences = bookSentenceService.getSentences(userKey, limit, getMinCreateDate, toDate.minusDays(1));

        LocalDateTime updateSentenceCutoffDate = now;
        Integer updateLastFetchedRow = user.getLastFetchedRow()+limit;

        if(sentences.size() == limit) { // 일반적인 상황
            if(sentenceCutoffDate != null) {
                updateSentenceCutoffDate = sentenceCutoffDate;  // 그대로 유지
            }
        } else if(sentences.size() < limit) {   // sentenceCutoffDate 이전의 데이터를 모두 한 번씩 조회한 상황 -> 조회 기준 변경
            Integer additionalCnt = limit-sentences.size();

            List<Object[]> additionalSentences = bookSentenceService.getSentences(userKey, additionalCnt, sentenceCutoffDate, now.minusDays(1));
            sentences.addAll(additionalSentences);

            updateLastFetchedRow = additionalCnt;

            if(sentences.size() != limit)
                throw new IllegalStateException("Sentences size is not the same as limit.");
        } else {
            throw new IllegalStateException("Sentences size exceeded the limit.");
        }

        userService.updateUserAfterSendMail(user, updateSentenceCutoffDate, updateLastFetchedRow);

        Map<String, Object> result = new HashMap<>();
        result.put("sentences", sentences);
        result.put("user", user);

        return result;
    }

    private LocalDateTime getMinCreateDate() {
        return bookSentenceService.findMinCreateDate();
    }
}
