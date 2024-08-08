package com.myproject.boardpractice1.repository;

import com.myproject.boardpractice1.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
