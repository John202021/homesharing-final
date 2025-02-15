package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.HomeOwner;
import gr.hua.dit.HomeSharing.entities.Renter;
import gr.hua.dit.HomeSharing.repositories.HomeOwnerRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import gr.hua.dit.HomeSharing.entities.Home;
import gr.hua.dit.HomeSharing.repositories.HomeRepository;
import jakarta.transaction.Transactional;
import gr.hua.dit.HomeSharing.repositories.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;
import gr.hua.dit.HomeSharing.entities.Rental;
import java.util.stream.Collectors;
import gr.hua.dit.HomeSharing.entities.HomeCharacteristics;

@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final HomeOwnerRepository homeOwnerRepository;
    private final RentalRepository rentalRepository;
    public HomeService(HomeRepository homeRepository, HomeOwnerRepository homeOwnerRepository, RentalRepository rentalRepository) {
        this.homeRepository = homeRepository;
        this.homeOwnerRepository = homeOwnerRepository;
        this.rentalRepository = rentalRepository;
    }


    @Transactional
    public void saveHome(Home home) {
        homeRepository.save(home);
    }


    @Transactional
    public void deleteHome(int id) {
        // First, check if the home exists
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found with id: " + id));

        // Find all rentals associated with this home
        List<Rental> homeRentals = rentalRepository.findByHomeId(id);

        // Delete all rentals associated with the home
        rentalRepository.deleteAll(homeRentals);

        // Now delete the home
        homeRepository.deleteById(id);
    }

    @Transactional
    public int updateHome(Home home) {
        // Retrieve the existing Home by its ID.
        Home existingHome = homeRepository.findById(home.getId())
                .orElseThrow(() -> new RuntimeException("Home not found with id: " + home.getId()));

        // Update basic fields.
        existingHome.setDailyPrice(home.getDailyPrice());

        // Update the HomeCharacteristics if provided.
        if (home.getCharacteristics() != null) {
            HomeCharacteristics newCharacteristics = home.getCharacteristics();
            HomeCharacteristics existingCharacteristics = existingHome.getCharacteristics();

            if (existingCharacteristics != null) {
                existingCharacteristics.setAddress(newCharacteristics.getAddress());
                existingCharacteristics.setArea(newCharacteristics.getArea());
                existingCharacteristics.setRooms(newCharacteristics.getRooms());
                existingCharacteristics.setBathrooms(newCharacteristics.getBathrooms());
                existingCharacteristics.setSquareMeters(newCharacteristics.getSquareMeters());
                existingCharacteristics.setFloor(newCharacteristics.getFloor());
                existingCharacteristics.setHumanCapacity(newCharacteristics.getHumanCapacity());
            } else {
                // In case the existing home doesn't have characteristics (unlikely if it's non-nullable)
                existingHome.setCharacteristics(newCharacteristics);
            }
        }
        // Note: Rentals are typically managed separately so they are not updated here.
        // Save the updated Home entity.
        homeRepository.save(existingHome);
        return existingHome.getId();
    }


    @Transactional
    public int addHome(Home home, int ownerId) {
        HomeOwner owner = homeOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("HomeOwner not found with id: " + ownerId));
        home.setHomeOwner(owner);
        homeRepository.save(home);
        return home.getId();
    }


    @Transactional
    public void acceptHomeRequest(int homeId){
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found with id: " + homeId));
        if (home.getAccepted() != null) {
            throw new RuntimeException("Home request already processed");
        }
        home.setAccepted(true);
        home.setRequestProcessedAt(LocalDateTime.now());
        homeRepository.save(home);
    }

    @Transactional
    public void declineHomeRequest(int homeId){
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found with id: " + homeId));
        if (home.getAccepted() != null) {
            throw new RuntimeException("Home request already processed");
        }
        home.setAccepted(false);
        home.setRequestProcessedAt(LocalDateTime.now());
        homeRepository.save(home);
    }

    @Transactional
    public List<Home> getAcceptedHomes(){
        return homeRepository.findByAccepted(true);
    }

    public List<Home> getHomeRequests() {
        return homeRepository.findByAcceptedIsNull();
    }

    @Transactional
    public List<Home> getHomeByOwnerId(int ownerId) {
        HomeOwner owner = homeOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("HomeOwner not found with id: " + ownerId));
        return owner.getHomes();
    }

    @Transactional
    public Home getHome(int id) {
        return homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found with id: " + id));
    }

    @Transactional
    public List<Home> getHomes() {
        return homeRepository.findAll();
    }

    @Transactional
    public List<Home> getDeclinedHomes(){
        return homeRepository.findByAccepted(false);
    }
    @Transactional
    public List<Home> getPendingHomes(){
        return homeRepository.findByAcceptedIsNull();
    }


    @Transactional
    public List<Home> filterHomes(
            Integer minPrice,
            Integer maxPrice,
            Integer minSquareMeters,
            Integer maxSquareMeters,
            Integer minHumanCapacity) {

        List<Home> homes = homeRepository.findByAccepted(true);

        return homes.stream()
                .filter(home -> minPrice == null || home.getDailyPrice() >= minPrice)
                .filter(home -> maxPrice == null || home.getDailyPrice() <= maxPrice)
                .filter(home -> minSquareMeters == null || home.getCharacteristics().getSquareMeters() >= minSquareMeters)
                .filter(home -> maxSquareMeters == null || home.getCharacteristics().getSquareMeters() <= maxSquareMeters)
                .filter(home -> minHumanCapacity == null || home.getCharacteristics().getHumanCapacity() >= minHumanCapacity)
                .collect(Collectors.toList());
    }

}
