package de.zebrajaeger.panostore.repo;

import de.zebrajaeger.panostore.data.Pano;
import org.springframework.data.repository.CrudRepository;

public interface PanoRepository extends CrudRepository<Pano, Long> {
}
