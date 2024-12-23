package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.entity.Book;
import kr.co.yh.bookLog.entity.BookSentence;
import kr.co.yh.bookLog.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional
public class MarkdownProcessorTest {

    @Autowired
    MarkdownProcessor markdownProcessor;

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setup() {
        Book book = Book.builder().title("test_sentence").author("test_author").build();
        bookRepository.save(book);
    }

    @Test
    @Rollback
    void processFile() throws IOException {
        Resource[] resources = markdownProcessor.getFiles("sentence_md/test_sentence.md");
        Assertions.assertThat(resources).isNotNull();

        String[] fileInfo = markdownProcessor.readFiles(resources[0]);
        String title = fileInfo[0];
        String text = fileInfo[1];
        Assertions.assertThat(title).contains("test_sentence");
        Assertions.assertThat(text).contains("문장1");

        String[] processedTexts = markdownProcessor.processFiles(text);

        Assertions.assertThat(processedTexts[0]).contains("문장1").contains("문장2");
        Assertions.assertThat(processedTexts.length).isEqualTo(2);

        List<BookSentence> result = markdownProcessor.insertSentences(title, processedTexts);
        Assertions.assertThat(result).isNotNull();
    }

}
