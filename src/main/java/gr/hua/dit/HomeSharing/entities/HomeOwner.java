package gr.hua.dit.HomeSharing.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
//@Table(name = "home_owner")
public class HomeOwner extends User {

    @OneToMany(mappedBy = "homeOwner", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Home> ownedHomes;

    @NotBlank
    private String address;

    @NotBlank
    private String area;

    public HomeOwner() {
    }

    public HomeOwner(String firstName, String lastName, String email, String password, String phoneNumber, LocalDate birthDate, String address, String area) {
        super(firstName, lastName, email, password, phoneNumber, birthDate);
        this.address = address;
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<Home> getOwnedHomes() {
        return ownedHomes;
    }

    public void setOwnedHomes(List<Home> ownedHomes) {
        this.ownedHomes = ownedHomes;
    }

    public void addOwnedHome(Home home) {
        ownedHomes.add(home);
    }

    public void removeOwnedHome(Home home) {
        ownedHomes.remove(home);
    }

    public List<Home> getHomes() {
        return ownedHomes;
    }

    @Override
    public String toString() {
        return "HomeOwner{" +
                "id=" + super.getId() +
                ", firstName='" + super.getFirstName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", email='" + super.getEmail() + '\'' +
                ", phoneNumber='" + super.getPhoneNumber() + '\'' +
                ", birthDate=" + super.getBirthDate() +
                ", ownedHomes=" + ownedHomes +
                '}';
    }
}
