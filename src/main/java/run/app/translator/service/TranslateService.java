package run.app.translator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.app.translator.models.Strings;
import run.app.translator.models.Translate;
import run.app.translator.repository.StringsRepository;
import run.app.translator.repository.TranslateRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TranslateService {
    private TranslateRepository translateRepository;

    private StringsRepository stringsRepository;

    @Autowired
    public void setTranslateRepository(TranslateRepository translateRepository) {
        this.translateRepository = translateRepository;
    }

    @Autowired
    public void setStringsRepository(StringsRepository stringsRepository) {
        this.stringsRepository = stringsRepository;
    }

    public Iterable<Translate> findTranslateByStrings(Integer stringsId) {
        Optional<Strings> optional = stringsRepository.findById(stringsId);
        if (optional.isPresent()) {
            Strings strings = optional.get();
            return translateRepository.findAllByStrings(strings);
        }
        return new ArrayList<>();
    }

    public Iterable<Translate> findAllByStrings(Strings strings) {
        return translateRepository.findAllByStrings(strings);
    }

    public Iterable<Translate> findTranslateByStringsAndKey(Integer stringsId, String key) {
        Optional<Strings> optional = stringsRepository.findById(stringsId);
        if (optional.isPresent()) {
            Strings strings = optional.get();
            return translateRepository.findAllByStringsAndKey(strings, key);
        }
        return new ArrayList<>();
    }

    public Translate save(Translate translate) {
        return translateRepository.save(translate);
    }

    public Iterable<Translate> saveAll(Iterable<Translate> translateArrayList) {
        return  translateRepository.saveAll(translateArrayList);
    }

    public void delete(Integer id) {
        translateRepository.deleteById(id);
    }
}
