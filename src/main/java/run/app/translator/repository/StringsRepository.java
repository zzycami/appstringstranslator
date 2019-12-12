package run.app.translator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import run.app.translator.models.Files;
import run.app.translator.models.Strings;

public interface StringsRepository extends JpaRepository<Strings, Integer> {
    Iterable<Strings> findAllByFiles(Files file);
}
