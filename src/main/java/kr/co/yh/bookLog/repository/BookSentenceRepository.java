package kr.co.yh.bookLog.repository;

import kr.co.yh.bookLog.entity.BookSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookSentenceRepository extends JpaRepository<BookSentence, Long> {

    @Query(value =
            "WITH sorted_rows AS (" +
                    "   SELECT " +
                    "       b.title, " +
                    "       b.author, " +
                    "       bs.text, " +
                    "       u.last_fetched_row, " +
                    "       md5(CAST(bs.sentence_key AS TEXT) || CAST(u.sort_key AS TEXT)) AS hash_val, " +
                    "       ROW_NUMBER() OVER (" +
                    "           ORDER BY " +
                    "               CASE" +
                    "                   WHEN bs.create_date::date between :fromDate and :toDate then 0 " +
                    "                   ELSE 1 " +
                    "               END," +
                    "               md5(CAST(bs.sentence_key AS TEXT) || CAST(u.sort_key AS TEXT))" +
                    "       ) AS row_num " +
                    "   FROM book_sentence bs " +
                    "   CROSS JOIN users u " +
                    "   INNER JOIN book b ON bs.book_key = b.book_key  " +
                    "   WHERE u.user_key = :userKey" +
                    "       AND bs.create_date::date between :fromDate and :toDate" +
                    ") " +
                    "SELECT * FROM sorted_rows " +
                    "WHERE row_num BETWEEN last_fetched_row + 1 AND last_fetched_row + :limit", nativeQuery = true)
    Optional<List<Object[]>> getSentences(@Param("userKey") Long userKey, @Param("limit") Integer limit, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("SELECT MIN(b.createDate) FROM BookSentence b")
    LocalDateTime findMinCreateDate();
}
