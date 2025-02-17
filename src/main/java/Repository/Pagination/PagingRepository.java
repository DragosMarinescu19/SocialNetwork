package Repository.Pagination;

import org.example.lab6.Project.Application.Domain.Entity;
import Repository.Repository;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findall(Pageable pageable);
}