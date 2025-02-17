package Repository.DataBase;
import org.example.lab6.Project.Application.Domain.Entity;
import Repository.Pagination.*;
public interface FriendRequestPagingRepository<ID, E extends Entity<ID>> extends PagingRepository<ID, E>{
    Page<E> findAllFriendRequests(Pageable pageable);

    Page<E> findAllUserFriends(Pageable pageable, Long id);

    Page<E> findAllUserFriendRequests(Pageable pageable, Long id);
}
