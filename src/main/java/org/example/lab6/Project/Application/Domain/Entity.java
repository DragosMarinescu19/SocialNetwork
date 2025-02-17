package org.example.lab6.Project.Application.Domain;
import java.io.*;
import java.util.*;
//clasa template
public class Entity<ID> implements Serializable{
    //clasa poate fi serializata
    //serializarea este procesul prin care un obiect poate fi transformat intr un flux de biti si permite salvarea
    //acstuia in fisier

    //clasa generica pentru a accepta orice tip de date
    //permite reutilizarea clasei

    protected ID id;
    //poate fi accesat de clasele derivate sau de clasele din acelasi pachet

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Entity)) return false;
        Entity<?> entity = (Entity<?>)obj;
        return getId().equals(entity.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId());
    }

    @Override
    public String toString(){
        return "Entity{ id= "+getId()+"}";
    }
}
