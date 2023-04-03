package pro.sky.noskoff.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.noskoff.services.FilesServiceSocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/socksFiles")
@Tag(name = "Работа с файлами", description = "скачать файл со всеми носками или загрузить свой")
public class FilesSocksController {
    private final FilesServiceSocks filesServiceSocks;

    public FilesSocksController(FilesServiceSocks filesServiceSocks) {
        this.filesServiceSocks = filesServiceSocks;
    }

    @GetMapping("/export")
    @Operation(summary = "Скачать файл с носками")
    public ResponseEntity<InputStreamResource> downloadDataSocksFile() throws FileNotFoundException {
        File file = filesServiceSocks.getDataFileSocks();   // получаем инфу о самом файле
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));  // открываем из fail поток (FileInputStream), сразу не читаем, оборачиваем в resource
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)  //
                    .contentLength(file.length())   // чтобы браузер знал сколько байт в файле
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksLog.json\"")  // говорим, что его надо скачать и с каким названием
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить файл с носками")
    public ResponseEntity<Void> uploadDataSocksFile(@RequestParam MultipartFile file) {
        if (filesServiceSocks.uploadDataSocksFile(file)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
