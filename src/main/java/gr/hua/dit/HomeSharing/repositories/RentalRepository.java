package gr.hua.dit.HomeSharing.repositories;

import gr.hua.dit.HomeSharing.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByAcceptedIsNullAndRenterId(int renterId);
    List<Rental> findByAcceptedAndRenterId(Boolean accepted, int renterId);
    List<Rental> findByRenterId(int renterId);
    List<Rental> findByHomeIdAndAccepted(int homeId, Boolean accepted);
    List<Rental> findByHomeHomeOwnerIdAndAcceptedIsNull(int homeOwnerId);
    List<Rental> findByHomeHomeOwnerIdAndAcceptedAndEndDateAfter(int homeOwnerId, Boolean accepted, LocalDate endDate); // This is a sql query of:  SELECT r FROM Rental r WHERE r.home.homeOwnerId = :homeOwnerId AND r.accepted = :accepted AND r.endDate > :endDate
    List<Rental> findByHomeHomeOwnerIdAndAcceptedNotNullAndEndDateBefore(int homeOwnerId, LocalDate endDate); // This is a sql query of: SELECT r FROM Rental r WHERE r.home.homeOwnerId = :homeOwnerId AND r.accepted IS NOT NULL AND r.endDate < :endDate
    boolean existsByHomeHomeOwnerIdAndId(int homeOwnerId, int rentalId);
    List<Rental> findByHomeId(int homeId);
}
