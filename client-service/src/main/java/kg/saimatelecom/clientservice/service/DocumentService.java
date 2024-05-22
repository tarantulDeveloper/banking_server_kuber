package kg.saimatelecom.clientservice.service;

import kg.saimatelecom.clientservice.model.Document;

public interface DocumentService {
    Document findDocumentByUsername(String username);
    Document saveDocument(Document document);
}
