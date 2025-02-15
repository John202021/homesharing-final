package gr.hua.dit.HomeSharing.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Renter extends User {
    @OneToMany(mappedBy = "renter", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Rental> rentals;

    public Renter() {
    }

    public Renter(String firstName, String lastName, String email, String password, String phoneNumber, LocalDate birthDate) {
        super(firstName, lastName, email, password, phoneNumber, birthDate);
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public void addRental(Rental rental) {
        this.rentals.add(rental);
    }

    public void removeRental(Rental rental) {
        this.rentals.remove(rental);
    }

    @Override
    public String toString() {
        return "Renter{" +
                "id=" + super.getId() +
                ", firstName='" + super.getFirstName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", email='" + super.getEmail() + '\'' +
                ", phoneNumber='" + super.getPhoneNumber() + '\'' +
                ", birthDate=" + super.getBirthDate() +
                ", rentals=" + rentals +
                '}';
    }
}
