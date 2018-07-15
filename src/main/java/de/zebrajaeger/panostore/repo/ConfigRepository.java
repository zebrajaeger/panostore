package de.zebrajaeger.panostore.repo;

import de.zebrajaeger.panostore.data.Config;
import de.zebrajaeger.panostore.data.Pano;
import org.springframework.data.repository.CrudRepository;

public interface ConfigRepository extends CrudRepository<Config, Long> {
}
