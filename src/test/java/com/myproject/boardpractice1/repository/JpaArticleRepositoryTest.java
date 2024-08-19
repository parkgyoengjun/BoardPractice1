package com.myproject.boardpractice1.repository;

import com.myproject.boardpractice1.config.JpaConfig;
import com.myproject.boardpractice1.domain.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) // JpaConfig 는 직접 만든거라서 직접 @Import 해줘야함
@DataJpaTest
class JpaArticleRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaArticleRepositoryTest(
            @Autowired ArticleRepository articleRepository,
                                    @Autowired ArticleCommentRepository articleCommentRepository)
    {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    } // 생성자 주입패턴

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // given

        // when
            List<Article> articles = articleRepository.findAll();
            // repository 에 ArticleRepository 는 JpaRepository 를 상속받고있고 JpaRepository 에 들어가보면
            // List<T> findAll() 로 List 를 써야함.

        // then
            Assertions.assertThat(articles)
                    .isNotNull()
                    .hasSize(123);
            // articles 는 not null 이고 사이즈가 123개이다.
    }
    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        /*
            insert 테스트 작성법 : 기존의 갯수를 샌 다음에 insert 한 후에 숫자가 늘었다면 ok
         */
        // given
            long preboiusCount = articleRepository.count();

        // when
            Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));
            // articleRepository.save(Article.of("new article", "new content", "#spring")); 이것만 설정해도 문제 없음

        // then
            Assertions.assertThat(articleRepository.count()).isEqualTo(preboiusCount + 1);
            // 저장되었는것이랑 이전 갯수가 같으면 된다.
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        /*
            update 테스트 작성법 : 기존의 데이트를 수정했을때 잘됐는지 확인하면된다.
                                 우선 영속성 컨텍스트로부너 하나 엔티티 개체를 가져옴
         */
        // given
            Article article = articleRepository.findById(1L).orElseThrow();
            // orElseThrow 를 통해 Optional 에서 원하는 객체를 바로 얻거나 예외를 던질 수 있습니다.

            String updatedHashtag = "#springboot";
            article.setHashtag(updatedHashtag);

        // when
            Article savedArticle = articleRepository.saveAndFlush(article);
            // saveAndFlush 로 하면 하이버네이트가 디비에 반영할거라 테스트 콘솔에 업데이트 쿼리가 생길거임
            // 다만 rollback 되기 때문에 디비에 반영이 되지는 않는다.

        // then
            Assertions.assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
            // hasFieldOrPropertyWithValue(String name, Object value) : 실제 객체에 주어진 값을 갖는 지정된 필드 또는 속성이 있는지 확인합니다.

    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        /*
            update 테스트 작성법 : 값하나를 꺼내서 지워야함
         */

        // 값 하나를 꺼내서 사이즈 체크 한 후 꺼낸 값을 삭제 시킨다. 그리고 삭제된 사이즈랑 비교함

        // given
            Article article = articleRepository.findById(1L).orElseThrow();
            long previousArticleCount = articleRepository.count();
            long previousArticleCommentCount = articleCommentRepository.count();
            int deletedCommentsSize = article.getArticleComments().size(); // 1L articleComment 에 대한 사이즈를 측정


        // when
            articleRepository.delete(article);
            // 어짜피 article 을 삭제하면 자동적으로 articleComment 도 삭제 되기때문에

        // then
            Assertions.assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
            Assertions.assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }


    @DisplayName("내가 삭제한거")
    @Test
    void given_when_then() {

        // given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCount = article.getArticleComments().size();

        // when
        articleRepository.delete(article);

        // then
        Assertions.assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        Assertions.assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCount);

    }
}