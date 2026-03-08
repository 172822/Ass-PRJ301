/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class SubArea {

    private Integer id;
    private Integer areaId;
    private String name;

    public SubArea() {}

    public SubArea(Integer id, Integer areaId, String name) {
        this.id = id;
        this.areaId = areaId;
        this.name = name;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAreaId() { return areaId; }
    public void setAreaId(Integer areaId) { this.areaId = areaId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
