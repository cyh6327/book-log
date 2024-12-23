package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.repository.BookSentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookSentenceService {
    @Autowired
    BookSentenceRepository bookSentenceRepository;

    public List<Object[]> getSentences(Long userKey, Integer limit, LocalDateTime fromDate, LocalDateTime toDate) {
        return bookSentenceRepository.getSentences(userKey, limit, fromDate, toDate)
                .orElseThrow(() -> new RuntimeException("Sentence not found with userKey: " + userKey));
    }

    public LocalDateTime findMinCreateDate() {
        return bookSentenceRepository.findMinCreateDate();
    }
}
