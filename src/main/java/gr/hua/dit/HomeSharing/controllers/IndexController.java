package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.*;
//import gr.hua.dit.HomeSharing.entities.UserDetails;
import gr.hua.dit.HomeSharing.services.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/")
public class IndexController {
    private final HomeService homeService;
    private UserService userService;
    private RenterService renterService;
    private HomeOwnerService homeOwnerService;
    private AdministratorService administratorService;

    public IndexController(UserService userService, RenterService renterService, HomeOwnerService homeOwnerService, AdministratorService administratorService, HomeService homeService) {
        this.userService = userService;
        this.renterService = renterService;
        this.homeOwnerService = homeOwnerService;
        this.administratorService = administratorService;
        this.homeService = homeService;
    }

    @PostConstruct
    public void setup() {
        System.out.println("IndexController initialized");

        LocalDate birthDate = LocalDate.of(1995, 1, 1);

        if (administratorService.getAdministrators().isEmpty()) {
            User admin = userService.registerUser(new Administrator("Admin", "Am", "admin@gmail.com", "admin12345", "6904598536", birthDate));
        }

        if (!renterService.getRenters().isEmpty()) {
            return;
        }

        // Creating Renters
        User newRenter = userService.registerUser(new Renter("Nikos", "Io", "nikos@gmail.com", "12345678", "6934567890", birthDate));
        User anotherRenter = userService.registerUser(new Renter("John", "Ts", "john@gmail.com", "87654321", "6987654321", birthDate));
        User thirdRenter = userService.registerUser(new Renter("Alex", "Ar", "alex@gmail.com", "1211223344", "6987654323", birthDate));
        User fourthRenter = userService.registerUser(new Renter("George", "Oik", "george@gmail.com", "11223344", "6987654325", birthDate));

        // Creating HomeOwners
        User firstHomeOwner = userService.registerUser(new HomeOwner("Nikos", "In", "nikos_home@gmail.com", "22345678", "6987654326", birthDate, "Athens", "Downtown"));
        User secondHomeOwner = userService.registerUser(new HomeOwner("John", "Ts", "john_home@gmail.com", "98765432", "6987654327", birthDate, "Thessaloniki", "Downtown"));
        User thirdHomeOwner = userService.registerUser(new HomeOwner("Alex", "Ar", "alex_home@gmail.com", "2211223344", "6987654328", birthDate, "Patra", "Downtown"));

        // Creating Homes for Nikos
        Home nikosHome1 = homeOwnerService.addHomeForOwner(firstHomeOwner.getId(), new Home(new HomeCharacteristics("12 Ermou Street", "Athens", 3, 2, 120, 1, 4), 50));
        Home nikosHome2 = homeOwnerService.addHomeForOwner(firstHomeOwner.getId(), new Home(new HomeCharacteristics("34 Kifisias Avenue", "Athens", 2, 1, 80, 1, 3), 40));
        Home nikosHome3 = homeOwnerService.addHomeForOwner(firstHomeOwner.getId(), new Home(new HomeCharacteristics("56 Panepistimiou Street", "Athens", 4, 3, 140, 2, 6), 60));
        Home nikosHome4 = homeOwnerService.addHomeForOwner(firstHomeOwner.getId(), new Home(new HomeCharacteristics("78 Stadiou Street", "Athens", 3, 2, 110, 1, 5), 55));

        // Creating Homes for John
        Home johnHome1 = homeOwnerService.addHomeForOwner(secondHomeOwner.getId(), new Home(new HomeCharacteristics("78 Aristotelous Square", "Thessaloniki", 3, 2, 110, 2, 5), 55));
        Home johnHome2 = homeOwnerService.addHomeForOwner(secondHomeOwner.getId(), new Home(new HomeCharacteristics("90 Tsimiski Street", "Thessaloniki", 2, 1, 70, 1, 2), 35));
        Home johnHome3 = homeOwnerService.addHomeForOwner(secondHomeOwner.getId(), new Home(new HomeCharacteristics("102 Egnatia Avenue", "Thessaloniki", 5, 4, 160, 3, 8), 75));
        Home johnHome4 = homeOwnerService.addHomeForOwner(secondHomeOwner.getId(), new Home(new HomeCharacteristics("114 Agiou Dimitriou Street", "Thessaloniki", 3, 2, 100, 1, 4), 50));

        // Creating Homes for Alex
        Home alexHome1 = homeOwnerService.addHomeForOwner(thirdHomeOwner.getId(), new Home(new HomeCharacteristics("114 Agiou Andreou Street", "Patra", 3, 2, 100, 1, 4), 50));
        Home alexHome2 = homeOwnerService.addHomeForOwner(thirdHomeOwner.getId(), new Home(new HomeCharacteristics("126 Korinthou Street", "Patra", 2, 1, 65, 0, 3), 30));
        Home alexHome3 = homeOwnerService.addHomeForOwner(thirdHomeOwner.getId(), new Home(new HomeCharacteristics("138 Germanou Street", "Patra", 4, 2, 140, 2, 6), 65));
        Home alexHome4 = homeOwnerService.addHomeForOwner(thirdHomeOwner.getId(), new Home(new HomeCharacteristics("150 Riga Feraiou Street", "Patra", 3, 2, 100, 1, 4), 50));

        // Accepting Home Requests
        homeService.acceptHomeRequest(nikosHome1.getId());
        homeService.acceptHomeRequest(nikosHome2.getId());
        homeService.acceptHomeRequest(nikosHome3.getId());
        homeService.acceptHomeRequest(johnHome1.getId());
        homeService.acceptHomeRequest(johnHome2.getId());
        homeService.acceptHomeRequest(johnHome3.getId());
        homeService.acceptHomeRequest(alexHome1.getId());
        homeService.acceptHomeRequest(alexHome2.getId());
        homeService.acceptHomeRequest(alexHome3.getId());
        homeService.acceptHomeRequest(nikosHome4.getId());
        homeService.acceptHomeRequest(johnHome4.getId());
        homeService.acceptHomeRequest(alexHome4.getId());

        // Creating Rentals
        Rental rental1 = new Rental(LocalDate.of(2025, 1, 5), LocalDate.of(2025, 1, 6), 5, 50);
        renterService.rentHome(newRenter.getId(), nikosHome1.getId(), rental1);

        Rental rental2 = new Rental(LocalDate.of(2025, 1, 7), LocalDate.of(2025, 1, 10), 3, 90);
        renterService.rentHome(anotherRenter.getId(), nikosHome2.getId(), rental2);

        Rental rental3 = new Rental(LocalDate.of(2025, 1, 2), LocalDate.of(2025, 1, 4), 5, 180);
        renterService.rentHome(thirdRenter.getId(), nikosHome3.getId(), rental3);

        Rental rental4 = new Rental(LocalDate.of(2025, 1, 5), LocalDate.of(2025, 1, 6), 5, 80);
        renterService.rentHome(fourthRenter.getId(), johnHome1.getId(), rental4);

        Rental rental5 = new Rental(LocalDate.of(2025, 1, 8), LocalDate.of(2025, 1, 12), 6, 120);
        renterService.rentHome(newRenter.getId(), johnHome2.getId(), rental5);

        Rental rental6 = new Rental(LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 5), 4, 100);
        renterService.rentHome(anotherRenter.getId(), johnHome3.getId(), rental6);

        Rental rental7 = new Rental(LocalDate.of(2025, 3, 3), LocalDate.of(2025, 3, 6), 3, 75);
        renterService.rentHome(thirdRenter.getId(), alexHome1.getId(), rental7);

        Rental rental8 = new Rental(LocalDate.of(2025, 4, 10), LocalDate.of(2025, 4, 15), 5, 85);
        renterService.rentHome(fourthRenter.getId(), alexHome2.getId(), rental8);

        Rental rental9 = new Rental(LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 4), 3, 95);
        renterService.rentHome(newRenter.getId(), alexHome3.getId(), rental9);

        Rental rental10 = new Rental(LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 4), 3, 95);
        renterService.rentHome(anotherRenter.getId(), nikosHome4.getId(), rental10);

        Rental rental11 = new Rental(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 4), 3, 95);
        renterService.rentHome(thirdRenter.getId(), johnHome4.getId(), rental11);

        Rental rental12 = new Rental(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 4), 3, 95);
        renterService.rentHome(fourthRenter.getId(), alexHome4.getId(), rental12);

        System.out.println("IndexController setup complete");
    }
    

    @GetMapping
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("{id}")
    public String getId(@PathVariable int id, Model model) {
        model.addAttribute("id", id);
        return "index";
    }

}
