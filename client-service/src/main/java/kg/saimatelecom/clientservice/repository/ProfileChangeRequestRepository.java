package kg.saimatelecom.clientservice.repository;

import kg.saimatelecom.clientservice.model.ProfileChangeRequest;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileChangeRequestRepository {
    @Query(value = "SELECT * FROM profile_changes_requests r WHERE r.status = 'PENDING' AND r.id = :id LIMIT 1")
    Optional<ProfileChangeRequest> findPendingById(@Param("id") Long id);

    @Query("SELECT r.* FROM profile_changes_requests r WHERE r.status = 'PENDING'")
    List<ProfileChangeRequest> getAllPending();

    ProfileChangeRequest save(ProfileChangeRequest profileChangeRequest);
}
