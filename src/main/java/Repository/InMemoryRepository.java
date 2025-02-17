package Repository;


import java.util.*;
import org.example.lab6.Project.Application.Domain.*;
import org.example.lab6.Project.Application.Domain.Validators.Validator;

public class InMemoryRepository<ID,E extends Entity<ID>> implements Repository<ID, E> {

    private Validator<E> validator;
    Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID,E>();//key value
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.get(id));//Optional.empty() daca nu excxista, adica in loc de null
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if(entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));//insereza entitatea doar daca nu exista
    }//returneaza Optional.empty() daca a fopst adaugata pt ca putIfAbsent returneaza null
    //altfel returneaza Optional.of(entity) daca exista deja pt ca putIfAbsent returneaza entity

    @Override
    public Optional<E> delete(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity==null)
            throw new IllegalArgumentException("entity must be not null");

        validator.validate(entity);
        E oldentity=entities.put(entity.getId(), entity);
        //daca key nu exista se adauga si ret null , altfel se actualizeaza
        //valoarea asociata cheii si returneaza valoarea anterioara

        return Optional.ofNullable(oldentity);
        //returneaza Optional.empty() daca nu exista inainte
    }
}
