/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotel.management.system;

import java.util.ArrayList;

/**
 *
 * @author moham
 */
public class HotelManagementSystem {
   
    public static ArrayList<Rooms> rooms = new ArrayList<>();
    public static ArrayList<coustmers> guests = new ArrayList<>();
    public static ArrayList<Employees> employees = new ArrayList<>();
    public static ArrayList<Reservation> reservations = new ArrayList<>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LogIn().setVisible(true);
            }
        });
   
    }
    
}
