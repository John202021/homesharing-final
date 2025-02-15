package gr.hua.dit.HomeSharing.repositories;

import gr.hua.dit.HomeSharing.entities.HomeOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeOwnerRepository extends JpaRepository<HomeOwner, Integer> {
}
