package com.example.knowledge_base.service;
import com.example.knowledge_base.db.model.Document;
import com.example.knowledge_base.db.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private LlamaService llamaService;

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id).map(document -> {
            // Enhance document content using LLaMA3
            String enhancedContent = llamaService.enhanceDocumentContent(document.getContent());
            document.setContent(enhancedContent);
            return document;
        });
    }

    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    public Document updateDocument(Long id, Document updatedDocument) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setTitle(updatedDocument.getTitle());
                    document.setContent(updatedDocument.getContent());
                    return documentRepository.save(document);
                })
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}
