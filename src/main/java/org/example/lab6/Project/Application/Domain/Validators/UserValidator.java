package org.example.lab6.Project.Application.Domain.Validators;

import org.example.lab6.Project.Application.Domain.User;

public class UserValidator implements Validator<User>{

    @Override
    public void validate(User value) throws ValidationException {
        String errorMessage = "";

        if(value.getFirstName().isEmpty())
            errorMessage += "First Name is empty";
        if(value.getLastName().isEmpty())
            errorMessage += "Last Name is empty";
        if(!errorMessage.isEmpty())
            throw new ValidationException(errorMessage);
    }
}
