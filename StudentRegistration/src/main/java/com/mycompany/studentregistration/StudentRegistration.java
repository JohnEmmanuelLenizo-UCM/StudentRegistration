package com.mycompany.studentregistration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class StudentRegistration {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        //String url = "jdbc:ucanaccess://C://Users//John//Downloads//DBpractice//StudentRegistration//Database//StudentRegistration.accdb"; //this is local db (Access Database)
        String url = "jdbc:mysql://localhost:3306/studentregistration?allowPublicKeyRetrieval=true&useSSL=false";
        String dbUser = "app_user";
        String dbPass = "studentregistrationapp123";
        
        String getUserInformation = "SELECT FirstName, MiddleName, LastName, Email, Password FROM Profile WHERE ID = ?";
        String registerUserInformation = "INSERT INTO Profile (ID, FirstName, MiddleName, LastName, Email, Password) VALUES (?, ?, ?, ?, ?, ?)";
        int userID;
        int latestID = 1;
        int selection;
        String firstname, middlename, lastname, regEmail, regPassword;
        
        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass);){
            do {
                //input
                System.out.print( """
                                    ==============================
                                    STUDENT REGISTRATION APP
                                    ==============================
                                    1. Register
                                    2. Find
                                    3. Exit
                                    ==============================
                                    """);
                System.out.print("Enter selection : ");
                selection = input.nextInt();
                input.nextLine();
                System.out.println("==============================");
                System.out.println();
                
                switch (selection) {
                case 1:
                    PreparedStatement updateID = connection.prepareStatement("SELECT MAX(ID) + 1 FROM Profile");
                    try (ResultSet rs = updateID.executeQuery();) {
                        if (rs.next()) {
                            latestID = rs.getInt(1);
                        }
                        System.out.println("==============================");    
                        System.out.println("REGISTER");
                        System.out.println("=============================="); 
                        System.out.println("Student ID: " + latestID);    
                        System.out.print("First Name: ");
                        firstname = input.nextLine();
                        System.out.print("Middle Name: ");
                        middlename = input.nextLine();
                        System.out.print("Last Name: ");
                        lastname = input.nextLine();
                        System.out.print("Email: ");
                        regEmail = input.nextLine();
                        System.out.print("Password: ");
                        regPassword = input.nextLine();

                        try(PreparedStatement registerUser = connection.prepareStatement(registerUserInformation);) {
                            registerUser.setInt(1, latestID);
                            registerUser.setString(2, firstname);
                            registerUser.setString(3, middlename);
                            registerUser.setString(4, lastname);
                            registerUser.setString(5, regEmail);
                            registerUser.setString(6, regPassword);
                            
                            int rowsAffected = registerUser.executeUpdate();
                            if (rowsAffected > 0) {
                            System.out.println("==============================");
                            System.out.println("SUCCESSFULLY REGISTERED");
                            System.out.println("==============================");
                            continue;  
                            }   
                        }
                        catch (Exception e) {
                            System.out.println("DATABASE ERROR OCCURED");
                        }                   
                    }
                break;
                case 2:
                    System.out.println("==============================");
                    System.out.print("Enter student ID: " );
                    userID = input.nextInt();
                    input.nextLine();
                    System.out.println("==============================");
                    //set userID
                    PreparedStatement findUser = connection.prepareStatement(getUserInformation);
                    findUser.setInt(1, userID);
                    //Execute Query
                    try (ResultSet result = findUser.executeQuery();) {
                         if (result.next()) {
                            String name = result.getString(1) + " " + result.getString(2) + " " + result.getString(3);
                            String email = result.getString(4);
                            String password = result.getString(5);
                            System.out.println("RESULT");
                            System.out.println("==============================");
                            System.out.println("Student Name  : " + name);
                            System.out.println("Email         : " + email);
                            System.out.println("Password      : " + password);
                            System.out.println("==============================");
                            System.out.println();
                            continue;
                        }
                        else {
                            System.out.println("DATABASE ERROR OCCURED");
                        }
                    }
                    break;
                default:
                    System.exit(0);
                    break;
                }
            } while (true);
        }
        catch (Exception e) {
            System.out.print(e);
        }
    }
}
