package kg.saimatelecom.catalog.repository;

import kg.saimatelecom.catalog.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog,Long> {
    Optional<Catalog> getCatalogByUsername(String dealer_email);
}
