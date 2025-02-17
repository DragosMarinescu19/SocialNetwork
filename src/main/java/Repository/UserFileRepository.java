package Repository;


import org.example.lab6.Project.Application.Domain.*;
import org.example.lab6.Project.Application.Domain.Validators.Validator;

import java.util.*;
public class UserFileRepository extends AbstractFileRepository<Long,User> {

    public UserFileRepository(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    @Override
    public User extractEntity(List<String> attributes){
        User user=new User(1L,attributes.get(1),attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }

    @Override
    protected String createEntityAsString(User user) {
        return user.getId()+" "+user.getFirstName()+" "+user.getLastName();
    }
}
