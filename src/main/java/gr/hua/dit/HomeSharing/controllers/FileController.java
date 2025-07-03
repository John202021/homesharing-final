package gr.hua.dit.HomeSharing.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;

import gr.hua.dit.HomeSharing.services.FileStorageService;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
    String key = storageService.upload(file);
    return ResponseEntity.ok(key);     // just the key in the body
    }

    @GetMapping("/{key}")
    public ResponseEntity<byte[]> download(@PathVariable String key) throws IOException {
        byte[] data = storageService.download(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(data);
    }

    
}
