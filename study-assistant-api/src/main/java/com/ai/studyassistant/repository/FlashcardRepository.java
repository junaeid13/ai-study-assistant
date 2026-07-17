package com.ai.studyassistant.repository;

import com.ai.studyassistant.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByDocumentId(Long documentId);
}
