package kr.co.yh.bookLog.runner;

import kr.co.yh.bookLog.service.BookService;
import kr.co.yh.bookLog.service.FileProcessor;
import kr.co.yh.bookLog.service.MarkdownProcessor;

import java.io.IOException;

public class ApplicationRunner {

    public static void main(String[] args) throws IOException {
        insertBooks();
        //insertSentences();
    }

    static void insertBooks() throws IOException {
        BookService bookService = new BookService();
        bookService.insertBooksFromCSV("book_csv/book.csv");
    }

    static void insertSentences() throws IOException {
        FileProcessor processor = new MarkdownProcessor();
        processor.processAndInsert("sentence_md/*.md");
    }
}
