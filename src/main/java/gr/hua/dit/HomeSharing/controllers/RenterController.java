package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.HomeOwner;
import gr.hua.dit.HomeSharing.entities.Rental;
import gr.hua.dit.HomeSharing.entities.Renter;
//import gr.hua.dit.HomeSharing.entities.UserDetails;
import gr.hua.dit.HomeSharing.services.HomeService;
import gr.hua.dit.HomeSharing.services.RentalService;
import gr.hua.dit.HomeSharing.services.RenterService;
import gr.hua.dit.HomeSharing.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/renter")
public class RenterController {
    RenterService renterService;
    RentalService rentalService;
    HomeService homeService;

    public RenterController(RenterService renterService, RentalService rentalService, HomeService homeService) {
        this.renterService = renterService;
        this.rentalService = rentalService;
        this.homeService = homeService;
    }

//    @Secured("ROLE_ADMIN")
//    @GetMapping("")
//    public String getRenters(Model model) {
//        List<Renter> renters = renterService.getRenters();
//        System.out.print(renters);
//        model.addAttribute("renters", renters);
//        return "renter/renters";
//    }

    @Secured("ROLE_ADMIN") // A renter can only see his own profile
    @GetMapping("{id}")
    public String getRenter(@PathVariable int id, Model model) {
        Renter renter = renterService.getRenter(id);
        System.out.println(renter);
        model.addAttribute("renter", renter);
        return "renter/renter_profile";
    }

    @Secured("ROLE_RENTER")
    @GetMapping("me")
    public String getRenter(Model model) {
        int renterId = SecurityUtil.getLoggedInUserId();
        Renter renter = renterService.getRenter(renterId);
        System.out.println(renter);
        model.addAttribute("renter", renter);
        return "renter/renter_profile";
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("{id}")
    public String deleteRenter(@PathVariable int id) {
        renterService.deleteRenter(id);
        System.out.println("Delete Renter request");
        return "index";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("edit")
    public String editRenter (@Valid @ModelAttribute("renter") Renter updatedRenter, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() && bindingResult.getErrorCount() > 1 || !bindingResult.hasFieldErrors("password")) {
            model.addAttribute("homeowner", updatedRenter);
            return "renter/edit_renter_form";
        }

        renterService.updateRenter(updatedRenter);
        return "redirect:/users";
    }
}

