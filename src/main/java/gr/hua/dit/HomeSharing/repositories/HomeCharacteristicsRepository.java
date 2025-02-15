package gr.hua.dit.HomeSharing.repositories;

import gr.hua.dit.HomeSharing.entities.HomeCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeCharacteristicsRepository extends JpaRepository<HomeCharacteristics, Integer> {
}
