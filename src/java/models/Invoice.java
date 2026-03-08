/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class Invoice {

    private Integer id;
    private Integer roomId;
    private Integer month;
    private Integer year;
    private Double roomPrice;
    private Double electricPrice;
    private Double waterPrice;
    private Double totalPrice;
    private String status;

    public Invoice() {}

    public Invoice(Integer id, Integer roomId, Integer month, Integer year,
                   Double roomPrice, Double electricPrice,
                   Double waterPrice, Double totalPrice, String status) {
        this.id = id;
        this.roomId = roomId;
        this.month = month;
        this.year = year;
        this.roomPrice = roomPrice;
        this.electricPrice = electricPrice;
        this.waterPrice = waterPrice;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Double getRoomPrice() { return roomPrice; }
    public void setRoomPrice(Double roomPrice) { this.roomPrice = roomPrice; }

    public Double getElectricPrice() { return electricPrice; }
    public void setElectricPrice(Double electricPrice) { this.electricPrice = electricPrice; }

    public Double getWaterPrice() { return waterPrice; }
    public void setWaterPrice(Double waterPrice) { this.waterPrice = waterPrice; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
