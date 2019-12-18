package run.app.translator.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import run.app.translator.googleTranslate.GoogleTranslate;
import run.app.translator.models.Files;
import run.app.translator.models.Strings;
import run.app.translator.service.FileService;
import run.app.translator.service.StringsService;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class IndexController {
    private FileService fileService;
    private StringsService stringsService;
    private GoogleTranslate googleTranslate;

    @Value("classpath:language.json")
    private Resource languageResource;

    @Value("classpath:gettk.js")
    private Resource scriptResource;

    @Autowired
    public void setGoogleTranslate(GoogleTranslate googleTranslate) {
        this.googleTranslate = googleTranslate;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setStringsService(StringsService stringsService) {
        this.stringsService = stringsService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "多语言自动配置");
        return "index";
    }

    @RequestMapping("language")
    @ResponseBody
    public String language() throws IOException {
        return IOUtils.toString(languageResource.getInputStream(), StandardCharsets.UTF_8);
    }

    @RequestMapping("translate/word/{word}/from/{from}/to/{to}")
    @ResponseBody
    public String translate(@PathVariable("word") String word, @PathVariable("from") String from, @PathVariable("to") String to) throws IOException, ScriptException, NoSuchMethodException {
        googleTranslate.setScriptResource(this.scriptResource);
        String tkk = googleTranslate.getTKK();
        return googleTranslate.translate(word, from, to);
    }

    @PostMapping("upload")
    @ResponseBody
    public ResponseEntity<?> upload(MultipartFile file, HttpServletRequest request) {
        String uploadDictionary = request.getSession().getServletContext().getRealPath("/uploadFile/");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/");
        String containerName = format.format(new Date());
        File folder = new File(uploadDictionary + containerName);
        if (!folder.isDirectory()) {
            boolean isSuccess = folder.mkdirs();
            if (!isSuccess) {
                System.out.println("mkdirs failed");
            }
        }
        String oldFilename = file.getOriginalFilename();
        String extension = "strings";
        if (oldFilename != null) {
            extension = oldFilename.substring(oldFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString();
        try {
            File savedFile = new File(folder, filename + extension);
            file.transferTo(savedFile);
            Files files = fileService.addFile(oldFilename, filename, file.getSize(), folder + filename + extension, extension.replace(".", ""));
            Iterable<Strings> stringsArrayList = parseStrings(savedFile, files);
            return new ResponseEntity<Object>(stringsService.saveAll(stringsArrayList), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Iterable<Strings> parseStrings(File filename, Files files) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String lineValue = "";
        ArrayList<Strings> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"(.*?)\"[\\s]*=[\\s]*\"(.*?)\"[\\s]*;");
        while ((lineValue = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(lineValue);
            if (matcher.find()) {
                int count = matcher.groupCount();
                String all = matcher.group(0);
                String key = matcher.group(1);
                String value = matcher.group(2);
                Strings strings = new Strings();
                strings.setKey(key);
                strings.setOrigin(value);
                strings.setFiles(files);
                result.add(strings);
            }
        }
        return result;
    }
}
