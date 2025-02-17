package Repository;

import org.example.lab6.Project.Application.Domain.Entity;

import java.util.Optional;

public interface Repository<ID,E extends Entity<ID>> {

    //E poate fi de tip User sau Friendship si ID poate fi orice tip de date Long, String etc

    //Folosind Optional reduce riscul aparitiei NullPointerException
    //Gestioneaza valorile care ar putea fi null
    //returneaza entitatea cu id ul sopeciifcat sau optional gol=Optional.empty(), arunca exceptie daca id e null
    Optional<E> findOne(ID id);

    //returneraza toate entitatile
    Iterable<E> findAll();

    /*entity nu trebuie sa fie null
    returneaza Optional gol daca e deja salvata altfel returneaza entitatea
    arunca ValidationException daca entitatea nu e valida
    arunca IllegalArgumentException daca entitatea e null
    */
    Optional<E> save(E entity);

    //sterge entitatea cu id ul dat si o returneaza sau Optional gol daca entity nu e gasita
    Optional<E> delete(ID id);

    //returneaza Optional gol daca a fost deja actualizata
    //returneaza entitatea si arunca exceptii daca entity=null sau nu e valida
    Optional<E> update(E entity);

    /*Folosind Optional pentru metodele findOne, save, delete si update, interfata Repository
    poate exprima in mod clar ca o entitate poate fi prezenta sau poate lipsi fara a folosi null.
    Aceasta reduce riscul de erori si face codul mai usor de intretinut.
     */

}

