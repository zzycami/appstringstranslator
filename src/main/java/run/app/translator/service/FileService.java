package run.app.translator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import run.app.translator.models.Files;
import run.app.translator.repository.FileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    private FileRepository fileRepository;

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

    public Optional<Files> files(Integer id) {
        return fileRepository.findById(id);
    }

    public Files save(Files files) {
        return fileRepository.save(files);
    }

    public void remove(Integer id) {
        fileRepository.deleteById(id);
    }
}
