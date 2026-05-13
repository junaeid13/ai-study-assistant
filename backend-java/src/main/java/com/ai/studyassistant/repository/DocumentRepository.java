package com.ai.studyassistant.repository;

import com.ai.studyassistant.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}