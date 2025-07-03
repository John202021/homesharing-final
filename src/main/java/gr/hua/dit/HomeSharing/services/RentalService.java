package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.repositories.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    private RentalRepository rentalRepository;
    private EmailService emailService;

    public RentalService(RentalRepository rentalRepository, EmailService emailService) {
        this.rentalRepository = rentalRepository;
        this.emailService = emailService;
    }

    @Transactional
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    @Transactional
    public void saveRental(Rental rental) {
        rentalRepository.save(rental);
    }

    @Transactional
    public Rental getRental(int id) {
        return rentalRepository.findById(id).get();
    }

    @Transactional
    public void deleteRental(int id) {
        rentalRepository.deleteById(id);
    }

    @Transactional
    public Optional<LocalDate> checkRentalAvailability(int homeId, LocalDate newStartDate, LocalDate newEndDate) {
        List<Rental> rentals = rentalRepository.findByHomeIdAndAccepted(homeId, true); // Fetch only accepted rentals from DB

        for (Rental rental : rentals) {
            LocalDate existingStart = rental.getStartDate();
            LocalDate existingEnd = rental.getEndDate();

            if (!newStartDate.isAfter(existingEnd) && !newEndDate.isBefore(existingStart)) {
                return Optional.of(existingEnd);
            }
        }
        return Optional.empty();
    }

    @Transactional
    public void acceptRentalRequest(int id) {
        Rental rental = this.getRental(id);

        if (rental.getAccepted() != null) {
            throw new RuntimeException("Rental request already processed");
        }

        // Check if rental dates overlap for the same home
        Optional<LocalDate> overlappingDate = checkRentalAvailability(
                rental.getHome().getId(),
                rental.getStartDate(),
                rental.getEndDate()
        );
        if (overlappingDate.isPresent()) {
            throw new RuntimeException("Rental period overlaps with an existing rental ending on " + overlappingDate.get());
        }

        rental.setAccepted(true);
        rental.setRequestProcessedAt(LocalDateTime.now());
        rentalRepository.save(rental);
        // Send email notification to renter
        emailService.sendRentalRequestApprovedMail(
                rental.getRenter().getEmail(),
                rental.getRenter().getFirstName(),
                rental.getHome(),
                rental
        );
    }

    @Transactional
    public void rejectRentalRequest(int id) {
        Rental rental = this.getRental(id);
        if (rental.getAccepted() != null) {
            throw new RuntimeException("Rental request already processed");
        }
        rental.setAccepted(false);
        rental.setRequestProcessedAt(LocalDateTime.now());
        rentalRepository.save(rental);
        // Send email notification to renter
        emailService.sendRentalRequestRejectedMail(
                rental.getRenter().getEmail(),
                rental.getRenter().getFirstName(),
                rental.getHome(),
                rental
        );
    }

    @Transactional
    public List<Rental> getPendingRentals(int renterId) {
        return rentalRepository.findByAcceptedIsNullAndRenterId(renterId);
    }

    @Transactional
    public List<Rental> getAcceptedRentals(int renterId) {
        return rentalRepository.findByAcceptedAndRenterId(true, renterId);
    }

    @Transactional
    public List<Rental> getRejectedRentals(int renterId) {
        return rentalRepository.findByAcceptedAndRenterId(false, renterId);
    }

    @Transactional
    public List<Rental> getRentalsByRenter(int renterId) {
        return rentalRepository.findByRenterId(renterId);
    }

    @Transactional
    public List<Rental> getRentalsByHome(int homeId) {
        return rentalRepository.findByHomeId(homeId);
    }
    
}
