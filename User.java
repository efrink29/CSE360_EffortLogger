package effortLogger;

import java.util.Optional;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class User {

    private String password;
    private String username;
    private char[] sessionKey;
    public char type; // User's type: 'e' for employee, 's' for supervisor, 'm' for manager
    
    public String getPassword() {
    	return password;
    }
    
    public char getType() {
    	return type;
    }

    // Constructor for User class
    public User(String username, String password, char type) {
        this.password = password;
        this.type = type;
        this.username = username;
        this.sessionKey = new char[100];
        java.util.Arrays.fill(this.sessionKey, '*');
    }
    
    public String getUsername() {
    	return this.username;
    }

    // Allows a user to login with a password
    public char[] login(String password) {
        if (this.sessionKey[0] != '*') {
            showAlert("Login Error", "User is already Logged in", Alert.AlertType.ERROR);
            return null;
        }
        if (this.validatePassword(password)) {
            this.sessionKey = generateKey();
            showAlert("Login", "Login successful", Alert.AlertType.INFORMATION);
            return this.sessionKey;
        }
        //showAlert("Login Error", "Incorrect Password", Alert.AlertType.ERROR);
        return null;
    }

    // Logs the user out and resets the session key
    public boolean logout() {
        java.util.Arrays.fill(this.sessionKey, '*');
        showAlert("Logout", "User logged out successfully", Alert.AlertType.INFORMATION);
        return true;
    }

    // Allows the user to update their password
    public boolean updatePassword(char[] clientKey, String currentPassword) {
        if (!this.validateKey(clientKey)) {
            showAlert("Session Error", "Invalid Session Key", Alert.AlertType.ERROR);
            return false;
        }
        if (!this.validatePassword(currentPassword)) {
            //showAlert("Password Error", "Incorrect Password", Alert.AlertType.ERROR);
            return false;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password Update");
        dialog.setHeaderText("Update your password");
        dialog.setContentText("Please enter a new password:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && validateInput(result.get())) {
            this.password = result.get();
            showAlert("Password Update", "Password updated successfully", Alert.AlertType.INFORMATION);
            return true;
        }
        return false;
    }

    // Validates the input password with the current password
    private boolean validatePassword(String password) {
        if (!validateInput(password)) {
            showAlert("Validation Error", "Invalid Input Password", Alert.AlertType.ERROR);
            return false;
        }
        if (password.length() != this.password.length()) {
            showAlert("Validation Error", "Incorrect Password", Alert.AlertType.ERROR);
            return false;
        }
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) != this.password.charAt(i)) {
                showAlert("Validation Error", "Incorrect Password", Alert.AlertType.ERROR);
                return false;
            }
        }
        return true;
    }

    // Validates the input string to have allowed characters
    private boolean validateInput(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!(c == 33 || (c >= 35 && c <= 38) || (c >= 40 && c <= 59) || (c >= 63 && c <= 126))) {
                
                return false;
            }
        }
        return true;
    }


    // Validates the session key provided by the client
    public boolean validateKey(char[] clientKey) {
        if (this.sessionKey[0] == '*') {
            showAlert("Session Error", "User is not currently Logged in", Alert.AlertType.ERROR);
            return false;
        }
        if (clientKey.length != 100) {
            showAlert("Key Error", "Provided key is not formatted correctly: Size = " + clientKey.length + " Expected = 100", Alert.AlertType.ERROR);
            return false;
        }
        for (int i = 0; i < 100; i++) {
            if (clientKey[i] != this.sessionKey[i]) {
                showAlert("Key Error", "Invalid Session Key", Alert.AlertType.ERROR);
                return false;
            }
        }
        return true;
    }

    // Generates a unique session key
    private char[] generateKey() {
        Random rand = new Random();
        char[] newKey = new char[100];
        for (int i = 0; i < 100; i++) {
            newKey[i] = (char) (65 + rand.nextInt(25));
        }
        return newKey;
    }
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}