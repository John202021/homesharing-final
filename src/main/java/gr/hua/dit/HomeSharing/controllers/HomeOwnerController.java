package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.HomeOwner;
import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.entities.Renter;
import gr.hua.dit.HomeSharing.services.HomeOwnerService;
import gr.hua.dit.HomeSharing.services.RenterService;
import gr.hua.dit.HomeSharing.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/homeowner")
public class HomeOwnerController {
    HomeOwnerService homeOwnerService;

    public HomeOwnerController(HomeOwnerService homeOwnerService) {
        this.homeOwnerService = homeOwnerService;
    }

//    @GetMapping
//    public String getHomeOwners(Model model) {
//        List<HomeOwner> homeOwners = homeOwnerService.getHomeOwners();
//        System.out.println(homeOwners);
//        model.addAttribute("homeOwners", homeOwners);
//        return "index";
//    }

    @Secured("ROLE_HOMEOWNER")
    @GetMapping("me")
    public String getHomeOwner(Model model) {
        int ownerId = SecurityUtil.getLoggedInUserId();
        HomeOwner homeOwner = homeOwnerService.getHomeOwner(ownerId);
        model.addAttribute("homeOwner", homeOwner);
        return "home_owner/owner_profile";
    }



    @Secured("ROLE_ADMIN")
    @DeleteMapping("homeowners/{homeOwnerId}")
    public String deleteHomeOwner(@PathVariable int homeOwnerId) {
        homeOwnerService.deleteHomeOwner(homeOwnerId);
        System.out.println("Delete HomeOwner request");
        return "index";
    }

    @Secured("ROLE_HOMEOWNER")
    @GetMapping("rental-requests")
    public String getRentalRequests(Model model) {
        int ownerId = SecurityUtil.getLoggedInUserId();
        List<Rental> rentals = homeOwnerService.getRentalRequests(ownerId);
        model.addAttribute("title", "Your home's rental requests");
        model.addAttribute("rentals", rentals);
        model.addAttribute("viewingRentalRequests", true);
        return "rental/rentals";
    }

    @Secured("ROLE_HOMEOWNER")
    @GetMapping("upcoming-rentals")
    public String getUpcomingRentals(Model model) {
        int ownerId = SecurityUtil.getLoggedInUserId();
        List<Rental> rentals = homeOwnerService.getUpcomingRentals(ownerId);
        model.addAttribute("title", "Your home's upcoming rentals");
        model.addAttribute("rentals", rentals);
        model.addAttribute("viewingUpcomingRentals", true);
        return "rental/rentals";
    }

    @Secured("ROLE_HOMEOWNER")
    @GetMapping("rental-history")
    public String getRentalHistory(Model model) {
        int ownerId = SecurityUtil.getLoggedInUserId();
        List<Rental> rentals = homeOwnerService.getHistoricalRentals(ownerId);
        model.addAttribute("title", "Your home's rental history");
        model.addAttribute("rentals", rentals);
        model.addAttribute("viewingRentalHistory", true);
        return "rental/rentals";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("edit")
    public String editHomeOwner(@Valid @ModelAttribute("homeowner") HomeOwner updatedHomeOwner, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() && bindingResult.getErrorCount() > 1 || !bindingResult.hasFieldErrors("password")) {
            model.addAttribute("homeowner", updatedHomeOwner);
            return "home_owner/edit_owner_form";
        }
        System.out.println("Updated HomeOwner: " + updatedHomeOwner);

        homeOwnerService.updateHomeOwner(updatedHomeOwner);
        return "redirect:/users";
    }
}
