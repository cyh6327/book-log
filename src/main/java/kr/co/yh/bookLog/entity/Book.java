package kr.co.yh.bookLog.entity;

import jakarta.persistence.*;
import lombok.*;

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

}
