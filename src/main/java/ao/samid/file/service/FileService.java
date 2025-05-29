package ao.samid.file.service;

import ao.samid.file.dto.response.FileResponse;
import ao.samid.file.entity.File;
import ao.samid.file.repository.FileRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final MinioService minioService;
    public List<FileResponse> getFilesByCarId(Long id) {
        return  fileRepository.findFilesByCarId(id).stream().map(image->
                FileResponse.builder()
                        .id(image.getId())
                        .imageName(image.getImageName())
                        .imagePath(minioService.getFileUrl(image.getImageName()))
                        .contentType(image.getContentType())
                        .carId(image.getCarId())
                        .build()).toList();
    }
    public void deleteFileById(Long id) {
        fileRepository.findById(id).ifPresent(file -> {
            minioService.deleteFile(file.getImageName());
            fileRepository.delete(file);
        });
    }
    public FileResponse uploadFile(MultipartFile request, Long carId) {
        String fileUrl=minioService.uploadFile(request, request.getOriginalFilename());
        File save = fileRepository.save(File
                .builder()
                .carId(carId)
                .imagePath(fileUrl)
                .imageName(request.getOriginalFilename())
                .contentType(request.getContentType())
                .build());
        return FileResponse
                .builder()
                .id(save.getId())
                .carId(carId)
                .imagePath(fileUrl)
                .imageName(request.getOriginalFilename())
                .contentType(request.getContentType())
                .build();
    }
    public InputStream downloadFile(Long id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        return minioService.downloadFile(file.getImageName());
    }
    public String getFileUrl(Long id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        return minioService.getFileUrl(file.getImageName());
    }
    public void deleteAllFilesByCarId(Long carId) {
        fileRepository.findFilesByCarId(carId).forEach(file ->{
            minioService.deleteFile(file.getImageName());
            fileRepository.delete(file);
        });

    }



}
