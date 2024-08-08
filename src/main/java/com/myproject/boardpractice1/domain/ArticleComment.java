package com.myproject.boardpractice1.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter // 롬복에서 제동하는 getXX() 를 자동으로 생성해줌
@ToString // 자동으로 toString() 생성해줌
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
}) // Entity 와 매핑할 테이블 지정
@EntityListeners(AuditingEntityListener.class)
@Entity
// JPA 를 사용해 테이블과 매핑할 클래스에 붙여서 JAP가 관리하게함(1. 기본생성자 꼭 필요, 2. final, enum, interface, inner class 사용불가 3. 필드(변수) fianl 사용불가)
public class ArticleComment { // 게시글

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article;
    // @ManyToOne 다대일구조이고 FK 를 가지고 있어 원래는 주인이여야하지만 주인권을 Article 에서 mappedBy 로 가져감 optional 은 null 의 여부 판단
    @Setter @Column(nullable = false, length = 500) private String content;

    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy @Column(nullable = false,length = 100) private String createdBy; // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자

    protected ArticleComment() {}

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment)) return false;
        ArticleComment that = (ArticleComment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
