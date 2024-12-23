package kr.co.yh.bookLog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookKey;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column
    private String category;

    @Column
    private String tag;

    @Column
    private Integer rate;

    @Column
    private String note;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @PrePersist // 엔티티가 데이터베이스에 저장되기 전에 호출
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }
}
