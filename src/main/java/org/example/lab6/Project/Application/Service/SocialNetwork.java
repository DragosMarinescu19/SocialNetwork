package org.example.lab6.Project.Application.Service;


import Repository.DataBase.FriendRequestPagingRepository;
import Repository.Pagination.PagingRepository;
import org.example.lab6.Project.Application.Domain.Validators.ValidationException;
import Repository.DataBase.FriendshipDBRepository;
import Repository.DataBase.UserDBRepository;
import Repository.DataBase.MessagesDBRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.example.lab6.Project.Application.Domain.*;
import org.example.lab6.Project.Application.Enum.*;
import org.example.lab6.Project.Application.Service.*;
import org.example.lab6.Project.Application.Messages.MessageUser;
import Repository.*;
import Repository.Pagination.*;
import static org.example.lab6.Project.Application.Messages.MessageUser.showErrorMessage;

/**
 * A service class that provides functionalities for managing a social network,
 * including user and friendship operations.
 */

public class SocialNetwork {

    protected final PagingRepository<Long, User> repositoryUser;
    protected final FriendRequestPagingRepository<Long, Friendship> repositoryFriendship;

    private final MessagesDBRepository messagesDBRepository;


    public SocialNetwork(PagingRepository<Long, User> repositoryUser, FriendRequestPagingRepository<Long, Friendship> repositoryFriendship,  MessagesDBRepository messagesDBRepository) {
        this.repositoryUser = repositoryUser;
        this.repositoryFriendship = repositoryFriendship;
        this.messagesDBRepository = messagesDBRepository;
    }

    /**
     * @return all Users from the social network
     */
    public Iterable<User> getUsers() {
        return repositoryUser.findAll();
    }

    /**
     * @param id Id of the user that needs to be found
     * @return User
     */
    public User findUser(Long id) {
        return repositoryUser.findOne(id).orElseThrow(() -> new ValidationException("User doesn't exist!"));
    }


