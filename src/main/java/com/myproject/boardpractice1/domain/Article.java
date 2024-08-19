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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter // 롬복에서 제동하는 getXX() 를 자동으로 생성해줌
@ToString // 자동으로 toString() 생성해줌
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
}) // Entity 와 매핑할 테이블 지정
@EntityListeners(AuditingEntityListener.class) // 필드컬럼들?? 이 있는곳에 붙여줌으로써 Auditing 기능(변화가 있으면 변화를 적용시켜줌)을 활성화 시킴
@Entity // JPA 를 사용해 테이블과 매핑할 클래스에 붙여서 JAP가 관리하게함(1. 기본생성자 꼭 필요, 2. final, enum, interface, inner class 사용불가 3. 필드(변수) fianl 사용불가)
public class Article { // 게시판

    @Id // 특정 속성을 기본키로 설정(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본값을 자동으로 DB에 생성함. GenerationType.IDENTITY : 기본키 생성을 DB에 위임
    private Long id;

    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 내용
    // @Column :  객체 필드를 테이블 컬럼과 매핑, @Column(nullable : null 값 허용여부 설정(false 는 not null 제약조건))

    @Setter private String hashtag; // 해시태그

    @ToString.Exclude // 순환 참조 때문에 쓰임(한쪽 연결을 끊기 위해)
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();
    // 다대일 구조 원래라면 FK 를 가진 쪽인 ArticleComment 가 주인인데 게시판에서도 조회가 가능하도록 주인 설정은 article에 설정
    // mappedby = "aritlce" 이 주인 설정을 한거임 , cascade 는 영속성여부판단


    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy @Column(nullable = false,length = 100) private String createdBy; // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자
    // @CreateDate, @LastModifiedDate : JPA 에서 제공하는 애노테이션(직접 날짜를 설정하지 않아도 자동으로 저장해줌)
    // @CreateBy, @LastModifiedBy : 생성자와 수정자 변경시 자동으로 db에 저장된다.


    // 모든 jpa Entitiy 들은 hibernate 구현체를 사용하는 경우 기본 생성자를 가지고 있어야한다.
    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }



    // 위의 요소들의 중복을 제거하거나 정렬을 해야할때 비교를 할수있게(동등성 검사)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return id != null && id.equals(article.id); // id가 일치하는지와 부분
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
