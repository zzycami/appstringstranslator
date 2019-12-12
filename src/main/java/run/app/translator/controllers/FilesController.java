package run.app.translator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import run.app.translator.models.Files;
import run.app.translator.service.FileService;

@RestController
@RequestMapping("/files")
public class FilesController {
    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("")
    public ResponseEntity<?> list() {
        return new ResponseEntity<Object>(fileService.findAllFiles(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Files files) {
        return new ResponseEntity<Files>(fileService.save(files), HttpStatus.CREATED);
    }

    @PatchMapping("")
    public ResponseEntity<?> update(@RequestBody Files files) {
        return new ResponseEntity<Files>(fileService.save(files), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") Integer id) {
        fileService.remove(id);
        return new ResponseEntity<Files>(HttpStatus.ACCEPTED);
    }
}
