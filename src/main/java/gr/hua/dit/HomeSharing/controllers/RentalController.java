package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.services.HomeOwnerService;
import gr.hua.dit.HomeSharing.services.RentalService;
import gr.hua.dit.HomeSharing.util.SecurityUtil;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/rental")
public class RentalController {
    private RentalService rentalService;
    private HomeOwnerService homeOwnerService;

    public RentalController(RentalService rentalService, HomeOwnerService homeOwnerService) {
        this.rentalService = rentalService;
        this.homeOwnerService = homeOwnerService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("")
    public String getRentals(Model model) {
        List<Rental> rentals = rentalService.getRentals();
        model.addAttribute("rentals", rentals);
        model.addAttribute("title", "All Rentals");
        return "rental/rentals";
    }

    @Secured("ROLE_RENTER")
    @GetMapping("myrentals")
    public String getRentalsByStatus(Model model) {
        int renterId = SecurityUtil.getLoggedInUserId();
        List<Rental> rentals = rentalService.getAcceptedRentals(renterId);

        model.addAttribute("rentals", rentals);
        model.addAttribute("viewingMyRentals", true);
        model.addAttribute("title", "Your Rentals");
        return "rental/rentals";
    }

    @Secured("ROLE_RENTER")
    @GetMapping("myrentals/requests")
    public String getRentalRequests(Model model) {
        int renterId = SecurityUtil.getLoggedInUserId();
        List<Rental> rentals = rentalService.getPendingRentals(renterId);
        rentals.addAll(rentalService.getRejectedRentals(renterId));

        model.addAttribute("rentals", rentals);
        model.addAttribute("viewingMyRentalsRequests", true);
        model.addAttribute("title", "Your Rental Requests");
        return "rental/rentals";
    }

    @Secured("ROLE_HOMEOWNER")
    @PatchMapping("/accept/{rental_id}")
    public String acceptRentalRequest(@PathVariable int rental_id, RedirectAttributes redirectAttributes) {
        int homeOwnerId = SecurityUtil.getLoggedInUserId();
        if (!homeOwnerService.hasRentalRequest(homeOwnerId, rental_id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have a rental request with this id");
            return "redirect:/homeowner/rental-requests";
        }
        try {
            rentalService.acceptRentalRequest(rental_id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/homeowner/rental-requests";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Rental request accepted");
        return "redirect:/homeowner/rental-requests";
    }

    @Secured("ROLE_HOMEOWNER")
    @PatchMapping("/reject/{rental_id}")
    public String rejectRentalRequest(@PathVariable int rental_id, RedirectAttributes redirectAttributes) {
        int homeOwnerId = SecurityUtil.getLoggedInUserId();
        if (!homeOwnerService.hasRentalRequest(homeOwnerId, rental_id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have a rental request with this id");
            return "redirect:/homeowner/rental-requests";
        }

        try {
            rentalService.rejectRentalRequest(rental_id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while rejecting the rental request");
            return "redirect:/homeowner/rental-requests";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Rental request rejected");
        return "redirect:/homeowner/rental-requests";
    }


    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{rental_id}")
    public String deleteRental(@PathVariable int rental_id, RedirectAttributes redirectAttributes) {
        try {
            rentalService.deleteRental(rental_id);
            redirectAttributes.addFlashAttribute("successMessage", "Rental deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete rental. Error: " + e.getMessage());
        }
        return "redirect:/rental";
    }
}