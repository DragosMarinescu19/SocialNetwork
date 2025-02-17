package org.example.lab6.Project.Application.Domain;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String password;
    private String profileImageUrl;
    ArrayList<User> friends;


    public User(Long id,String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.profileImageUrl=null;
        friends = new ArrayList<>();
        this.id = id;
    }

    public User(Long id,String firstName, String lastName, String password,String profileImageUrl)  {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.profileImageUrl=profileImageUrl;
        friends = new ArrayList<>();
        this.id = id;
    }
    public User(Long id,String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl=null;
        friends = new ArrayList<>();
        this.id = id;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }
    public void addFriend(User user) {
        friends.add(user);
    }
    public void removeFriend(User user) {
        friends.remove(user);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Eroare la hash-ul parolei: " + e.getMessage());
        }
    }/*
    public String hashPassword(String password){
        String newpassword="";
        for(int i=password.length()-1;i>=0;i--)
            newpassword+=password.charAt(i);
        return newpassword;
    }*/


    @Override
    public String toString() {
        return "firstName='" + firstName + '\'' +
                ", lastName='" + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (!Objects.equals(getFirstName(), user.getFirstName())) return false;
        if (!Objects.equals(getLastName(), user.getLastName())) return false;
        return Objects.equals(getFriends(), user.getFriends());

    }


    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}
