package com.college.utilities;

import com.college.domain.User1;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordService {

    public static String hashPassword(String plainTextPassword) {
        // Generate a salt and hash the password
        // BCrypt.gensalt() generates a salt with default rounds (10)
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // Check if the plain-text password matches the stored hashed password
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }


    // TESTING //
    public static void main(String[] args){
        // Example of creating a new user
        String plainPassword = "mySecretPassword123";
        String hashedPassword = PasswordService.hashPassword(plainPassword);
        User1 newUser = new User1("john.doe", hashedPassword);

        System.out.println("\nTESTING");
        System.out.println(plainPassword);
        System.out.println(hashedPassword);
        System.out.println(newUser);

        // Save newUser to database
        // ...


        // Example of user authentication
//        String providedPassword = "mySecretPassword123";
//        User1 storedUser = retrieveUserFromDatabase("john.doe");
//        // Assuming you retrieve the User object
//
//        if (storedUser != null && PasswordService.checkPassword(providedPassword, storedUser.getHashedPassword())) {
//            System.out.println("Authentication successful!");
//        } else {
//            System.out.println("Authentication failed.");
//        }
    }
}