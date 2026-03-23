package models;

public class BoardingHouseListItem {

    private Integer id;
    private String name;
    private String address;
    private String areaName;
    private String subAreaName;
    private String landlordFullName;
    private String landlordPhone;
    private String landlordEmail;
    private String imagePath;

    public BoardingHouseListItem() {}

    public BoardingHouseListItem(Integer id, String name, String address, String areaName, String subAreaName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.areaName = areaName;
        this.subAreaName = subAreaName;
    }

    public BoardingHouseListItem(Integer id, String name, String address, String areaName, String subAreaName,
            String landlordFullName, String landlordPhone, String landlordEmail, String imagePath) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.areaName = areaName;
        this.subAreaName = subAreaName;
        this.landlordFullName = landlordFullName;
        this.landlordPhone = landlordPhone;
        this.landlordEmail = landlordEmail;
        this.imagePath = imagePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getSubAreaName() {
        return subAreaName;
    }

    public void setSubAreaName(String subAreaName) {
        this.subAreaName = subAreaName;
    }

    public String getLandlordFullName() {
        return landlordFullName;
    }

    public void setLandlordFullName(String landlordFullName) {
        this.landlordFullName = landlordFullName;
    }

    public String getLandlordPhone() {
        return landlordPhone;
    }

    public void setLandlordPhone(String landlordPhone) {
        this.landlordPhone = landlordPhone;
    }

    public String getLandlordEmail() {
        return landlordEmail;
    }

    public void setLandlordEmail(String landlordEmail) {
        this.landlordEmail = landlordEmail;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
