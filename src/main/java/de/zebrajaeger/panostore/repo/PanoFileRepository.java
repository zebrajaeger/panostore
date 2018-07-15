package de.zebrajaeger.panostore.repo;

import de.zebrajaeger.panostore.data.Pano;
import de.zebrajaeger.panostore.data.PanoFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PanoFileRepository extends CrudRepository<PanoFile, Long> {
    Optional<PanoFile> findByPanoAndFileName(Pano pano, String fileName);

    @Query("SELECT f from PanoFile f WHERE f.pano = ?1 AND f.fileName = ?2")
    PanoFile xxx(Pano pano, String fileName);
}
