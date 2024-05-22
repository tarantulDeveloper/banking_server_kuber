package kg.saimatelecom.clientservice.service.impl;

import kg.saimatelecom.clientservice.exceptions.ResourceNotFoundException;
import kg.saimatelecom.clientservice.model.Document;
import kg.saimatelecom.clientservice.repository.DocumentRepository;
import kg.saimatelecom.clientservice.service.DocumentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentServiceImpl implements DocumentService {
    DocumentRepository documentRepository;

    @Override
    public Document findDocumentByUsername(String username) {
        return documentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    @Override
    public Document saveDocument(Document document) {
        return (Document) documentRepository.save(document);
    }
}
