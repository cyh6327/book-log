package kr.co.yh.bookLog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userKey;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private UserStatus status = UserStatus.ACTIVE;

    @Column(nullable = false)
    private Double sortKey;

    @Column(length = 500)
    private String note;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime joinDate;

    @Column
    private Integer lastFetchedRow = 0;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    // 데이터 선정 기준이 되는 날짜. 메일 전송 시 해당 날짜 이전에 생성된 book_sentence 레코드를 조회한다
    private LocalDateTime sentenceCutoffDate;

    @PrePersist // 엔티티가 데이터베이스에 저장되기 전에 호출
    public void prePersist() {
        this.sortKey = Math.random();
        this.joinDate = LocalDateTime.now();
        if(lastFetchedRow == null) {
            this.lastFetchedRow = 0;
        }
    }

    public void updateUserAfterSendMail(LocalDateTime sentenceCutoffDate, Integer lastFetchedRow) {
        this.sentenceCutoffDate = sentenceCutoffDate;
        this.lastFetchedRow = lastFetchedRow;
    }
}