package ao.samid.file.controller;

import ao.samid.file.dto.response.FileResponse;
import ao.samid.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    public void deleteFileById(Long id) {
        fileService.deleteFileById(id);
    }
    @PostMapping("/upload/{carId}")
    public ResponseEntity<Void> uploadFile(@RequestPart("file") MultipartFile request, @PathVariable Long carId) {
        fileService.uploadFile(request, carId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/car/{id}")
    public ResponseEntity<List<FileResponse>> getFilesByCarId(@PathVariable Long id) {
        return ResponseEntity.ok().body(fileService.getFilesByCarId(id));
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<InputStream> downLoadFile(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.downloadFile(id));
    }
    @GetMapping("/url/{id}")
    public ResponseEntity<String> getFileUrl(@PathVariable Long id) {
         return ResponseEntity.ok().body(fileService.getFileUrl(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFileById(id);
        return ResponseEntity.noContent().build();}
    @DeleteMapping("/car/{carId}/all")
    public ResponseEntity<Void> deleteAllFilesByCarId(@PathVariable Long carId) {
        fileService.deleteAllFilesByCarId(carId);
        return ResponseEntity.noContent().build();
    }
}
