package kg.saimatelecom.catalog.service;

import kg.saimatelecom.catalog.model.Catalog;

import java.util.Optional;

public interface CatalogService {
    Optional<Catalog> getCatalogByOwnerEmail(String email);
    void save(Catalog catalog);
}
