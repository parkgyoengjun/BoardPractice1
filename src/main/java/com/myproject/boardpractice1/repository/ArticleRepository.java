package com.myproject.boardpractice1.repository;

import com.myproject.boardpractice1.domain.Article;
import com.myproject.boardpractice1.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // 이거 하나만 넣어도 검색기능은 끝남
        QuerydslBinderCustomizer<QArticle>  //
{
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
//        bindings.bind(root.title).first((path, value) -> path.eq(value));
//        bindings.bind(root.title).first(SimpleExpression::eq);
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); //(밑에꺼랑 같은건데) 쿼리 생성되는 문장이 다름 like '${v}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);// like '%${v}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}