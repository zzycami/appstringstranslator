package run.app.translator.controllers;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import run.app.translator.models.Files;
import run.app.translator.models.Strings;
import run.app.translator.models.Translate;
import run.app.translator.service.FileService;
import run.app.translator.service.StringsService;
import run.app.translator.service.TranslateService;
import run.app.translator.utils.ReflectUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/strings")
public class StringsController {
    private StringsService stringsService;
    private TranslateService translateService;

    @Autowired
    public void setTranslateService(TranslateService translateService) {
        this.translateService = translateService;
    }

    @Autowired
    public void setStringsService(StringsService stringsService) {
        this.stringsService = stringsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> list(@PathVariable("id") Integer id) {
        Iterable<Strings> stringsIterable = stringsService.findStringsByFileId(id);
        ArrayList<Object> results = new ArrayList<>();
        for (Strings strings : stringsIterable) {
            Iterable<Translate> translateList = translateService.findAllByStrings(strings);
            Map<String, Object> properties = Maps.newHashMap();
            for (Translate translate : translateList) {
                properties.put(translate.getKey(), translate.getValue());
            }
            results.add(ReflectUtil.getTarget(strings, properties));
        }
        return new ResponseEntity<Object>(results, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Strings strings) {
        return new ResponseEntity<Strings>(stringsService.save(strings), HttpStatus.CREATED);
    }

    @PatchMapping("")
    public ResponseEntity<?> update(@RequestBody Strings strings) {
        return new ResponseEntity<Strings>(stringsService.save(strings), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") Integer id) {
        stringsService.delete(id);
        return new ResponseEntity<Files>(HttpStatus.ACCEPTED);
    }
}
