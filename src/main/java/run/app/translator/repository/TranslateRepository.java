package run.app.translator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import run.app.translator.models.Strings;
import run.app.translator.models.Translate;

import java.security.Key;

public interface TranslateRepository extends JpaRepository<Translate, Integer> {

    Iterable<Translate> findAllByStringsAndKey(Strings strings, String key);

    Iterable<Translate> findAllByStrings(Strings strings);
}
