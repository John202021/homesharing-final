package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.*;
import gr.hua.dit.HomeSharing.services.HomeOwnerService;
import gr.hua.dit.HomeSharing.services.HomeService;
import gr.hua.dit.HomeSharing.services.RenterService;
import gr.hua.dit.HomeSharing.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import gr.hua.dit.HomeSharing.services.RentalService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;  // Don't forget to import Comparator



import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;
    private final RenterService renterService;
    private final RentalService rentalService;
    private final HomeOwnerService homeOwnerService;

    public HomeController(HomeService homeService, RenterService renterService, RentalService rentalService, HomeOwnerService homeOwnerService) {
        this.homeService = homeService;
        this.renterService = renterService;
        this.rentalService = rentalService;
        this.homeOwnerService = homeOwnerService;
    }


    @GetMapping("")
    public String getHomes(
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minSquareMeters,
            @RequestParam(required = false) Integer maxSquareMeters,
            @RequestParam(required = false) Integer minHumanCapacity,
            @RequestParam(required = false) String sortOrder,  // New parameter for sorting
            Model model) {
        model.addAttribute("title", "Available Homes for Rent");
        try {
            // Get the filtered list from your service.
            List<Home> homes = homeService.filterHomes(minPrice, maxPrice, minSquareMeters, maxSquareMeters, minHumanCapacity);

            // If a sort order is specified, sort the list accordingly.
            if (sortOrder != null) {
                if ("asc".equals(sortOrder)) {
                    homes.sort(Comparator.comparingInt(Home::getDailyPrice));
                } else if ("desc".equals(sortOrder)) {
                    homes.sort(Comparator.comparingInt(Home::getDailyPrice).reversed());
                }
            }

            // Pass the filter values back to the view.
            model.addAttribute("homes", homes);
            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);
            model.addAttribute("minSquareMeters", minSquareMeters);
            model.addAttribute("maxSquareMeters", maxSquareMeters);
            model.addAttribute("minHumanCapacity", minHumanCapacity);
            model.addAttribute("sortOrder", sortOrder);

            return "home/homes_filters";
        } catch (Exception e) {
            System.err.println("Error fetching homes: " + e.getMessage());
            model.addAttribute("errorMessage", "Unable to fetch homes.");
            return "home/homes_filters";
        }
    }


    //home details
    @GetMapping("/{id}")
    public String getHome(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Home home = homeService.getHome(id);
            Rental rentalRequest = new Rental();
            model.addAttribute("rentalRequest", rentalRequest); // Add rental request form to the model, because you can rent a home from this page
            model.addAttribute("home", home);
            return "home/home_details";
        } catch (Exception e) {
            System.err.println("Error fetching home by ID: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to fetch home details.");
            return "redirect:/home";
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_HOMEOWNER"})
    @GetMapping("/pending")
    public String getPendingHomes(Model model) {
        model.addAttribute("viewingHomeRequests", true);
        try {
            List<Home> homes = homeService.getPendingHomes();
            model.addAttribute("title", "Pending Home Requests");
            model.addAttribute("homes", homes);
            return "home/homes";
        } catch (Exception e) {
            System.err.println("Error fetching homes: " + e.getMessage());
            model.addAttribute("errorMessage", "Unable to fetch homes.");
            return "home/homes";
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_HOMEOWNER"})
    @GetMapping("/declined")
    public String getDeclinedHomes(Model model) {
        try {
            List<Home> homes = homeService.getDeclinedHomes();
            model.addAttribute("title", "Declined Home Requests");
            model.addAttribute("homes", homes);
            return "home/homes";
        } catch (Exception e) {
            System.err.println("Error fetching homes: " + e.getMessage());
            model.addAttribute("errorMessage", "Unable to fetch homes.");
            return "home/homes";
        }
    }

    @Secured("ROLE_HOMEOWNER")
    @GetMapping("/new")
    public String showAddHomeForm(Model model) {
        model.addAttribute("home", new Home());
        model.addAttribute("formTitle", "Add New Home");
        model.addAttribute("actionUrl", "/home"); // POST to /home for new homes
        return "home/home_form";
    }


    @Secured("ROLE_HOMEOWNER")
    @PostMapping
    public String addHome(@Valid @ModelAttribute Home home, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        int ownerId = SecurityUtil.getLoggedInUserId();
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Invalid input.");
            return "home/home_form";
        }
        try {
            homeOwnerService.addHomeForOwner(ownerId, home);
            redirectAttributes.addFlashAttribute("successMessage", "Home request sent successfully.");
            return "redirect:/home";
        } catch (Exception e) {
            System.err.println("Error adding home: " + e.getMessage());
            model.addAttribute("errorMessage", "Unable to add home.");
            return "home/home_form";
        }
    }

    @Secured("ROLE_HOMEOWNER")
    @DeleteMapping("/{id}")
    public String deleteHome(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        int homeOwnerId = SecurityUtil.getLoggedInUserId();
        boolean hasHome = homeOwnerService.hasHome(homeOwnerId, id);
        // Security check: Ensure the homeowner owns the home
        if (!hasHome) {
            redirectAttributes.addFlashAttribute("errorMessage", "You don't own this home to delete it.");
            return "redirect:/home/ownedhomes";
        }

        try {
            homeService.deleteHome(id);
            String message = "Deleted home with ID: " + id;
            System.out.println(message);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/home/ownedhomes";
        } catch (Exception e) {
            System.err.println("Error deleting home: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete this home.");
            return "redirect:/home/ownedhomes";
        }
    }

    @Secured("ROLE_HOMEOWNER")
    @GetMapping("/ownedhomes")
    public String getHomeByOwnerId(Model model) {
        int ownerId = SecurityUtil.getLoggedInUserId();
        try {
            List<Home> homes = homeService.getHomeByOwnerId(ownerId);
            model.addAttribute("title", "My Homes");
            model.addAttribute("homes", homes);
            return "home/homes";
        } catch (Exception e) {
            System.err.println("Error fetching homes by owner ID: " + e.getMessage());
            model.addAttribute("errorMessage", "Unable to fetch homes for the owner.");
            return "index";
        }
    }

    //accept or decline home requests
    @Secured("ROLE_ADMIN")
    @PatchMapping("/accept/{home_id}")
    public String acceptHomeRequest(@PathVariable int home_id, RedirectAttributes redirectAttributes, Model model) {
        try {
            homeService.acceptHomeRequest(home_id);
            String message = "Accepted home request with ID: " + home_id;
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/home/pending";
        } catch (Exception e) {
            System.err.println("Error accepting home request: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to accept home request.");
            return "redirect:/home/pending";
        }
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/decline/{home_id}")
    public String declineHomeRequest(@PathVariable int home_id, RedirectAttributes redirectAttributes, Model model) {
        try {
            homeService.declineHomeRequest(home_id);
            String message = "Declined home request with ID: " + home_id;
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/home/pending";
        } catch (Exception e) {
            System.err.println("Error declining home request: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to decline home request.");
            return "redirect:/home/pending";
        }
    }

    @Secured("ROLE_RENTER")
    @PostMapping("/{home_id}/rent")
    public String rentHome(@PathVariable int home_id,
                           @ModelAttribute("rentalRequest") Rental rentalRequest,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        // Fetch home details to check human capacity
        Home home = homeService.getHome(home_id);

        // Custom validation: Check if start date is in the future
        if (rentalRequest.getStartDate() != null && rentalRequest.getStartDate().isBefore(LocalDate.now())) {
            bindingResult.rejectValue("startDate", "error.startDate", "Start date can't be in the past.");
        }

        // Custom validation: Check if end date is provided and is after the start date
        if (rentalRequest.getStartDate() != null && rentalRequest.getEndDate() != null) {
            if (!rentalRequest.getEndDate().isAfter(rentalRequest.getStartDate())) {
                bindingResult.rejectValue("endDate", "error.endDate", "End date must be after the start date.");
            }
        }

        // Custom validation: Check if numberOfPeople exceeds home capacity
        if (rentalRequest.getNumberOfPeople() > home.getCharacteristics().getHumanCapacity()) {
            bindingResult.rejectValue("numberOfPeople", "error.numberOfPeople",
                    "Number of people cannot exceed the home capacity (" + home.getCharacteristics().getHumanCapacity() + ")");
        }

        // Check rental availability if both dates are provided
        if (rentalRequest.getStartDate() != null && rentalRequest.getEndDate() != null) {
            if (rentalService.checkRentalAvailability(home_id, rentalRequest.getStartDate(), rentalRequest.getEndDate()).isPresent()) {
                bindingResult.rejectValue("startDate", "error.startDate", "Home is not available for the selected dates.");
            }
        }

        // If validation errors exist, return to the form with errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("home", home); // Ensure home data is available in the view
            model.addAttribute("showForm", true); // Show the form in the home_details by default
            return "home/home_details";
        }

        // Calculate the number of days between the start and end dates
        long days = ChronoUnit.DAYS.between(rentalRequest.getStartDate(), rentalRequest.getEndDate()) + 1;
        int totalPrice = (int)(home.getDailyPrice() * days);

        // Create a new Rental with the provided dates and computed price
        Rental rental = new Rental(rentalRequest.getStartDate(), rentalRequest.getEndDate(), rentalRequest.getNumberOfPeople(), totalPrice);

        try {
            int renterId = SecurityUtil.getLoggedInUserId();
            renterService.rentHome(renterId, home_id, rental);
            String message = "Successfully sent a rental application, wait for approval.";
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/home";
        } catch (Exception e) {
            System.err.println("Error renting home: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to rent home. Try again later.");
            return "redirect:/home";
        }
    }

    @Secured("ROLE_HOMEOWNER")
    @GetMapping("/{home_id}/rentals")
    public String getRentalsByHome(@PathVariable int home_id, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<Rental> rentals = rentalService.getRentalsByHome(home_id);
            model.addAttribute("title", "Home Rentals");
            model.addAttribute("rentals", rentals);
            return "rental/rentals";
        } catch (Exception e) {
            System.err.println("Error fetching rentals by home ID: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to fetch rentals for the home.");
            return "redirect:/home/ownedhomes";
        }
    }

    @Secured("ROLE_HOMEOWNER")
    @GetMapping("/edit/{id}")
    public String showEditHomeForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        int ownerId = SecurityUtil.getLoggedInUserId();
        if (!homeOwnerService.hasHome(ownerId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You don't own this home.");
            return "redirect:/home/ownedhomes";
        }

        Home home = homeService.getHome(id);
        model.addAttribute("home", home);
        model.addAttribute("formTitle", "Edit Home");
        model.addAttribute("actionUrl", "/home/edit"); // POST to /home/edit/{id} for updates
        return "home/home_form";
    }

    @Secured("ROLE_HOMEOWNER")
    @PostMapping("/edit")
    public String editHome(@Valid @ModelAttribute("home") Home home,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        int ownerId = SecurityUtil.getLoggedInUserId();
        if (!homeOwnerService.hasHome(ownerId, home.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You don't own this home.");
            return "redirect:/home/ownedhomes";
        }

        if (bindingResult.hasErrors()) {
            // Re-add the necessary variables in case of errors.
            model.addAttribute("formTitle", "Edit Home");
            model.addAttribute("actionUrl", "/home/edit");
            return "home/home_form";
        }

        try {


            // If your nested objects (e.g., characteristics) also require the ID to be maintained,
            // update them here if necessary.

            homeService.updateHome(home); // Make sure this method exists in your service layer.
            redirectAttributes.addFlashAttribute("successMessage", "Home updated successfully.");
            return "redirect:/home/ownedhomes";
        } catch (Exception e) {
            System.err.println("Error updating home: " + e.getMessage());
            model.addAttribute("errorMessage", "Unable to update home.");
            model.addAttribute("formTitle", "Edit Home");
            model.addAttribute("actionUrl", "/home/edit");
            return "home/home_form";
        }
    }


}
