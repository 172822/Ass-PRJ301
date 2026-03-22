/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class BoardingHouse {
    private Integer id;
    private Integer landlordId;
    private Integer subAreaId;
    private String name;
    private String address;
    /** Đường dẫn tương đối trong webapp, ví dụ {@code uploads/boarding/uuid.jpg} */
    private String imagePath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(Integer landlordId) {
        this.landlordId = landlordId;
    }

    public Integer getSubAreaId() {
        return subAreaId;
    }

    public void setSubAreaId(Integer subAreaId) {
        this.subAreaId = subAreaId;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public BoardingHouse(Integer id, Integer landlordId, Integer subAreaId, String name, String address) {
        this(id, landlordId, subAreaId, name, address, null);
    }

    public BoardingHouse(Integer id, Integer landlordId, Integer subAreaId, String name, String address, String imagePath) {
        this.id = id;
        this.landlordId = landlordId;
        this.subAreaId = subAreaId;
        this.name = name;
        this.address = address;
        this.imagePath = imagePath;
    }

    public BoardingHouse() {
    }
    
    
}
