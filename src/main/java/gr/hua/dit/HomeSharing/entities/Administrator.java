package gr.hua.dit.HomeSharing.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
public class Administrator extends User {
    public Administrator() {
    }

    public Administrator(String firstName, String lastName, String email, String password, String phoneNumber, LocalDate birthDate) {
        super(firstName, lastName, email, password, phoneNumber, birthDate);
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", birthDate=" + getBirthDate() +
                '}';
    }
}
