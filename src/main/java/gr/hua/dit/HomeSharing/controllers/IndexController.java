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
        User newRenter = userService.registerUser(
                new Renter("Nikos", "Io", "nikos@gmail.com", "12345678", "1234567890", birthDate)
        );

        User anotherRenter = userService.registerUser(
                new Renter("John", "Ts", "john@gmail.com", "87654321", "0987654321", birthDate)
        );

        User thirdRenter = userService.registerUser(
                new Renter("Alex", "Ar", "alex@gmail.com", "1211223344", "0987654323", birthDate)
        );

        User fourthRenter = userService.registerUser(
                new Renter("George", "Oik", "george@gmail.com", "11223344", "0987654325", birthDate)
        );

        // Creating HomeOwners
        User firstHomeOwner = userService.registerUser(
                new HomeOwner("Nikos", "In", "nikos_home@gmail.com", "22345678", "0987654326", birthDate, "123 Maple Street", "Downtown")
        );

        User secondHomeOwner = userService.registerUser(
                new HomeOwner("John", "Ts", "john_home@gmail.com", "98765432", "0987654327", birthDate, "100 Maple Street", "Downtown")
        );

        User thirdHomeOwner = userService.registerUser(
                new HomeOwner("Alex", "Ar", "alex_home@gmail.com", "2211223344", "0987654328", birthDate, "120 Maple Street", "Downtown")
        );

        User fourthHomeOwner = userService.registerUser(
                new HomeOwner("George", "Oik", "george_home@gmail.com", "21223344", "0987654321", birthDate, "112 Maple Street", "Downtown")
        );

        // Creating Homes
        Home firstHome = homeOwnerService.addHomeForOwner(
                firstHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("123 Maple Street", "Downtown", 3, 2, 120, 1, 4),
                        50
                )
        );
        Home secondHome = homeOwnerService.addHomeForOwner(
                firstHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("456 Oak Avenue", "Suburbs", 4, 3, 100, 2, 6),
                        30
                )
        );
        Home thirdHome = homeOwnerService.addHomeForOwner(
                firstHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("789 Pine Road", "Riverside", 2, 1, 80, 1, 3),
                        60
                )
        );
        Home fourthHome = homeOwnerService.addHomeForOwner(
                firstHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("101 Birch Lane", "Uptown", 5, 4, 50, 3, 8),
                        20
                )
        );
        Home fifthHome = homeOwnerService.addHomeForOwner(
                secondHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("202 Cedar Street", "Downtown", 3, 2, 140, 2, 5),
                        50
                )
        );
        Home sixthHome = homeOwnerService.addHomeForOwner(
                secondHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("303 Elm Boulevard", "Suburbs", 2, 1, 35, 1, 2),
                        15
                )
        );
        Home seventhHome = homeOwnerService.addHomeForOwner(
                thirdHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("404 Walnut Way", "Riverside", 2, 1, 90, 0, 3),
                        60
                )
        );
        Home eighthHome = homeOwnerService.addHomeForOwner(
                thirdHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("505 Chestnut Avenue", "Uptown", 4, 2, 65, 4, 5),
                        40
                )
        );
        Home ninthHome = homeOwnerService.addHomeForOwner(
                fourthHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("606 Spruce Drive", "Downtown", 4, 1, 75, 1, 5),
                        35
                )
        );
        Home tenthHome = homeOwnerService.addHomeForOwner(
                fourthHomeOwner.getId(),
                new Home(
                        new HomeCharacteristics("707 Willow Court", "Suburbs", 3, 2, 85, 1, 4),
                        55
                )
        );

        //Rental 1
        homeService.acceptHomeRequest(firstHome.getId());
        Rental rental1 = new Rental(LocalDate.of(2025,1,5),
                                    LocalDate.of(2025,1,6),
                                                     5,50);
        renterService.rentHome(newRenter.getId(), firstHome.getId(), rental1);

        //Rental 2
        homeService.acceptHomeRequest(secondHome.getId());
        Rental rental2 = new Rental(LocalDate.of(2025,1,7),
                                   LocalDate.of(2025,1,10),
                                                     3,90);
        renterService.rentHome(anotherRenter.getId(), secondHome.getId(), rental2);

        //Rental 3
        homeService.acceptHomeRequest(thirdHome.getId());
        Rental rental3 = new Rental(LocalDate.of(2025,1,2),
                                    LocalDate.of(2025,1,4),
                                                    5,180);
        renterService.rentHome(thirdRenter.getId(), thirdHome.getId(), rental3);

        //Rental 4
        homeService.acceptHomeRequest(fourthHome.getId());
        Rental rental4 = new Rental(LocalDate.of(2025,1,5),
                                    LocalDate.of(2025,1,6),
                                                     5,80);
        renterService.rentHome(fourthRenter.getId(), fourthHome.getId(), rental4);

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
