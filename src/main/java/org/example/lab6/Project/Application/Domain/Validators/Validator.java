package org.example.lab6.Project.Application.Domain.Validators;


public interface Validator<T> {
    //este template pentru a putea o data sa verificat <User> si apoi <Friendship>
    public void validate(T value) throws ValidationException;
}
