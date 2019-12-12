package run.app.translator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import run.app.translator.models.Files;
import run.app.translator.models.Strings;
import run.app.translator.service.FileService;
import run.app.translator.service.StringsService;

import java.util.Optional;

@RestController
@RequestMapping("/strings")
public class StringsController {
    private StringsService stringsService;

    @Autowired
    public void setStringsService(StringsService stringsService) {
        this.stringsService = stringsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> list(@PathVariable("id") Integer id) {
        Iterable<Strings> stringsIterable = stringsService.findStringsByFileId(id);
        return new ResponseEntity<Object>(stringsIterable, HttpStatus.OK);
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
