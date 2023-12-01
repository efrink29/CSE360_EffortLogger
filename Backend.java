package application;

import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EffortLogger Prototype");

        // Create layout elements
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("EffortLogger Prototype");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Perform backend data processing here (e.g., check username uniqueness and password strength)
            // You can add logic to display appropriate messages based on the result.

            // For this prototype, let's display a simple message.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Result");
            alert.setHeaderText(null);

            if (isUsernameUnique(username) && isPasswordStrong(password)) {
                alert.setContentText("Registration successful!");
            } else {
                alert.setContentText("Registration failed. Please check your input.");
            }

            alert.showAndWait();
        });

        // Add elements to the layout
        root.getChildren().addAll(titleLabel, usernameField, passwordField, registerButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Mock functions for username uniqueness and password strength (you should replace with actual logic)
    private boolean isUsernameUnique(String username) {
        // Add your logic to check if the username is unique
        return true;
    }

    private boolean isPasswordStrong(String password) {
        // Add your logic to check password strength
        return password.length() >= 8;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
