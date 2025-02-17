package Repository;


import java.io.*;
import java.util.*;
import org.example.lab6.Project.Application.Domain.*;
import org.example.lab6.Project.Application.Domain.Validators.Validator;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    String filename;

    public AbstractFileRepository(String filename, Validator<E> validator) {
        super(validator);
        this.filename = filename;
        loadData();
    }
    public void loadData() {
        System.out.println("Loading data from " + filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String newLine;
            while((newLine=reader.readLine())!=null){
                System.out.println(newLine);
                List<String> data = Arrays.asList(newLine.split(" "));
                E entity = extractEntity(data);
                super.save(entity);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public Optional<E> delete(ID id) {
        Optional<E> entity = super.delete(id);
        if (entity.isPresent()) {
            saveAll();
        }
        return entity;
    }

    public Optional<E> update(E entity) {
        Optional<E> result = super.update(entity);
        if (result.isPresent()) {
            saveAll();
        }
        return Optional.ofNullable(entity);
    }

    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    private void saveAll() {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(filename, false))) {
            for (var entity : findAll()) {
                bW.write(createEntityAsString(entity));
                bW.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(E entity) {
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(filename,true))){//modul append
            writer.write(createEntityAsString(entity));
            writer.newLine();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
