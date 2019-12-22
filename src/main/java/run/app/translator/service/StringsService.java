package run.app.translator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.app.translator.models.Files;
import run.app.translator.models.Strings;
import run.app.translator.repository.FileRepository;
import run.app.translator.repository.StringsRepository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class StringsService {
    private StringsRepository stringsRepository;

    private FileRepository fileRepository;

    private TranslateService translateService;

    @Autowired
    public void setTranslateService(TranslateService translateService) {
        this.translateService = translateService;
    }

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Autowired
    public void setStringsRepository(StringsRepository stringsRepository) {
        this.stringsRepository = stringsRepository;
    }

    public Iterable<Strings> findAll() {
        return stringsRepository.findAll();
    }

    public Iterable<Strings> findStringsByFileId(Integer id) {
        Optional<Files> optional = fileRepository.findById(id);
        if (optional.isPresent()) {
            Files files = optional.get();
            return stringsRepository.findAllByFiles(files);
        }
        return new ArrayList<>();
    }

    public Optional<Strings> findStringsById(Integer id) {
        return stringsRepository.findById(id);
    }

    public Strings save(Strings strings) {
        return stringsRepository.save(strings);
    }

    public Iterable<Strings> saveAll(Iterable<Strings> stringsArrayList) {
       return  stringsRepository.saveAll(stringsArrayList);
    }

    public void deleteByFiles(Integer fileId) {
        Iterable<Strings> strings = findStringsByFileId(fileId);
        for (Strings string : strings) {
            translateService.deleteByStrings(string);
        }
        stringsRepository.deleteInBatch(strings);
    }

    public void delete(Integer id) {
        translateService.deleteByStringsId(id);
        stringsRepository.deleteById(id);
    }
}
