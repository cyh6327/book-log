package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.entity.BookSentence;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MarkdownProcessorTest {

    @Autowired
    MarkdownProcessor markdownProcessor;

    @Test
    @Transactional
    void processFile() throws IOException {
        Resource[] resources = markdownProcessor.getFiles("sentence_md/*.md");
        String[] fileNames = Arrays.stream(resources).map(Resource::getFilename).toArray(String[]::new);
        Assertions.assertThat(fileNames).contains("test_sentence.md");

        String text = markdownProcessor.readFiles(resources[0]);
        Assertions.assertThat(text).contains("문장1");

        String[] processedTexts = markdownProcessor.processFiles(text);

        Assertions.assertThat(processedTexts[0]).contains("문장1").contains("문장2");
        Assertions.assertThat(processedTexts.length).isEqualTo(2);

        List<BookSentence> result = markdownProcessor.insertSentences(processedTexts);
        Assertions.assertThat(result).isNotNull();
    }

}
