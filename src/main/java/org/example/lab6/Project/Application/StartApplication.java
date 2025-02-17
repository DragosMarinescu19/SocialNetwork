package org.example.lab6.Project.Application;

import Repository.DataBase.FriendRequestPagingRepository;
import Repository.DataBase.MessagesDBRepository;
import Repository.Pagination.PagingRepository;
import org.example.lab6.Project.Application.Domain.Friendship;
import org.example.lab6.Project.Application.Domain.User;
import org.example.lab6.Project.Application.Domain.Validators.FriendshipValidator;
import org.example.lab6.Project.Application.Domain.Validators.UserValidator;
import Repository.DataBase.FriendshipDBRepository;
import Repository.DataBase.UserDBRepository;
import org.example.lab6.Project.Application.Service.SocialNetwork;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class StartApplication extends Application {

    SocialNetwork socialNetwork;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        PagingRepository<Long, User> userRepo = new UserDBRepository(new UserValidator());
        FriendRequestPagingRepository<Long, Friendship> friendshipRepo = new FriendshipDBRepository(new FriendshipValidator());
        MessagesDBRepository messagesDBRepository = new MessagesDBRepository(userRepo);

        socialNetwork = new SocialNetwork(userRepo, friendshipRepo,messagesDBRepository);

        initView(primaryStage);
        primaryStage.setWidth(600);
        primaryStage.setTitle("SocialNetwork");
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/login.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);

        LogInController controller = fxmlLoader.getController();
        controller.setSocialNetwork(socialNetwork);
        controller.initialize();
    }


}