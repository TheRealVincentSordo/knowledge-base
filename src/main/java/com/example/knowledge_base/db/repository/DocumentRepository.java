package com.example.knowledge_base.db.repository;

import com.example.knowledge_base.db.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
