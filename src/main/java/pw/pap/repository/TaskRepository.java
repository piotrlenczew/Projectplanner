package pw.pap.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pw.pap.model.Task;


@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
}
