package kr.co.yh.bookLog.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import kr.co.yh.bookLog.entity.Book;
import kr.co.yh.bookLog.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

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
}
