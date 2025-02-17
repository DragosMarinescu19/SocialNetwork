package org.example.lab6.Project.Application;

import org.example.lab6.Project.Application.Domain.Validators.FriendshipValidator;
import org.example.lab6.Project.Application.Domain.Validators.UserValidator;
import Repository.DataBase.FriendshipDBRepository;
import Repository.DataBase.UserDBRepository;
import org.example.lab6.Project.Application.Service.*;
import org.example.lab6.Project.Application.UI.*;

public class Main {
    public static void main(String[] args) {
/*
        //InMemoryRepository<Long,User> repoUser=new InMemoryRepository<>(new UserValidator());
        //InMemoryRepository<Long, Friendship> repoFriendship = new InMemoryRepository<>(new FriendshipValidator(repoUser));
        //UserFileRepository repoUser=new UserFileRepository("C:\\Users\\marin\\OneDrive\\Desktop\\MAP\\lab3+\\lab3\\src\\Users.csv",new UserValidator());
        //FriendshipFileRepository repoFriendship = new FriendshipFileRepository("C:\\Users\\marin\\OneDrive\\Desktop\\MAP\\lab3+\\lab3\\src\\Friendships.csv", new FriendshipValidator(repoUser));

        UserDBRepository repoUser=new UserDBRepository(new UserValidator());
        FriendshipDBRepository repoFriendship= new FriendshipDBRepository(new FriendshipValidator(repoUser));

        SocialNetwork socialNetwork=new SocialNetwork(repoUser,repoFriendship);
        Console ui=new Console(socialNetwork);

        /*User u1 = new User("Andrei", "Marcel");
        User u2 = new User("Marius", "Marian");
        User u3 = new User("Claudiu", "Borcea");
        User u4 = new User("Denis", "Dumbrava");
        User u5 = new User("Elena", "Mirel");
        User u6 = new User("Florentina", "Popa");
        User u7 = new User("Ana", "Popescu");
        User u8 = new User("George", "Alin");

        socialNetwork.addUser(u1);
        socialNetwork.addUser(u2);
        socialNetwork.addUser(u3);
        socialNetwork.addUser(u4);
        socialNetwork.addUser(u5);
        socialNetwork.addUser(u6);
        socialNetwork.addUser(u7);
        socialNetwork.addUser(u8);*/

        //ui.run();
    }
}