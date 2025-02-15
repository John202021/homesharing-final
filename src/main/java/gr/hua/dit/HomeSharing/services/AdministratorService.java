package gr.hua.dit.HomeSharing.services;

import gr.hua.dit.HomeSharing.entities.*;
import gr.hua.dit.HomeSharing.repositories.AdministratorRepository;
import gr.hua.dit.HomeSharing.repositories.HomeOwnerRepository;
import gr.hua.dit.HomeSharing.repositories.HomeRepository;
import gr.hua.dit.HomeSharing.repositories.RenterRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdministratorService {
    private AdministratorRepository administratorRepository;
    private HomeRepository homeRepository;
    private RenterRepository renterRepository;
    private HomeOwnerRepository homeOwnerRepository;

    public AdministratorService(AdministratorRepository administratorRepository, HomeRepository homeRepository, RenterRepository renterRepository, HomeOwnerRepository homeOwnerRepository) {
        this.administratorRepository = administratorRepository;
        this.homeRepository = homeRepository;
        this.renterRepository = renterRepository;
        this.homeOwnerRepository = homeOwnerRepository;
    }

    @Transactional
    public List<Administrator> getAdministrators() {
        return administratorRepository.findAll();
    }

    @Transactional
    public Administrator getAdministrator(int id) {
        return administratorRepository.findById(id).get();
    }

    @Transactional
    public void deleteAdministrator(int id) {
        administratorRepository.deleteById(id);
    }
}
