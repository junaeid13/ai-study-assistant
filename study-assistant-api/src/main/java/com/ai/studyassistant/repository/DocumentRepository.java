package com.ai.studyassistant.repository;

import com.ai.studyassistant.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

List<Document> findByUserUsername(String username);
Optional<Document> findByIdAndUsername(Long id, String username);
}