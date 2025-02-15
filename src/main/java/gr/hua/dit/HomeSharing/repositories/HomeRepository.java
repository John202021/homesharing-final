package gr.hua.dit.HomeSharing.repositories;

import gr.hua.dit.HomeSharing.entities.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<Home, Integer> {
    List<Home> findByAccepted(Boolean accepted); // For accepted homes
    List<Home> findByAcceptedIsNull();          // For home requests (pending acceptance)

    boolean existsByHomeOwnerIdAndId(int homeOwnerId, int id);
}
