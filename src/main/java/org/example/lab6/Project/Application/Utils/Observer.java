package org.example.lab6.Project.Application.Utils;

public interface Observer<E extends Event> {
    void update(E e);
}
