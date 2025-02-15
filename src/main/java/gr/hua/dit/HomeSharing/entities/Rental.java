package gr.hua.dit.HomeSharing.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "renter_id", nullable = false)
    private Renter renter;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @Column(name = "number_of_people")
    @Min(1)
    private Integer numberOfPeople;

    @Column(name = "total_price")
    @Min(0)
    private Integer totalPrice;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @Column(name = "request_processed_at")
    private LocalDateTime requestProcessedAt;

    private Boolean accepted;

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
    }
    
    public Rental() {
    }

    public Rental(LocalDate startDate, LocalDate endDate, Integer numberOfPeople, Integer totalPrice) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfPeople = numberOfPeople;
        this.totalPrice = totalPrice;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Renter getRenter() {
        return renter;
    }

    public void setRenter(Renter renter) {
        this.renter = renter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
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
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", renterId=" + renter.getId() +
                ", homeId=" + home.getId() +
                ", numberOfPeople=" + numberOfPeople +
                ", totalPrice=" + totalPrice +
                ", requestedAt=" + requestedAt +
                ", requestProcessedAt=" + requestProcessedAt +
                ", accepted=" + accepted +
                '}';
    }
}
