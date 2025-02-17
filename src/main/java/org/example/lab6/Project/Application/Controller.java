package org.example.lab6.Project.Application;

import Repository.Pagination.Page;
import Repository.Pagination.Pageable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.lab6.Project.Application.Domain.Message;
import org.example.lab6.Project.Application.Enum.FriendshipRequest;
import org.example.lab6.Project.Application.Messages.MessageUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

import org.example.lab6.Project.Application.Domain.Friendship;
import org.example.lab6.Project.Application.Domain.User;
import org.example.lab6.Project.Application.Service.SocialNetwork;
import org.example.lab6.Project.Application.Enum.FriendshipRequest;
/**
 * Controller class for managing user and friendship operations in the SocialNetwork application.
 * This class is responsible for handling UI interactions and updating the model accordingly.
 */
public class Controller implements Initializable {
    SocialNetwork socialNetwork;
    User loggedUser;

    ObservableList<User> userObservableList = FXCollections.observableArrayList();
    ObservableList<Friendship> friendshipObservableList = FXCollections.observableArrayList();
    ObservableList<Friendship> friendRequestObservableList = FXCollections.observableArrayList();


    private int currentPageUsers = 0;
    private int pageSizeUsers = 5;

    private int currentPageFriendships = 0;
    private int pageSizeFriendships = 5;

    private int currentPageFriendRequests = 0;
    private int pageSizeFriendRequests = 5;
    private int currentPageUserFriends = 0;
    private int pageSizeUserFriends = 10;




    @FXML
    private Button nextBtnFriendRequests;
    @FXML
    private Button previousBtnFriendRequests;
    @FXML
    private Button lastPageBtnFriendRequests;
    @FXML
    private Button firstPageBtnFriendRequests;
    @FXML
    private TextField noElementsOnPageFriendRequests;
    @FXML
    private Label pageNumberFriendRequests;

    @FXML
    private Button nextBtnFriendships;
    @FXML
    private Button previousBtnFriendships;
    @FXML
    private Button lastPageBtnFriendships;
    @FXML
    private Button firstPageBtnFriendships;
    @FXML
    private TextField noElementsOnPageFriendships;
    @FXML
    private Label pageNumberFriendships;



    @FXML
    private Button nextBtnFriendships2;
    @FXML
    private Button previousBtnFriendships2;
    @FXML
    private Button lastPageBtnFriendships2;
    @FXML
    private Button firstPageBtnFriendships2;
    @FXML
    private TextField noElementsOnPageFriendships2;
    @FXML
    private Label pageNumberFriendships2;



    @FXML
    private Button nextBtnUsers;
    @FXML
    private Button previousBtnUsers;
    @FXML
    private Button lastPageBtnUsers;
    @FXML
    private Button firstPageBtnUsers;
    @FXML
    private TextField noElementsOnPageUsers;
    @FXML
    private Label pageNumberUsers;

    @FXML private ListView<Message> listMessages;
    @FXML private TextArea message;
    @FXML private ChoiceBox<User> choiceboxId1;
    @FXML
    private ListView<User> listUsers;
    @FXML
    private ListView<Friendship> listFriendships;

    @FXML
    private ListView<Friendship> listFriendships2;
    @FXML
    private ListView<Friendship> listFriendRequests;

    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldFirstName1;
    @FXML
    private TextField textFieldLastName1;
    @FXML
    private TextField textFieldFirstName2;
    @FXML
    private TextField textFieldLastName2;

    @FXML
    private Button myProfileButton;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Button uploadPictureButton;

    @FXML
    private Label userNameLabel;


    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public void setLoggedInUser(User user) {
        this.loggedUser = user;
        initApp();
        updateProfilePage();
    }

