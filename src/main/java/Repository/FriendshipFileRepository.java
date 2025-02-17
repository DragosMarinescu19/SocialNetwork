package Repository;



import java.time.LocalDateTime;
import java.util.List;
import org.example.lab6.Project.Application.Domain.*;
import org.example.lab6.Project.Application.Domain.Validators.Validator;
public class FriendshipFileRepository extends AbstractFileRepository<Long, Friendship> {

    public FriendshipFileRepository(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Long id1 = Long.parseLong(attributes.get(1));
        Long id2 = Long.parseLong(attributes.get(2));
        Friendship friendship = new Friendship(id1, id2, LocalDateTime.now());
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId() + ";" + entity.getIdUser1() + ";" + entity.getIdUser2();
    }
}

