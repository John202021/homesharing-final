package gr.hua.dit.HomeSharing.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;

    @Column(unique = true)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Column(updatable = false)
    @Size(min = 4)
    private String password;

    @Column(name = "phone_number")
    @NotBlank
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @Column(name = "birth_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate birthDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String phoneNumber, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                ", roles=" + roles +
                ", registrationDate=" + registrationDate +
                '}';
    }
}


