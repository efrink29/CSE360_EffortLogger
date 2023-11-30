package effortLogger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;



public class EffortLoggerV2 extends Application {
	
	private char[] userSession;
	// The User class represents an individual user with attributes like password, session key, and type.
	
	
	public class SceneSwitcher {
	    private Stage stage;
	    private Map<String, Scene> scenes;

	    public SceneSwitcher(Stage stage) {
	        this.stage = stage;
	        this.scenes = new HashMap<>();
	    }

	    public void addScene(String name, Scene scene) {
	        scenes.put(name, scene);
	    }

	    public Scene getScene(String name) {
	        return scenes.get(name);
	    }

	    public void switchTo(String name) {
	        stage.setScene(getScene(name));
	    }
	}
	
	public class LoginView {
	    private Scene scene;
	    private TextField username;
	    private PasswordField password;
	    private Button loginButton;
	    private SceneSwitcher sceneSwitcher;
	    private UserManager userManager;

	    public LoginView(SceneSwitcher switcher, UserManager userManager) {
	        this.sceneSwitcher = switcher;
	        this.userManager = userManager;
	        initializeView();
	    }

	    private void initializeView() {
	        // UI Components
	        username = new TextField();
	        username.setPromptText("Username");

	        password = new PasswordField();
	        password.setPromptText("Password");

	        loginButton = new Button("Login");

	        // Layout
	        VBox layout = new VBox(10); // 10 is the spacing between elements
	        layout.setPadding(new Insets(10, 10, 10, 10)); // Set padding
	        layout.getChildren().addAll(username, password, loginButton);

	        // Event Handling
	        loginButton.setOnAction(event -> handleLogin());

	        // Set the scene
	        scene = new Scene(layout, 300, 250); // Adjust the size as needed
	    }

	    private void handleLogin() {
	        String user = username.getText();
	        String pass = password.getText();

	        // Authenticate User (pseudo-code)
	        boolean isAuthenticated = authenticateUser(user, pass);

	        if (isAuthenticated) {
	            // Switch to the appropriate scene based on user type
	            sceneSwitcher.switchTo("userDashboard");
	        } else {
	            // Show error message (pseudo-code)
	            // showAlert("Login Failed", "Invalid username or password");
	        }
	    }

	    // Helper method for authentication (pseudo-code)
	    private boolean authenticateUser(String username, String password) {
	    	User u = userManager.getUser(username);
	    	
	    	if (u == null) {
	    		return false;
	    	}
	    	
	    	userSession = u.login(password);
	    	if (userSession == null) {
	    		return false;
	    	} else {
	    		return true;
	    	}
	    }

	    public Scene getScene() {
	        return scene;
	    }
	}
	
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) {
    	
    	UserManager userManager = new UserManager("/Users/ethanfrink/git/repository/EffortLogger/src/effortLogger/StoredUsers");
    	
        primaryStage.setTitle("Effort Logger");

        // Scene switcher
        SceneSwitcher sceneSwitcher = new SceneSwitcher(primaryStage);

        // Login View
        LoginView loginView = new LoginView(sceneSwitcher, userManager);
        sceneSwitcher.addScene("login", loginView.getScene());
        

        // Set initial scene
        primaryStage.setScene(sceneSwitcher.getScene("login"));
        primaryStage.show();
        userManager.saveUsersToFile();
    }

}