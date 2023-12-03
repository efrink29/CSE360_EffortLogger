package effortLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;


public class UserManager {

    private HashMap<String, Integer> userIDs; // Maps usernames to their respective IDs
    private ArrayList<User> validUsers; // List of valid users
    private String userFileName; // path to file where user data is stored
    private static final String AES = "AES";
    private static final byte[] keyValue = 
        new byte[] { 'Y', 'o', 'u', 'r', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y', 'H', 'e', 'r' };

    
    public UserManager(String userFileName) throws Exception {
        userIDs = new HashMap<>();
        validUsers = new ArrayList<>();
        this.userFileName = userFileName;

        try {
            File file = new File(userFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Assuming the file format is "username,password,type"
                String[] userDetails = line.split(",");

                if (userDetails.length == 3) {
                	String username = userDetails[0];
                    String encryptedPassword = userDetails[1];
                    String password = AESdecrypt(encryptedPassword); // Decrypt the password
                	//String password = userDetails[1];
                    char type = userDetails[2].charAt(0);

                    User user = new User(username, password, type);
                    validUsers.add(user);
                    userIDs.put(username, validUsers.size()-1); // The ID is the list index
                }
            }
            scanner.close();
            
        } catch (FileNotFoundException e) {
        	showAlert("File Error", "An error occurred while writing to the file: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    public void saveUsersToFile() throws Exception {
        try {
            FileWriter fileWriter = new FileWriter(userFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (User u : validUsers) {
                String username = u.getUsername();
                
                // Modified to generate encrypted password
                String encryptedPassword = AESencrypt(u.getPassword()); // Encrypt the password
                String line = String.format("%s,%s,%c", username, encryptedPassword, u.getType());
                System.out.println(line);
                printWriter.println(line);
            }

            printWriter.close();
        } catch (IOException e) {
        	showAlert("File Error", "An error occurred while writing to the file: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @Override
    protected void finalize() {
    	
    	try {
			saveUsersToFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Validates the user based on userID and the provided session key
    public boolean validateUser(int userID, char[] clientKey) {
        int index = userID - 1;
        if (userID > this.validUsers.size() || userID < 0) {
            showAlert("Validation Error", "ID out of range", Alert.AlertType.ERROR);
            return false;
        }
        User u = validUsers.get(index);
        return u.validateKey(clientKey);
    }

    // Adds a new user to the system
    public boolean addUser(String managerUserName, char[] managerKey, String username, String password, char type) {
        // Validations for input data
    	if (!validateInput(managerUserName) || !validateInput(username) || !validateInput(password)) {
            showAlert("Input Error", "Invalid input", Alert.AlertType.ERROR);
            return false;
        }

        validUsers.add(new User(username, password, type));
        int userID = validUsers.size();
        userIDs.put(username, userID);
        return true;
    }

    // Fetches a User object based on username
    public User getUser(String userName) {
        if (validateInput(userName)) {
            int userID = getID(userName);
            if (userID != -1) {
                return validUsers.get(userID);
            }
        }
        return null;
    }

    // Retrieves the ID associated with the given username
    private int getID(String userName) {
        Integer userID = userIDs.get(userName);
        if (userID == null) {
            showAlert("User Error", "User not found", Alert.AlertType.ERROR);
            return -1;
        }
        return userID;
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
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private static String AESencrypt(String Data) throws Exception  {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    private static String AESdecrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, AES);
    }

    // Simple Base64 Encryption
	 private String encrypt(String data) {
	     return Base64.getEncoder().encodeToString(data.getBytes());
	 }
	
	 // Simple Base64 Decryption
	 private String decrypt(String data) {
	     return new String(Base64.getDecoder().decode(data));
	 }
}