    /**
     * @param user Adds the user to the map.repository
     */
    public User addUser(User user) {
        user.setPassword(User.hashPassword(user.getPassword()));
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            throw new ValidationException("User is invalid!");
        }
        repositoryUser.save(user);
        return user;
    }

    public void saveURL(User user) {
        repositoryUser.save(user);
    }

    /**
     * @return All friendships from map.repository
     */
    public Iterable<Friendship> getFriendships() {
        return repositoryFriendship.findAll();
    }

    public List<User> getListFriends(User user) {
        List<User> friends = new ArrayList<>();
        getFriendships().forEach(friendship -> {
            if (friendship.getIdUser1().equals(user.getId())) {
                friends.add(findUser(friendship.getIdUser2()));
            } else if (friendship.getIdUser2().equals(user.getId())) {
                friends.add(findUser(friendship.getIdUser1()));
            }
        });
        return friends;
    }


    /**
     * @param id - id of the user that needs to be removed
     * @return user
     */
    public User removeUser(Long id) {
        User u = null;
        try {
            u = repositoryUser.findOne(id).orElseThrow(() -> new ValidationException("User doesn't exist!"));
            if (u == null) {
                throw new IllegalArgumentException("User doesn't exist!");
            }
            repositoryUser.delete(id);

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid user! ");
        } catch (ValidationException v) {
            System.out.println();
        }
        return u;
    }


    public Long getNewFriendshipId() {
        Long id = 0L;
        for (Friendship f : repositoryFriendship.findAll()) {
            id = f.getId();
        }
        id++;
        return id;
    }

    public Long getNewUserId(){
        Long id=0L;
        for(User u:repositoryUser.findAll())
            id=u.getId();
        id++;
        return id;
    }
    public User getUserPass(String password){
        for(User u:repositoryUser.findAll()){
            if(u.getPassword().equals(u.hashPassword(password)))
                return u;
        }
        return null;
    }
    public User getUserByName(String firstname,String lastname){
        for(User u:repositoryUser.findAll()){
            if(u.getFirstName().equals(firstname) && u.getLastName().equals(lastname))
                return u;
        }
        return null;
    }

    public void addFriendship(Friendship friendship) {
        if (getFriendships() != null) {
            getFriendships().forEach(f -> {
                if (f.getIdUser1().equals(friendship.getIdUser1()) && f.getIdUser2().equals(friendship.getIdUser2())) {
                    throw new ValidationException("The friendship already exist! ");
                }
            });
            if (repositoryUser.findOne(friendship.getIdUser1()).isEmpty() || repositoryUser.findOne(friendship.getIdUser2()).isEmpty()) {
                throw new ValidationException("User doesn't exist! ");
            }
            if (friendship.getIdUser1().equals(friendship.getIdUser2()))
                throw new ValidationException("IDs can't be the same!!! ");
        }

        friendship.setId(getNewFriendshipId());
        repositoryFriendship.save(friendship);

    }

    public void removeFriendship(Long id1, Long id2) {
        Long id = 0L;
        for (Friendship f : repositoryFriendship.findAll()) {
            if ((f.getIdUser1().equals(id1) && f.getIdUser2().equals(id2)) || (f.getIdUser1().equals(id2) && f.getIdUser2().equals(id1)))
                id = f.getId();
        }
        if (id == 0L)
            throw new IllegalArgumentException("The friendship doesn't exist!");
        repositoryFriendship.delete(id);
    }

    public void createFriendshipRequest(Long id1, Long id2) {
        Friendship friendship = new Friendship(id1, id2, LocalDateTime.now());
        addFriendship(friendship);
    }

    public void manageFriendRequest(Friendship friendship, FriendshipRequest friendshipRequest) {
        try {
            if (repositoryFriendship.findOne(friendship.getId()).isEmpty()) {
                throw new Exception("Friendship doesn't exist!");
            } else if (friendship.getFriendshipRequestStatus() != FriendshipRequest.PENDING) {
                showErrorMessage(null, "The request must be PENDING in order to APPROVE/DECLINE it");
                throw new Exception("Friendship is not PENDING!");
            }
            friendship.setFriendshipRequestStatus(friendshipRequest);
            repositoryFriendship.update(friendship);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Messages

    public boolean addMessage(Long id_from, Long id_to, String message) {
        try {
            User from = findUser(id_from);
            User to = findUser(id_to);

            Message msg = new Message(from, Collections.singletonList(to), message);
            messagesDBRepository.save(msg);

            List<Message> messagesBetweenUsers = getMessages(id_to, id_from);
            if (messagesBetweenUsers.size() > 1) {
                Message oldReplyMessage = messagesBetweenUsers.get(messagesBetweenUsers.size() - 2);
                Message newReplyMessage = messagesBetweenUsers.get(messagesBetweenUsers.size() - 1);
                oldReplyMessage.setReplyTo(newReplyMessage);
                messagesDBRepository.update(oldReplyMessage);
            }

            return true;
        } catch (ValidationException ve) {
            System.out.println("eroare user");
        } catch (Exception ex) {
            System.out.println("eroare creare mesaj!");
        }
        return false;
    }

    public ArrayList<Message> getMessages(Long id1, Long id2) {
        User user1 = findUser(id1);
        User user2 = findUser(id2);

        Collection<Message> messages = (Collection<Message>) messagesDBRepository.findAll();
        return messages.stream()
                .filter(msg -> ((msg.getFrom().getId().equals(id1)) && msg.getTo().get(0).getId().equals(id2)) ||
                        (msg.getFrom().getId().equals(id2) && msg.getTo().get(0).getId().equals(id1)))
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toCollection(ArrayList::new));


    }


    public Page<User> findAllUsers(Pageable pageable) {
        return repositoryUser.findall(pageable);
    }


    public Page<Friendship> findAllFriendships(Pageable pageable) {
        return repositoryFriendship.findall(pageable);
    }


    public Page<Friendship> findAllUserFriends(Pageable pageable, User user) {
        Page<Friendship> pgF = repositoryFriendship.findAllUserFriends(pageable, user.getId());
        return pgF;
    }

    public Page<Friendship> findAllFriendRequests(Pageable pageable) {
        return repositoryFriendship.findAllFriendRequests(pageable);
    }


    public Page<Friendship> findAllUserFriendRequests(Pageable pageable, User user) {
        Page<Friendship> pgF = repositoryFriendship.findAllUserFriendRequests(pageable, user.getId());
        /*for (Friendship f: pgF.getElementsOnPage()) {
            f.setNameUser1(repositoryUser.findOne(f.getIdUser1()).get().getFirstName() + " " +
                    repositoryUser.findOne(f.getIdUser1()).get().getLastName());
            f.setNameUser2(repositoryUser.findOne(f.getIdUser2()).get().getFirstName() + " " +
                    repositoryUser.findOne(f.getIdUser2()).get().getLastName());
        }*/
        return pgF;
    }



    public Iterable<User> findAllUsers() {
        return repositoryUser.findAll();
    }


    public Iterable<Friendship> findAllFriendships() {
        return repositoryFriendship.findAll();
    }

}
