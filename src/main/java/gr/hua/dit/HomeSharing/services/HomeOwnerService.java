package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.Home;
import gr.hua.dit.HomeSharing.entities.HomeOwner;
import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.repositories.HomeOwnerRepository;
import gr.hua.dit.HomeSharing.repositories.HomeRepository;
import gr.hua.dit.HomeSharing.repositories.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomeOwnerService {
    private HomeOwnerRepository homeOwnerRepository;
    private RentalRepository rentalRepository;
    private HomeRepository homeRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public HomeOwnerService(HomeOwnerRepository homeOwnerRepository, RentalRepository rentalRepository, HomeRepository homeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.homeOwnerRepository = homeOwnerRepository;
        this.rentalRepository = rentalRepository;
        this.homeRepository = homeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<HomeOwner> getHomeOwners() {
        return homeOwnerRepository.findAll();
    }

    @Transactional
    public void updateHomeOwner(HomeOwner homeOwner) {
        HomeOwner existingHomeOwner = homeOwnerRepository.findById(homeOwner.getId())
                .orElseThrow(() -> new RuntimeException("HomeOwner with id " + homeOwner.getId() + " not found"));

        // Update individual fields
        existingHomeOwner.setFirstName(homeOwner.getFirstName());
        existingHomeOwner.setLastName(homeOwner.getLastName());
        existingHomeOwner.setEmail(homeOwner.getEmail());
        existingHomeOwner.setPhoneNumber(homeOwner.getPhoneNumber());
        existingHomeOwner.setBirthDate(homeOwner.getBirthDate());
        existingHomeOwner.setArea(homeOwner.getArea());
        existingHomeOwner.setAddress(homeOwner.getAddress());

        homeOwnerRepository.save(existingHomeOwner);
    }

    @Transactional
    public HomeOwner getHomeOwner(int id) {
        return homeOwnerRepository.findById(id).orElseThrow(() -> new RuntimeException("HomeOwner with id " + id + " not found"));
    }

    @Transactional
    public void deleteHomeOwner(int id) {
        // First, find the home owner
        HomeOwner homeOwner = homeOwnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HomeOwner not found with id: " + id));

         List<Home> homes = homeOwner.getHomes();

        // Delete all homes associated with the home owner
        for (Home home : homes) {
            // Find and delete all rentals for each home
            List<Rental> homeRentals = rentalRepository.findByHomeId(home.getId());
            rentalRepository.deleteAll(homeRentals);

            // Delete the home
            homeRepository.deleteById(home.getId());
        }

        // Now delete the home owner
        homeOwnerRepository.deleteById(id);
    }

    @Transactional
    public boolean hasHome(int homeOwnerId, int homeId) {
        return homeRepository.existsByHomeOwnerIdAndId(homeOwnerId, homeId);
    }

    @Transactional
    public boolean hasRentalRequest(int homeOwnerId, int rental_id){
        return rentalRepository.existsByHomeHomeOwnerIdAndId(homeOwnerId, rental_id);
    }

    @Transactional
    public List<Rental> getRentalRequests(int homeOwnerId){
        return rentalRepository.findByHomeHomeOwnerIdAndAcceptedIsNull(homeOwnerId);
    }

    @Transactional
    public List<Rental> getUpcomingRentals(int homeOwnerId) {
        return rentalRepository.findByHomeHomeOwnerIdAndAcceptedAndEndDateAfter(homeOwnerId, true, LocalDate.now());
    }

    @Transactional
    public List<Rental> getHistoricalRentals(int homeOwnerId) {
        return rentalRepository.findByHomeHomeOwnerIdAndAcceptedNotNullAndEndDateBefore(homeOwnerId, LocalDate.now());
    }

    @Transactional
    public Home addHomeForOwner(int homeOwnerId, Home home){
        if (!homeOwnerRepository.existsById(homeOwnerId)) {
            throw new RuntimeException("HomeOwner with id " + homeOwnerId + " not found");
        }
        HomeOwner owner = getHomeOwner(homeOwnerId); // Fetch only after validation
        home.setHomeOwner(owner);
        return homeRepository.save(home);
    }
}

