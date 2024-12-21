package kr.co.yh.bookLog.service;

import kr.co.yh.bookLog.entity.BookSentence;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public abstract class FileProcessor {
    public final void processAndInsert(String path) throws IOException {
        Resource[] files = getFiles(path);

        for(Resource file : files) {
            String fileText = readFiles(file);
            String[] processedTexts = processFiles(fileText);
            insertSentences(processedTexts);
        }

    }

    protected abstract Resource[] getFiles(String path) throws IOException;

    protected abstract String readFiles(Resource resource) throws IOException;

    protected abstract String[] processFiles(String text);

    protected abstract List<BookSentence> insertSentences(String[] markdownTexts);
}
