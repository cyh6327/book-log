package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.entity.Book;
import kr.co.yh.bookLog.entity.BookSentence;
import kr.co.yh.bookLog.repository.BookSentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MarkdownProcessor extends FileProcessor {

    @Autowired
    BookSentenceRepository bookSentenceRepository;

    @Autowired
    BookService bookService;

    @Override
    protected Resource[] getFiles(String path) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources(path);
    }

    @Override
    protected String[] readFiles(Resource resource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return new String[] {resource.getFilename().split("\\.")[0], reader.lines().collect(Collectors.joining(System.lineSeparator())) };
        }
    }

    @Override
    protected String[] processFiles(String text) {
        /*
        마크다운 특수 기호 제거 : _, ~, `, #, +, -, |, \\, /
        예외사항
            1) --- : 문장 분할 기준
            2) * : 강조 문법인데 주로 인용문에 사용했기 때문에 메일 전송 시 이탤릭체로 변경 필요
        */
        String regex = "(?<!\\-\\-\\-)[\\_\\~\\`\\#\\+\\|\\\\\\/](?!\\-)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        String cleanedText = matcher.replaceAll("");

        return Arrays.stream(cleanedText.split("---"))
                .map(String::trim)
                .skip(1)    // 첫번째는 메타데이터 포함한 헤더이므로 제거
                .toArray(String[]::new);
    }

    @Override
    protected List<BookSentence> insertSentences(String title, String[] markdownTexts) {
        Book book = bookService.getBookByTitle(title);

        List<BookSentence> sentences = new ArrayList<>();
        LocalDateTime specificDateTime = LocalDateTime.of(2024, 12, 23, 15, 30);

        for(String text : markdownTexts) {
            BookSentence sentence = BookSentence.builder().book(book).text(text).favoriteFlag('N').createDate(specificDateTime).build();
            sentences.add(sentence);
        }

        return bookSentenceRepository.saveAll(sentences);
    }
}
