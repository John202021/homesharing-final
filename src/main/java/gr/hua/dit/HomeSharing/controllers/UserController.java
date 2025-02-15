package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.HomeOwner;
import gr.hua.dit.HomeSharing.entities.Renter;
import gr.hua.dit.HomeSharing.entities.User;
import gr.hua.dit.HomeSharing.repositories.RoleRepository;
import gr.hua.dit.HomeSharing.services.HomeOwnerService;
import gr.hua.dit.HomeSharing.services.RenterService;
import gr.hua.dit.HomeSharing.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    private UserService userService;
    private RoleRepository roleRepository;
    private RenterService renterService;
    private HomeOwnerService homeOwnerService;


    public UserController(UserService userService, RoleRepository roleRepository, RenterService renterService, HomeOwnerService homeOwnerService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.renterService = renterService;
        this.homeOwnerService = homeOwnerService;
    }

    @GetMapping("/register-renter")
    public String showRegisterRenterForm(Model model){
        model.addAttribute("renter", new Renter());
        return "auth/register_renter";
    }

    @PostMapping("/register-renter")
    public String registerUser(@Valid @ModelAttribute Renter renter, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "auth/register_renter";
        }
        try {
            userService.registerUser(renter);
        } catch (Exception e){
            String message = "An error occurred while registering, please try again";
            model.addAttribute("errorMessage", message);
            return "auth/register_renter";
        }

        String message;
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) { // only admin can make post requests here and be authenticated
            message = "Successfully created a renter";
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/users";
        }
        message = "Successfully registered, now you can login with your credentials";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/login";
    }

    @GetMapping("/register-owner")
    public String showRegisterOwnerForm(Model model){
        model.addAttribute("owner", new HomeOwner());
        return "auth/register_owner";
    }

    @PostMapping("/register-owner")
    public String registerOwner(@Valid @ModelAttribute("owner") HomeOwner owner, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "auth/register_owner";
        }
        try {
            userService.registerUser(owner);
        } catch (Exception e){
            String message = "An error occurred while registering, please try again";
            model.addAttribute("errorMessage", message);
            return "auth/register_owner";
        }

        String message;
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) { // only admin can make post requests here and be authenticated
            message = "Successfully created an owner";
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/users";
        }
        message = "Successfully registered, now you can login with your credentials";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/login";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public String showUsers(Model model){
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "user/users";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users/renters")
    public String showRenters(Model model){
        model.addAttribute("users", renterService.getRenters());
        return "user/users";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users/owners")
    public String showOwners(Model model){
        model.addAttribute("users", homeOwnerService.getHomeOwners());
        return "user/users";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable Long user_id, Model model){
        model.addAttribute("user", userService.getUser(user_id));
        return "index";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/user/edit/{user_id}")
    public String editUserForm(@PathVariable Long user_id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(user_id);
        model.addAttribute("user", user);

        if (user instanceof Renter) {
            System.out.println("EDITING RENTER __________________________________________________________");
            model.addAttribute("renter", user);
            return "renter/edit_renter_form";
        } else if (user instanceof HomeOwner homeOwner) {
            System.out.println("EDITING OWNER __________________________________________________________");
            model.addAttribute("homeowner", homeOwner);
            return "home_owner/edit_owner_form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You can't edit an admin");
            return "redirect:/users";
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/user/{user_id}")
    public String deleteUser(@PathVariable Long user_id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(user_id);

            if (user instanceof Renter) {
                renterService.deleteRenter(user.getId());
            } else if (user instanceof HomeOwner) {
                homeOwnerService.deleteHomeOwner(user.getId());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "You can't delete an admin");
                return "redirect:/users";
            }

            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while trying to delete the user.");
        }
        return "redirect:/users";
    }
}