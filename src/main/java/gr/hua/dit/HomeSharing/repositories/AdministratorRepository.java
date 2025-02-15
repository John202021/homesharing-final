package gr.hua.dit.HomeSharing.repositories;

import gr.hua.dit.HomeSharing.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
}
