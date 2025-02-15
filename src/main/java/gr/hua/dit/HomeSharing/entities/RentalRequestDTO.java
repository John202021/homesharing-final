package gr.hua.dit.HomeSharing.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// NOT AN ENTITY IN THE DATABASE
public class RentalRequestDTO {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Rental period is required")
    @Min(value = 1, message = "Rental period must be at least 1 day")
    @Max(value = 30, message = "Rental period cannot exceed 30 days")
    private Integer days;

    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "At least 1 person is required")
    private Integer numberOfPeople;

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }

    public Integer getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }
}