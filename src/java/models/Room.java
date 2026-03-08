/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class Room {
    private Integer id;
    private Integer boardingHouseId;
    private String roomCode;
    private Double price;
    private Integer maxPerson;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBoardingHouseId() {
        return boardingHouseId;
    }

    public void setBoardingHouseId(Integer boardingHouseId) {
        this.boardingHouseId = boardingHouseId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(Integer maxPerson) {
        this.maxPerson = maxPerson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Room(Integer id, Integer boardingHouseId, String roomCode, Double price, Integer maxPerson, String status) {
        this.id = id;
        this.boardingHouseId = boardingHouseId;
        this.roomCode = roomCode;
        this.price = price;
        this.maxPerson = maxPerson;
        this.status = status;
    }

    public Room() {
    }
          
    
}