    private void updateProfilePage() {
        if (loggedUser != null) {
            userNameLabel.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName());

            String imageUrl = loggedUser.getProfileImageUrl();

            if (imageUrl != null ) {
                profileImageView.setImage(new Image(imageUrl));
            } else {
                imageUrl = getClass().getResource("/images/default_profile.png").toExternalForm();
                profileImageView.setImage(new Image(imageUrl));
            }
        }
    }

    @FXML
    private void uploadProfilePicture(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String imageUrl = selectedFile.toURI().toString();
            loggedUser.setProfileImageUrl(imageUrl);
            socialNetwork.saveURL(loggedUser);
            Image profileImage = new Image(imageUrl);
            profileImageView.setImage(profileImage);
        }
    }



    /**
     * Initializes the ListView items with the provided ObservableLists.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listUsers.setItems(userObservableList);
        listFriendships.setItems(friendshipObservableList);
        listFriendships2.setItems(friendshipObservableList);
        listFriendRequests.setItems(friendRequestObservableList);

    }

    /**
     * Initializes the application by populating the ListView items with users and friendships.
     * This method handles both logged-in and non-logged-in user scenarios.
     */
    public void initApp() {
        userObservableList.clear();
        friendshipObservableList.clear();
        friendRequestObservableList.clear();
        choiceboxId1.getItems().clear();
        for(User u:socialNetwork.getUsers())
            choiceboxId1.getItems().add(u);


        //Users
        Page<User> pageUsers=socialNetwork.findAllUsers(new Pageable(currentPageUsers, pageSizeUsers));
        int maxPageUsers = (int) Math.ceil((double) pageUsers.getTotalElementCount() / pageSizeUsers) - 1;
        if (maxPageUsers == -1) maxPageUsers = 0;
        if (currentPageUsers > maxPageUsers) {
            currentPageUsers = maxPageUsers;
            pageUsers = socialNetwork.findAllUsers(new Pageable(currentPageUsers, pageSizeUsers));
        }
        int totalNumberOfElementsUsers = pageUsers.getTotalElementCount();
        previousBtnUsers.setDisable(currentPageUsers == 0);
        firstPageBtnUsers.setDisable(currentPageUsers == 0);
        nextBtnUsers.setDisable((currentPageUsers+1)*pageSizeUsers>=totalNumberOfElementsUsers);
        lastPageBtnUsers.setDisable((currentPageUsers+1)*pageSizeUsers>=totalNumberOfElementsUsers);

        for(User users:pageUsers.getElementsOnPage())
            userObservableList.add(users);

        listUsers.setItems(userObservableList);
        pageNumberUsers.setText((currentPageUsers + 1) + "/" + (maxPageUsers + 1));

        //Friendships

        Page<Friendship> pageFriendships = socialNetwork.findAllUserFriends(new Pageable(currentPageFriendships, pageSizeFriendships), this.loggedUser);
        int maxPageFriendships = (int) Math.ceil((double) pageFriendships.getTotalElementCount() / pageSizeFriendships) - 1;
        if (maxPageFriendships == -1) maxPageFriendships = 0;
        if (currentPageFriendships > maxPageFriendships) {
            currentPageFriendships = maxPageFriendships;
            pageFriendships = socialNetwork.findAllFriendships(new Pageable(currentPageFriendships, pageSizeFriendships));
        }
        int totalNumberOfElementsFriendships = pageFriendships.getTotalElementCount();

        previousBtnFriendships.setDisable(currentPageFriendships == 0);
        firstPageBtnFriendships.setDisable(currentPageFriendships == 0);
        nextBtnFriendships.setDisable((currentPageFriendships + 1) * pageSizeFriendships >= totalNumberOfElementsFriendships);
        lastPageBtnFriendships.setDisable((currentPageFriendships + 1) * pageSizeFriendships >= totalNumberOfElementsFriendships);

        previousBtnFriendships2.setDisable(currentPageFriendships == 0);
        firstPageBtnFriendships2.setDisable(currentPageFriendships == 0);
        nextBtnFriendships2.setDisable((currentPageFriendships + 1) * pageSizeFriendships >= totalNumberOfElementsFriendships);
        lastPageBtnFriendships2.setDisable((currentPageFriendships + 1) * pageSizeFriendships >= totalNumberOfElementsFriendships);


        for (Friendship friendship : pageFriendships.getElementsOnPage())
                friendshipObservableList.add(friendship);

        listFriendships.setItems(friendshipObservableList);
        listFriendships2.setItems(friendshipObservableList);
        pageNumberFriendships.setText((currentPageFriendships + 1) + "/" + (maxPageFriendships + 1));
        pageNumberFriendships2.setText((currentPageFriendships + 1) + "/" + (maxPageFriendships + 1));


        // Friend Requests
        Page<Friendship> pageFriendRequests = socialNetwork.findAllUserFriendRequests(new Pageable(currentPageFriendRequests, pageSizeFriendRequests), this.loggedUser);

        int maxPageFriendRequests = (int) Math.ceil((double) pageFriendRequests.getTotalElementCount() / pageSizeFriendRequests) - 1;
        if (maxPageFriendRequests == -1) maxPageFriendRequests = 0;
        if (currentPageFriendRequests > maxPageFriendRequests ) {
            currentPageFriendRequests = maxPageFriendRequests;
            pageFriendRequests = socialNetwork.findAllFriendRequests(new Pageable(currentPageFriendRequests, pageSizeFriendRequests));
        }

        int totalNumberOfElementsFriendRequests = pageFriendRequests.getTotalElementCount();

        previousBtnFriendRequests.setDisable(currentPageFriendRequests == 0);
        firstPageBtnFriendRequests.setDisable(currentPageFriendRequests == 0);
        nextBtnFriendRequests.setDisable((currentPageFriendRequests + 1) * pageSizeFriendRequests >= totalNumberOfElementsFriendRequests);
        lastPageBtnFriendRequests.setDisable((currentPageFriendRequests + 1) * pageSizeFriendRequests >= totalNumberOfElementsFriendRequests);

        for (Friendship friendship : pageFriendRequests.getElementsOnPage()) {
            friendRequestObservableList.add(friendship);
        }
        listFriendRequests.setItems(friendRequestObservableList);
        pageNumberFriendRequests.setText((currentPageFriendRequests + 1) + "/" + (maxPageFriendRequests + 1));

    }

    public void onPreviousUsers(MouseEvent mouseEvent){
        currentPageUsers--;
        initApp();
    }

    public void onNextUsers(MouseEvent mouseEvent){
        currentPageUsers++;
        initApp();
    }

    public void onFirstUsers(MouseEvent mouseEvent){
        currentPageUsers=0;
        initApp();
    }

    public void onLastUsers(MouseEvent mouseEvent){
        Page<User> pageUsers=socialNetwork.findAllUsers(new Pageable(currentPageUsers, pageSizeUsers));
        currentPageUsers=(int) Math.ceil((double) pageUsers.getTotalElementCount() / pageSizeUsers)-1;
        initApp();
    }

    public void setNoElementsOnPageUsers(KeyEvent key) {
        if (key.getCode().equals(KeyCode.ENTER)) {
            try {
                if(!Objects.equals(noElementsOnPageUsers.getText(), "0"))
                    pageSizeUsers = Integer.parseInt(noElementsOnPageUsers.getText());
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("!!!");
                errorAlert.setContentText("Something wrong");
                errorAlert.showAndWait();
            }
            initApp();
        }
    }

    public void onPreviousFriendships(MouseEvent mouseEvent) {
        currentPageFriendships--;
        initApp();
    }

    public void onNextFriendships(MouseEvent mouseEvent) {
        currentPageFriendships++;
        initApp();
    }

    public void onFirstFriendships(MouseEvent mouseEvent) {
        currentPageFriendships = 0;
        initApp();
    }

    public void onLastFriendships(MouseEvent mouseEvent) {
        Page<Friendship> pageFriendships = socialNetwork.findAllFriendships(new Pageable(currentPageFriendships, pageSizeFriendships));
        currentPageFriendships = (int) Math.ceil((double) pageFriendships.getTotalElementCount() / pageSizeFriendships) - 1;
        initApp();
    }

    public void setNoElementsOnPageFriendships(KeyEvent key) {
        if (key.getCode().equals(KeyCode.ENTER)) {
            try {
                if (!Objects.equals(noElementsOnPageFriendships.getText(), "0"))
                    pageSizeFriendships = Integer.parseInt(noElementsOnPageFriendships.getText());
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("!!!");
                errorAlert.setContentText("Something wrong");
                errorAlert.showAndWait();
            }
            initApp();
        }
    }

    public void setNoElementsOnPageFriendships2(KeyEvent key) {
        if (key.getCode().equals(KeyCode.ENTER)) {
            try {
                if (!Objects.equals(noElementsOnPageFriendships2.getText(), "0"))
                    pageSizeFriendships = Integer.parseInt(noElementsOnPageFriendships2.getText());
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("!!!");
                errorAlert.setContentText("Something wrong");
                errorAlert.showAndWait();
            }
            initApp();
        }
    }

    public void onPreviousFriendRequests(MouseEvent mouseEvent) {
        currentPageFriendRequests--;
        initApp();
    }

    public void onNextFriendRequests(MouseEvent mouseEvent) {
        currentPageFriendRequests++;
        initApp();
    }

    public void setNoElementsOnPageFriendRequests(KeyEvent key) {
        if (key.getCode().equals(KeyCode.ENTER)) {
            try {
                if(!Objects.equals(noElementsOnPageFriendRequests.getText(), "0"))
                    pageSizeFriendRequests = Integer.parseInt(noElementsOnPageFriendRequests.getText());
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("!!!");
                errorAlert.setContentText("Something wrong");
                errorAlert.showAndWait();
            }
            initApp();
        }
    }

    public void onLastFriendRequests(MouseEvent mouseEvent) {
        Page<Friendship> pageFriendRequests = socialNetwork.findAllFriendRequests(new Pageable(currentPageFriendRequests, pageSizeFriendRequests));
        currentPageFriendRequests = (int) Math.ceil((double) pageFriendRequests.getTotalElementCount() / pageSizeFriendRequests) - 1;
        initApp();
    }

    public void onFirstFriendRequests(MouseEvent mouseEvent) {
        currentPageFriendRequests = 0;
        initApp();
    }


    public void sendMessage() {
        User fromUser = loggedUser;
        User toUser = choiceboxId1.getValue();
        String textMessage = message.getText();
        if(fromUser.getId() ==toUser.getId())
        {
            MessageUser.showErrorMessage(null,  "Can not send messages to yourself");
            return;

        }

        if (fromUser == null || toUser == null || textMessage.isEmpty()) {
            MessageUser.showErrorMessage(null, "Please select both users and enter a message.");
            return;
        }


        Long fromUserId = fromUser.getId();
        Long toUserId = toUser.getId();

        boolean success = socialNetwork.addMessage(fromUserId, toUserId, textMessage);

        if (success) {
            List<Message> updatedMessages = socialNetwork.getMessages(fromUserId, toUserId);
            listMessages.getItems().clear();
            listMessages.getItems().addAll(updatedMessages);

            MessageUser.showMessage(null, Alert.AlertType.INFORMATION, "Message Sent", "Your message has been sent to " + toUser.getFirstName() + ".");
        } else {
            MessageUser.showErrorMessage(null, "Failed to send message.");
        }
    }

    public void searchMessages() {
        Long id1=loggedUser.getId();
        Long id2= choiceboxId1.getValue().getId();
        List<Message> updatedMessages = socialNetwork.getMessages(id1, id2);
        listMessages.getItems().clear();
        listMessages.getItems().addAll(updatedMessages);
    }

    public void replyMessage(MouseEvent mouseEvent) {
        if (listMessages.getSelectionModel().getSelectedItem() != null) {
            Message msg = listMessages.getSelectionModel().getSelectedItem();

            Long to =loggedUser.getId();
            Long from = choiceboxId1.getValue().getId();
            String replyText = msg.getMessage()+" -replied with- "+message.getText();

            if (!socialNetwork.addMessage(from,to, replyText)) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("!!!");
                errorAlert.setContentText("Something wrong");
                errorAlert.showAndWait();
            } else {
                List<Message> updatedMessages = socialNetwork.getMessages(from, to);
                listMessages.getItems().clear();
                listMessages.getItems().addAll(updatedMessages);
                initApp();
            }

            message.clear();
        }
    }


    /**
     * Handles the event when a user is added.
     * If no user is logged in, it adds a new user to the social network.
     * If a user is logged in, it shows an error message.
     *
     * @param mouseEvent the event triggered by the user clicking the button
     */
    public void addUser(MouseEvent mouseEvent) {
            String firstName = textFieldFirstName.getText();
            String lastName = textFieldLastName.getText();
            String password=textFieldPassword.getText();

            try {
                Long newId=socialNetwork.getNewUserId();
                User user = new User(newId, firstName, lastName,password);
                socialNetwork.addUser(user);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Added");
                alert.setHeaderText(null);
                alert.setContentText("User " + firstName + " " + lastName + " added successfully!");
                alert.showAndWait();

                textFieldFirstName.clear();
                textFieldLastName.clear();
            } catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Cannot add User...");
                alert.showAndWait();
            }

            initApp();


    }


    /**
     * Handles the event when a user is deleted.
     * If no user is logged in, it removes a user from the social network.
     * If a user is logged in, it shows an error message.
     *
     * @param mouseEvent the event triggered by the user clicking the button
     */
    public void deleteUser(MouseEvent mouseEvent) {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();

        try {
            for(User u:socialNetwork.getUsers()) {
                if(u.getFirstName().equals(firstName) && u.getLastName().equals(lastName))
                    socialNetwork.removeUser(u.getId());
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Removed");
            alert.setHeaderText(null);
            alert.setContentText("User deleted successfully!");
            alert.showAndWait();

            textFieldFirstName.clear();
            textFieldLastName.clear();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: Cannot delete User...");
            alert.showAndWait();
        }

        initApp();

    }


    /**
     * Handles the event when a user clicks the button to delete a friendship.
     * It removes the selected friendship if the logged user has permission to do so.
     * If successful, a confirmation alert is shown, otherwise, an error alert is displayed.
     *
     * @param mouseEvent the event triggered by the user clicking the delete button
     */
    public void deleteFriendship(MouseEvent mouseEvent) {
        String firstName1 = textFieldFirstName1.getText();
        String lastName1 = textFieldLastName1.getText();
        String firstName2 = textFieldFirstName2.getText();
        String lastName2 = textFieldLastName2.getText();

        try {
            User user1 = socialNetwork.getUserByName(firstName1, lastName1);
            User user2 = socialNetwork.getUserByName(firstName2, lastName2);

            socialNetwork.removeFriendship(user1.getId(), user2.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Friendship removed");
            alert.setHeaderText(null);
            alert.setContentText("Friendship between " + firstName1 + " " + lastName1 +
                    " and " + firstName2 + " " + lastName2 + " removed successfully!");
            alert.showAndWait();

            textFieldFirstName1.clear();
            textFieldLastName1.clear();
            textFieldFirstName2.clear();
            textFieldLastName2.clear();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: Cannot delete Friendship... " + exception.getMessage());
            alert.showAndWait();
        }

        initApp();
    }


    /**
     * Handles the event when a user clicks the button to accept a friend request.
     * It accepts the friend request if the logged user has permission to do so,
     * and updates the friendship status accordingly. A success or error alert is shown.
     *
     * @param mouseEvent the event triggered by the user clicking the accept button
     */
    public void acceptFriendRequest(MouseEvent mouseEvent) {
        String firstName1 = textFieldFirstName1.getText();
        String lastName1 = textFieldLastName1.getText();
        String firstName2 = textFieldFirstName2.getText();
        String lastName2 = textFieldLastName2.getText();

        try {

            User user1 = socialNetwork.getUserByName(firstName1, lastName1);
            User user2 = socialNetwork.getUserByName(firstName2, lastName2);

            Friendship friendship=null;
            for(Friendship f:socialNetwork.getFriendships()){
                if(f.getIdUser1().equals(user1.getId()) && f.getIdUser2().equals(user2.getId()))
                    friendship=f;
            }
            if(friendship==null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Friend request denied");
                alert.setHeaderText(null);
                alert.setContentText("Friendship doesn't exist!");
                alert.showAndWait();

            }

            socialNetwork.manageFriendRequest(friendship, FriendshipRequest.APPROVED);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Friend Request Accepted");
            alert.setHeaderText(null);
            alert.setContentText("Friendship between " + firstName1 + " " + lastName1 +
                    " and " + firstName2 + " " + lastName2 + " approved!");
            alert.showAndWait();

            textFieldFirstName1.clear();
            textFieldLastName1.clear();
            textFieldFirstName2.clear();
            textFieldLastName2.clear();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: Cannot accept Friend Request... " + exception.getMessage());
            alert.showAndWait();
        }

        initApp();
    }


    /**
     * Handles the event when a user clicks the button to reject a friend request.
     * It rejects the friend request if the logged user has permission to do so,
     * and updates the friendship status accordingly. A success or error alert is shown.
     *
     * @param mouseEvent the event triggered by the user clicking the reject button
     */
    public void rejectFriendRequest(MouseEvent mouseEvent) {

        try {
            String firstName1 = textFieldFirstName1.getText();
            String lastName1 = textFieldLastName1.getText();
            String firstName2 = textFieldFirstName2.getText();
            String lastName2 = textFieldLastName2.getText();


            User user1=socialNetwork.getUserByName(firstName1, lastName1);
            User user2=socialNetwork.getUserByName(firstName2, lastName2);
            Friendship friendship=null;
            for(Friendship f:socialNetwork.getFriendships()){
                if(f.getIdUser1().equals(user1.getId()) && f.getIdUser2().equals(user2.getId())){
                    friendship=f;
                }
            }
            if(friendship!=null)
                socialNetwork.manageFriendRequest(friendship, FriendshipRequest.REJECTED);
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Friend request doesn't exist!...");
                alert.showAndWait();
            }

            textFieldFirstName1.clear();
            textFieldLastName1.clear();
            textFieldFirstName2.clear();
            textFieldLastName2.clear();
            initApp();

        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: Cannot decline Friend Request...");
            alert.showAndWait();
        }

        initApp();
    }

    /**
     * Handles the event when a user clicks the button to send a friend request.
     * It sends a friend request if no existing friendship exists and the logged user has permission to send the request.
     * A success or error alert is shown based on the result.
     *
     * @param mouseEvent the event triggered by the user clicking the send button
     */
    public void sendFriendRequest(MouseEvent mouseEvent) {

        try {
            String firstName1 = textFieldFirstName1.getText();
            String lastName1 = textFieldLastName1.getText();
            String firstName2 = textFieldFirstName2.getText();
            String lastName2 = textFieldLastName2.getText();
            User user1=socialNetwork.getUserByName(firstName1, lastName1);
            User user2=socialNetwork.getUserByName(firstName2, lastName2);
            if(loggedUser.getId().equals(user1.getId())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Friend Request sent!");
                alert.showAndWait();
            }
            if(loggedUser.getId().equals(user2.getId())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("You Received a Friend Request!");
                alert.showAndWait();
            }

            Friendship friendship=null;
            for(Friendship f:socialNetwork.getFriendships()){
                if(f.getIdUser1().equals(user1.getId()) && f.getIdUser2().equals(user2.getId())){
                    friendship=f;
                }
            }
            if (friendship == null) {
                socialNetwork.createFriendshipRequest(user1.getId(), user2.getId());
                textFieldFirstName1.clear();
                textFieldLastName1.clear();
                textFieldFirstName2.clear();
                textFieldLastName2.clear();
                initApp();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Friendship exists...");
                alert.showAndWait();
                return;
            }
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: Cannot send Friend Request...");
            alert.showAndWait();
        }

        initApp();
    }

    /**
     * Handles the event when a user clicks the button to delete a friend request.
     * It removes the friend request if the logged user has permission to do so,
     * and shows a success or error alert based on the result.
     *
     * @param mouseEvent the event triggered by the user clicking the delete button
     */
    public void deleteFriendRequest(MouseEvent mouseEvent) {

        try {
            String firstName1 = textFieldFirstName1.getText();
            String lastName1 = textFieldLastName1.getText();
            String firstName2 = textFieldFirstName2.getText();
            String lastName2 = textFieldLastName2.getText();
            User user1=socialNetwork.getUserByName(firstName1, lastName1);
            User user2=socialNetwork.getUserByName(firstName2, lastName2);


            Friendship friendship=null;
            for(Friendship f:socialNetwork.getFriendships()){
                if(f.getIdUser1().equals(user1.getId()) && f.getIdUser2().equals(user2.getId())){
                    friendship=f;
                }
            }
            if (friendship != null && friendship.getFriendshipRequestStatus() != FriendshipRequest.APPROVED) {
                socialNetwork.removeFriendship(user1.getId(), user2.getId());
                initApp();
            }

        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: Cannot delete Friend Request...");
            alert.showAndWait();
        }

        initApp();
    }


}