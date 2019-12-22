package run.app.translator.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.app.translator.models.Files;
import run.app.translator.models.Strings;
import run.app.translator.models.Translate;
import run.app.translator.repository.FileRepository;
import run.app.translator.repository.StringsRepository;
import run.app.translator.repository.TranslateRepository;
import run.app.translator.utils.ReflectUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class TranslateService {
    private TranslateRepository translateRepository;

    private StringsRepository stringsRepository;

    private StringsService stringsService;

    private FileRepository fileRepository;

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Autowired
    public void setTranslateRepository(TranslateRepository translateRepository) {
        this.translateRepository = translateRepository;
    }

    @Autowired
    public void setStringsRepository(StringsRepository stringsRepository) {
        this.stringsRepository = stringsRepository;
    }

    @Autowired
    public void setStringsService(StringsService stringsService) {
        this.stringsService = stringsService;
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

    public void deleteByStrings(Strings strings) {
        Iterable<Translate> translates = findAllByStrings(strings);
        if (translates.iterator().hasNext()) {
            translateRepository.deleteInBatch(translates);
        }
    }

    public void deleteByStringsId(Integer stringsId) {
        Iterable<Translate> translates = findTranslateByStrings(stringsId);
        if (translates.iterator().hasNext()) {
            translateRepository.deleteInBatch(translates);
        }
    }

    public void delete(Integer id) {
        translateRepository.deleteById(id);
    }

    public void exportTranslateFile(Integer fileId, String key, HttpServletResponse response) throws IOException {
        Optional<Files> optional = fileRepository.findById(fileId);
        if (!optional.isPresent()) {
            return;
        }
        Files files = optional.get();
        String oldFilename = files.getFilename();
        String extension = files.getSuffix();
        response.setContentType("text/plain");
        String filename = oldFilename.replace("." + extension, "") + "-" + key + "." + extension;
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        BufferedOutputStream bufferedOutputStream = null;
        StringBuilder write = new StringBuilder();
        String enter = "\r\n";
        ServletOutputStream servletOutputStream = response.getOutputStream();
        bufferedOutputStream = new BufferedOutputStream(servletOutputStream);
        Iterable<Strings> stringsIterable = stringsService.findStringsByFileId(fileId);
        ArrayList<Object> results = new ArrayList<>();
        for (Strings strings : stringsIterable) {
            Iterable<Translate> translateList = findTranslateByStringsAndKey(strings.getId(), key);
           if (translateList.iterator().hasNext()) {
               Translate translate = translateList.iterator().next();
               write.append("\"").append(strings.getKey()).append("\" : ");
               write.append("\"").append(translate.getValue()).append("\";").append(enter);
           }else {
               write.append("\"").append(strings.getKey()).append("\" : ");
               write.append("\"").append(strings.getOrigin()).append("\";").append(enter);
           }
        }
        bufferedOutputStream.write(write.toString().getBytes(StandardCharsets.UTF_8));
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
}
