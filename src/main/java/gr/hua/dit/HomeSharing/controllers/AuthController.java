package gr.hua.dit.HomeSharing.controllers;

import gr.hua.dit.HomeSharing.entities.Role;
import gr.hua.dit.HomeSharing.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    RoleRepository roleRepository;

    public AuthController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void setup() {
        Role role_renter = new Role("ROLE_RENTER");
        Role role_homeowner = new Role("ROLE_HOMEOWNER");
        Role role_admin = new Role("ROLE_ADMIN");

        roleRepository.updateOrInsert(role_renter);
        roleRepository.updateOrInsert(role_homeowner);
        roleRepository.updateOrInsert(role_admin);
    }

    @GetMapping("/login")
    public String login() {
//        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//            return "redirect:/home";
//        }
        return "auth/login";
    }
}