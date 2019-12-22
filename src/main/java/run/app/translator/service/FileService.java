package run.app.translator.service;

import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.app.translator.models.Files;
import run.app.translator.repository.FileRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    private FileRepository fileRepository;
    private StringsService stringsService;

    @Autowired
    public void setStringsService(StringsService stringsService) {
        this.stringsService = stringsService;
    }

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Iterable<Files> findAllFiles() {
        return fileRepository.findAll();
    }

    public Files addFile(String filename, String randomName, Long size, String path, String suffix) {
        Files files = new Files();
        files.setFilename(filename);
        files.setSize(size);
        files.setPath(path);
        files.setSuffix(suffix);
        files.setRandomName(randomName);
        return fileRepository.save(files);
    }

    public Optional<Files> getFile(Integer id) {
        return fileRepository.findById(id);
    }

    public Files addTranslateLanguage(Files files, String language) {
        String translatedLanguages = files.getTranslatedLanguage();
        if (StringUtil.isBlank(translatedLanguages)) {
            files.setTranslatedLanguage(language);
        }else {
            String[] languages = translatedLanguages.split(",");
            ArrayList<String> languageList = new ArrayList<>(Arrays.asList(languages));
            languageList.add(language);
            String value = String.join(",", languageList);
            files.setTranslatedLanguage(value);
        }
        return fileRepository.save(files);
    }

    public Optional<Files> files(Integer id) {
        return fileRepository.findById(id);
    }

    public Files save(Files files) {
        return fileRepository.save(files);
    }

    public void remove(Integer id) {
        stringsService.deleteByFiles(id);
        fileRepository.deleteById(id);
    }
}
