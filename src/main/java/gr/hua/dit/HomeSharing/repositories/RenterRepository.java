package gr.hua.dit.HomeSharing.repositories;

import gr.hua.dit.HomeSharing.entities.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenterRepository extends JpaRepository<Renter, Integer> {
}
