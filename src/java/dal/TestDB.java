/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author Admin
 */

import models.User;
import java.util.List;

public class TestDB {

    public static void main(String[] args) {

        UserDAO dao = new UserDAO();

        List<User> users = dao.getAllUsers();
        for (User u : users) {
            System.out.println("ID: " + u.getId());
            System.out.println("Name: " + u.getFullName());
            System.out.println("Phone: " + u.getPhone());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Role: " + u.getRole());
            System.out.println("Active: " + u.getIsActive());
            System.out.println("-----------------------");
        }
    }
}
