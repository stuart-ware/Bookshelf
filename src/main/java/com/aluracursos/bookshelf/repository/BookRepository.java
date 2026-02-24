package com.aluracursos.bookshelf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aluracursos.bookshelf.models.Books;

@Repository
public interface BookRepository extends JpaRepository<Books, Long>{
    @Query("SELECT COUNT(b) > 0 FROM Books b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Boolean verifiedBDExistence(@Param("title") String title);

    @Query(value = "SELECT * FROM book WHERE language = :language", nativeQuery = true)
    List<Books> findBooksByLanguage(@Param("language") String language);

    List<Books> findTop10ByOrderByDownloadsDesc();
}
