package org.example.lab6.Project.Application.Service;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SocialCommunities {

    SocialNetwork socialNetwork;
    HashMap<Long, List<Long>> adjList;

    public SocialCommunities(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    void DFS(Long v, HashMap<Long, Boolean> visited) {
        visited.put(v, true);//inseram nodul v ca si vizitat
        System.out.println(v + " " + this.socialNetwork.findUser(v).getFirstName() + " " + this.socialNetwork.findUser(v).getLastName());
        if (adjList.containsKey(v))
            adjList.get(v).stream().filter(x -> !visited.containsKey(x)).forEach(x -> DFS(x, visited));
        //folosim stream pentru a putea face filter,filter este o operatie care ia un predicat(o functie care ret un boolean)
        //si pastreza elem pt care rpedicatul este true,verifica daca nodul x adica vecinul nu a fost vizitat anterior
        //rezultatul va fi un stream cu doar acele noduri adiacente care nu a fost inca vizitate
    }//get(v) returneaza o lista cu vecinii lui v

    public int connectedCommunities() {
        // creeaza o lista de adiacenta pentru user si pr sai
        adjList = new HashMap<Long, List<Long>>();

        socialNetwork.getUsers().forEach(user -> {
            List<Long> friends = new ArrayList<>();
            socialNetwork.getFriendships().forEach(friendship -> {
                if (friendship.getIdUser1().equals(user.getId()))
                    friends.add(friendship.getIdUser2());
                if (friendship.getIdUser2().equals(user.getId()))
                    friends.add(friendship.getIdUser1());
            });
            if (!friends.isEmpty())
                this.adjList.put(user.getId(), friends);
        });

        // lista cu id-urile userilor
        List<Long> ids = new ArrayList<>();
        socialNetwork.getUsers().forEach(user -> {ids.add(user.getId());});

        AtomicInteger nrOfCommunities = new AtomicInteger(0);
        HashMap<Long, Boolean> visited = new HashMap<Long, Boolean>();

        ids.stream().filter(v-> !visited.containsKey(v)).forEach(v -> {
            DFS(v,visited);
            nrOfCommunities.incrementAndGet();//creste cu 1
            System.out.println();
        });
        return nrOfCommunities.get();
        /*
        for(Long v:ids) {
            if (!visited.containsKey(v)) {
                DFS(v,visited);
                nrOfCommunities.incrementAndGet();//creste cu 1
                System.out.println();
            }
        }
         */
    }

    public List<Long> mostSocialCommunities() {
        //creez o lista de adiacenta pt useri si pr lor
        adjList=new HashMap<Long,List<Long>>();
        AtomicReference<List<Long>> max= new AtomicReference<>(new ArrayList<>());
        //pentru a o putea folosi in functyia lambda

        socialNetwork.getUsers().forEach(user -> {
            List<Long> friends = new ArrayList<>();
            socialNetwork.getFriendships().forEach(friendship -> {
                if(friendship.getIdUser1().equals(user.getId()))
                    friends.add(friendship.getIdUser2());
                if(friendship.getIdUser2().equals(user.getId()))
                    friends.add(friendship.getIdUser1());
            });
            if(!friends.isEmpty()) {
                this.adjList.put(user.getId(), friends);
                if(max.get().size()<friends.size()+1) {
                    max.set(friends);
                    max.get().add(user.getId());
                }
            }
        });

        return max.get();
    }
}
