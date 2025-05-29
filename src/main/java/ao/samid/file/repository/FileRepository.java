package ao.samid.file.repository;

import ao.samid.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findFilesByCarId(Long carId);
}
