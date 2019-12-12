package run.app.translator.repository;

import org.springframework.data.repository.CrudRepository;
import run.app.translator.models.Files;

public interface FileRepository extends CrudRepository<Files, Integer> {

}
