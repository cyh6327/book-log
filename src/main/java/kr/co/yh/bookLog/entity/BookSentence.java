package kr.co.yh.bookLog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sentenceKey;

    @Column(length = 5000, nullable = false)
    private String text;

    @Column
    private String tag;

    @Column(length = 1)
    @ColumnDefault("'N'")
    private char favoriteFlag = 'N';

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @PrePersist // 엔티티가 데이터베이스에 저장되기 전에 호출
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }
}