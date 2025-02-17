package org.example.lab6.Project.Application.UI;



import org.example.lab6.Project.Application.Domain.Friendship;
import org.example.lab6.Project.Application.Domain.Validators.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import org.example.lab6.Project.Application.Service.SocialCommunities;
import org.example.lab6.Project.Application.Service.SocialNetwork;
import org.example.lab6.Project.Application.Domain.*;

public class Console {

    private SocialNetwork socialNetwork;
    private SocialCommunities socialCommunities;

    public Console(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
        this.socialCommunities = new SocialCommunities(socialNetwork);
    }

    void printMenu() {
        System.out.println("\t\t\tMENU\t\t\t");
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. Add friendship");
        System.out.println("4. Remove friendship");
        System.out.println("5. Print users");
        System.out.println("6. Print friendships");
        System.out.println("7. Connected Communities");
        System.out.println("8. Most Social Communities");
        System.out.println("0. EXIT");
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        boolean exit=false;
        while(!exit){
            printMenu();
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    addUser();
                    break;
                case 2:
                    removeUser();
                    break;
                case 3:
                    addFriendship();
                    break;
                case 4:
                    removeFriendships();
                    break;

                case 5:
                    printUsers();
                    break;
                case 6:
                    printFriendships();
                    break;
                case 7:
                    printConnectedCommunities();
                    break;
                case 8:
                    printMostSocialCommunity();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    exit=true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;


            }
        }
    }

    void printUsers(){
        System.out.println("Users:");
        for(User u:socialNetwork.getUsers())
            System.out.println("Id: "+u.getId()+" "+u.getFirstName()+" "+u.getLastName());
    }

    void addUser() {
        System.out.println("Add user");
        Scanner scan = new Scanner(System.in);
        System.out.println("Id: ");
        String var = scan.nextLine();
        System.out.println("First name: ");
        String firstName = scan.nextLine();
        System.out.println("Last name: ");
        String lastName = scan.nextLine();
        System.out.println("password: ");
        String password = scan.nextLine();
        try {
            Long id = Long.parseLong(var);
            socialNetwork.addUser(new User(id, firstName, lastName,password));
        } catch (ValidationException e) {
            System.out.println("Invalid user!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument");
        }

    }

    void removeUser() {
        printUsers();
        System.out.println("Remove user");
        Scanner scan = new Scanner(System.in);
        System.out.println("Id: ");
        String var = scan.nextLine();
        try {
            Long id = Long.parseLong(var);
            User user = socialNetwork.findUser(id);
            if (user == null) throw new ValidationException("User doesn,t exist!");
            socialNetwork.removeUser(id);
            System.out.println("User: " + user.getId() + " " + user.getFirstName() + " " + user.getLastName() + " was removed.");
        } catch (IllegalArgumentException e) {
            System.out.println("ID must be a number! It can't contain letters or symbols! ");
        } catch (ValidationException v) {
            System.out.println("User doesn't exist!");
        }
    }

    void printFriendships(){
        System.out.println("Friendships:");
        for(Friendship friend:socialNetwork.getFriendships())
            System.out.println(friend);
        /*for(User u:socialNetwork.getUsers()){
            System.out.println("Friends of user: " + u.getFirstName() + " " + u.getLastName() + " ( Number of friends: " + u.getFriends().size() + " )");
            if(u.getFriends()!=null){
                for(User friend:u.getFriends()){
                    System.out.println("ID: "+friend.getId()+" - FirstName - "+friend.getFirstName()+" - LastName - "+friend.getLastName());
                }
            }
        }*/
    }

    void addFriendship(){
        printFriendships();
        System.out.println("Add Friendship");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Id of the first user: ");
        String firstId=scanner.nextLine();
        System.out.print("Id of the second user: ");
        String secondId=scanner.nextLine();
        try{
            Long id1=0L,id2=0L;
            try{
                id1=Long.parseLong(firstId);
                id2=Long.parseLong(secondId);
            }catch(IllegalArgumentException e){
                System.out.println("ID must be a number");
            }
            socialNetwork.addFriendship(new Friendship(id1,id2, LocalDateTime.now()));

        }catch (ValidationException e){
            System.out.println("friendship is invalid");
        }catch(IllegalArgumentException e){
            System.out.println("Invalid argument");
        }
    }

    private void removeFriendships(){
        printFriendships();
        System.out.println("Remove Friendship");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Id of the first user: ");
        String firstId=scanner.nextLine();
        System.out.print("Id of the second user: ");
        String secondId=scanner.nextLine();
        try{
            Long id1=0L,id2=0L;
            try{
                id1=Long.parseLong(firstId);
                id2=Long.parseLong(secondId);
            }catch(IllegalArgumentException e){
                System.out.println("ID must be a number");
            }
            socialNetwork.removeFriendship(id1,id2);
        }catch(IllegalArgumentException e){
            System.out.println("Invalid arguments");
        }
    }

    private void printConnectedCommunities(){
        System.out.println("ConnectedCommunities:");
        int nrOfCom=socialCommunities.connectedCommunities();
        System.out.println("Number of Social Communities: " + nrOfCom);

    }

    private void printMostSocialCommunity(){
        System.out.println("Most Social Community:");
        List<Long> mostSocialCom=socialCommunities.mostSocialCommunities();
        for(Long value:mostSocialCom)
            System.out.println(value);
    }
}
