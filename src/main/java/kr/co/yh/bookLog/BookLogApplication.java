package kr.co.yh.bookLog;

import kr.co.yh.bookLog.service.BookService;
import kr.co.yh.bookLog.service.FileProcessor;
import kr.co.yh.bookLog.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BookLogApplication implements CommandLineRunner {

	@Autowired
	private BookService bookService;

	@Autowired
	private MailService mailService;

	@Autowired
	@Qualifier("markdownProcessor")
	private FileProcessor fileProcessor;

	public static void main(String[] args) {
		SpringApplication.run(BookLogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//insertBooks("book_csv/book.csv");
		//insertSentences("sentence_md/*.md");
		sendMail();
	}

	void insertBooks(String path) throws IOException {
		bookService.insertBooksFromCSV(path);
	}

	void insertSentences(String path) throws IOException {
		fileProcessor.processAndInsert(path);
	}

	void sendMail() {
		mailService.sendEmailsToActiveUsers();
	}
}
