package gr.hua.dit.HomeSharing.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "home_characteristics")
public class HomeCharacteristics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "characteristics", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Home home;

    @NotBlank
    private String address;

    @NotBlank
    private String area;

    @Min(1)
    private Integer rooms;

    @Min(0)
    private Integer bathrooms;

    @Column(name = "square_meters")
    @Min(1)
    private Integer squareMeters;

    @NotNull
    private Integer floor;

    @Column(name = "human_capacity")
    @Min(1)
    private Integer humanCapacity;

    public HomeCharacteristics() {
    }

    public HomeCharacteristics(String address, String area, int rooms, int bathrooms, int squareMeters, int floor, int humanCapacity) {
        this.address = address;
        this.area = area;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.squareMeters = squareMeters;
        this.floor = floor;
        this.humanCapacity = humanCapacity;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Integer getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(int squareMeters) {
        this.squareMeters = squareMeters;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Integer getHumanCapacity() {
        return humanCapacity;
    }

    public void setHumanCapacity(int humanCapacity) {
        this.humanCapacity = humanCapacity;
    }

    @Override
    public String toString() {
        return "HomeCharacteristics{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", area='" + area + '\'' +
                ", rooms=" + rooms +
                ", bathrooms=" + bathrooms +
                ", squareMeters=" + squareMeters +
                ", floor=" + floor +
                ", humanCapacity=" + humanCapacity +
                '}';
    }
}
