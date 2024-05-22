package kg.saimatelecom.clientservice.repository;

import kg.saimatelecom.clientservice.model.RegistrationRequest;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRequestRepository extends CrudRepository<RegistrationRequest, Long> {
    @Query(value = "SELECT * FROM registration_requests r WHERE r.status='PENDING' ")
    List<RegistrationRequest> findAllPending();


    @Query(value = "SELECT EXISTS(SELECT r.id FROM registration_requests r WHERE r.email = :email)")
    boolean existsByEmail(@Param("email") String email);


    @Query(value = "SELECT * FROM registration_requests r WHERE r.status='PENDING' AND r.id = :id ")
    Optional<RegistrationRequest> findPendingById(@Param("id") Long requestId);
}
