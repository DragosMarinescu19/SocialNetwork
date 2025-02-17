package org.example.lab6.Project.Application.Domain.Validators;


import org.example.lab6.Project.Application.Domain.Friendship;
import org.example.lab6.Project.Application.Domain.User;
import Repository.DataBase.UserDBRepository;
import Repository.DataBase.FriendshipDBRepository;

import java.util.Optional;



public class FriendshipValidator implements Validator<Friendship> {

    private FriendshipDBRepository repo;


    @Override
    public void validate(Friendship entity) throws ValidationException {

        Optional<Friendship> u1 = repo.findOne(entity.getIdUser1());
        Optional<Friendship> u2 = repo.findOne(entity.getIdUser2());

        if (entity.getIdUser1() == null || entity.getIdUser2() == null)
            throw new ValidationException("The id can't be null! ");
        if (u1.isEmpty() || u2.isEmpty())
            throw new ValidationException("The id doesn't exist! ");
    }
}
