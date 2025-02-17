package org.example.lab6.Project.Application;


import Repository.DataBase.MessagesDBRepository;
import Repository.DataBase.UserDBRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.lab6.Project.Application.Domain.*;
import org.example.lab6.Project.Application.Service.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

import static org.example.lab6.Project.Application.Domain.User.hashPassword;

public class LogInController {

    private SocialNetwork socialNetwork;

    @FXML
    private TextField logInFirstName;

    @FXML
    private TextField logInLastName;

    @FXML
    private PasswordField logInPassword;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private Button logInButton;

    @FXML
    private Button signUpButton;

    @FXML
    public void initialize() {
        logInButton.setOnAction(event -> handleLogIn());
        signUpButton.setOnAction(event -> handleSignUp());
    }
    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }


    private void handleLogIn() {
        String firstName = logInFirstName.getText().trim();
        String lastName = logInLastName.getText().trim();
        String password = logInPassword.getText().trim();


        if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Log In Error", "Please fill in all fields.");
            return;
        }

        User user = socialNetwork.getUserByName(firstName,lastName);



        if (user == null || !user.getPassword().equals(hashPassword(password))) {
            showAlert(Alert.AlertType.ERROR, "Log In Error", "Invalid credentials.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Scene mainScene = new Scene(loader.load());
            Stage currentStage = (Stage) logInButton.getScene().getWindow();


            Controller mainController = loader.getController();
            mainController.setSocialNetwork(socialNetwork);
            mainController.setLoggedInUser(user);


            currentStage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load main window.");
        }
    }

    private void handleSignUp() {
        String firstName = signUpFirstName.getText().trim();
        String lastName = signUpLastName.getText().trim();
        String password = signUpPassword.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Please fill in all fields.");
            return;
        }

        if (socialNetwork.getUserByName(firstName, lastName) != null) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Username is taken. Try another one.");
            return;
        }


        Long id = socialNetwork.getNewUserId();
        User newUser = new User(id, firstName, lastName,password);
        socialNetwork.addUser(newUser);




        showAlert(Alert.AlertType.INFORMATION, "Sign Up Success", "Account created successfully!");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Scene mainScene = new Scene(loader.load());
            Stage currentStage = (Stage) signUpButton.getScene().getWindow();

            Controller mainController = loader.getController();
            mainController.setSocialNetwork(socialNetwork);
            mainController.setLoggedInUser(newUser);

            // schimbarea ferestrei
            currentStage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load main window.");
        }
    }

    // metoda pt afisarea alertelor
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
