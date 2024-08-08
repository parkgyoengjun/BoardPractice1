package com.myproject.boardpractice1.repository;

import com.myproject.boardpractice1.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}