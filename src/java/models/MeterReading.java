/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class MeterReading {

    private Integer id;
    private Integer roomId;
    private Integer month;
    private Integer year;
    private Integer electricStart;
    private Integer electricEnd;
    private Integer waterStart;
    private Integer waterEnd;

    public MeterReading() {}

    public MeterReading(Integer id, Integer roomId, Integer month, Integer year,
                        Integer electricStart, Integer electricEnd,
                        Integer waterStart, Integer waterEnd) {
        this.id = id;
        this.roomId = roomId;
        this.month = month;
        this.year = year;
        this.electricStart = electricStart;
        this.electricEnd = electricEnd;
        this.waterStart = waterStart;
        this.waterEnd = waterEnd;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getElectricStart() { return electricStart; }
    public void setElectricStart(Integer electricStart) { this.electricStart = electricStart; }

    public Integer getElectricEnd() { return electricEnd; }
    public void setElectricEnd(Integer electricEnd) { this.electricEnd = electricEnd; }

    public Integer getWaterStart() { return waterStart; }
    public void setWaterStart(Integer waterStart) { this.waterStart = waterStart; }

    public Integer getWaterEnd() { return waterEnd; }
    public void setWaterEnd(Integer waterEnd) { this.waterEnd = waterEnd; }
}
