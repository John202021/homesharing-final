package gr.hua.dit.HomeSharing.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "home_owner_id", nullable = false)
    private HomeOwner homeOwner;

    @OneToMany(mappedBy = "home", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Rental> rentals;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "characteristics_id", referencedColumnName = "id", nullable = false)
    private HomeCharacteristics characteristics;

    @Column(name = "daily_price")
    @Min(0)
    private Integer dailyPrice;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @Column(name = "request_processed_at")
    private LocalDateTime requestProcessedAt;

    private Boolean accepted;

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
    }

    public Home() {
    }

    public Home(HomeCharacteristics characteristics, Integer dailyPrice) {
        this.characteristics = characteristics;
        this.dailyPrice = dailyPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public void setDailyPrice(Integer dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getRequestProcessedAt() {
        return requestProcessedAt;
    }

    public void setRequestProcessedAt(LocalDateTime requestProcessedAt) {
        this.requestProcessedAt = requestProcessedAt;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        if(!accepted) {
            this.accepted = false;
        } else {
            this.accepted = true;
        }
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public HomeCharacteristics getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(HomeCharacteristics characteristics) {
        this.characteristics = characteristics;
    }

    public Integer getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(int dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public HomeOwner getHomeOwner() {
        return homeOwner;
    }

    public void setHomeOwner(HomeOwner owner) {
        this.homeOwner = owner;
    }

    public void addRental(Rental rental) {
        if (rentals == null) {
            rentals = new ArrayList<>();
        }
        rentals.add(rental);
    }

    @Override
    public String toString() {
        return "Home{" +
                "id=" + id +
                ", homeOwnerId=" + homeOwner.getId() +
                ", rentals=" + rentals +
                ", characteristicsId=" + characteristics.getId() +
                ", dailyPrice=" + dailyPrice +
                ", requestedAt=" + requestedAt +
                ", requestProcessedAt=" + requestProcessedAt +
                ", accepted=" + accepted +
                '}';
    }
}