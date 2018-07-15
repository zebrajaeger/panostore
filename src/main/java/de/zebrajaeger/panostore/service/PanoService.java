package de.zebrajaeger.panostore.service;

import de.zebrajaeger.panostore.data.Config;
import de.zebrajaeger.panostore.data.Pano;
import de.zebrajaeger.panostore.data.PanoFile;
import de.zebrajaeger.panostore.krpano.KrPanoConfig;
import de.zebrajaeger.panostore.krpano.KrPanoPreview;
import de.zebrajaeger.panostore.repo.ConfigRepository;
import de.zebrajaeger.panostore.repo.PanoFileRepository;
import de.zebrajaeger.panostore.repo.PanoRepository;
import de.zebrajaeger.panostore.util.ZipFileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class PanoService {
    private static final Logger LOG = LoggerFactory.getLogger(PanoService.class);

    @Autowired
    private PanoRepository panoRepository;
    @Autowired
    private PanoFileRepository panoFileRepository;
    @Autowired
    private ConfigRepository configRepository;

    public PanoFile findFile(Long panoId, String path) {
        Optional<Pano> pano = panoRepository.findById(panoId);
        if (pano.isPresent()) {
            return panoFileRepository.xxx(pano.get(), path);
        }
        return null;
    }

    @Transactional(rollbackOn = {IOException.class, NoKrPanoConfigFound.class})
    public void importPano(File source) throws IOException, NoKrPanoConfigFound {
        long start = System.currentTimeMillis();
        Pano pano = panoRepository.save(new Pano());
        LOG.info("Start Pano Importmit ID: '{}'", pano.getId());

        LOG.info("    readAndSaveFiles: '{}'", pano.getId());
        List<PanoFile> xmlFiles = readAndSaveFiles(source, pano);
        LOG.info("    findAndSaveConfig: '{}'", pano.getId());
        ChoosenConfig choosenCfg = findAndSaveConfig(pano, xmlFiles);

        // find and save preview
        LOG.info("    findAndSavePreview: '{}'", pano.getId());
        KrPanoPreview preview = choosenCfg.panoConfig.getPreview();
        if (preview != null) {
            String url = preview.getUrl();
            PanoFile previewFile = panoFileRepository.xxx(pano, url);
            if (previewFile != null) {
                pano.setPreview(previewFile);
                LOG.info("!YOO!!!!");
            } else {
                LOG.info("!FUCK!!!!");
                pano.setPreview(null);
            }
            panoRepository.save(pano);
        }

        long duration = System.currentTimeMillis() - start;
        LOG.info("Finished Pano Import with ID: '{}' in '{}.{}' seconds", pano.getId(), (duration / 1000), (duration % 1000));
    }

    private List<PanoFile> readAndSaveFiles(File source, Pano pano) throws IOException {
        ZipFileUtils zipFileUtils = new ZipFileUtils();

        ZipFile zf = new ZipFile(source);

        // identify shared path
        for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = e.nextElement();
            zipFileUtils.addPath(entry);
        }
        LOG.info("Shared Path: '{}'", zipFileUtils.getSharedPathAsString());

        // import files
        List<PanoFile> xmlFiles = new LinkedList<>();
        for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = e.nextElement();
            if (!entry.isDirectory()) {
                String name = entry.getName();

                String path = zipFileUtils.removeSharedPath(name);
                PanoFile panoFile = new PanoFile();
                panoFile.setFileName(path);
                panoFile.setContent(IOUtils.toByteArray(zf.getInputStream(entry)));
                panoFile.setPano(pano);
                pano.getPanoFiles().add(panoFile);
                panoFileRepository.save(panoFile);

                if ("xml".equals(FilenameUtils.getExtension(name).toLowerCase())) {
                    xmlFiles.add(panoFile);
                }
            }
        }
        return xmlFiles;
    }

    private ChoosenConfig findAndSaveConfig(Pano pano, List<PanoFile> xmlFiles) throws NoKrPanoConfigFound {
        ChoosenConfig choosenCfg = null;
        for (PanoFile f : xmlFiles) {
            try {
                KrPanoConfig cfg = KrPanoConfig.fromString(f.contentAsString());
                if (choosenCfg == null || (choosenCfg.panoConfig.getPreview() == null && cfg.getPreview() != null)) {
                    choosenCfg = new ChoosenConfig(f, cfg);
                }
            } catch (IOException e) {
                LOG.warn("failed to read krpanoconfig: '{}'", f.getFileName());
            }
        }

        if (choosenCfg == null) {
            throw new NoKrPanoConfigFound();
        }

        Config config = new Config();
        config.setPanoFile(choosenCfg.panoFile);
        configRepository.save(config);
        pano.setConfig(config);
        panoRepository.save(pano);
        return choosenCfg;
    }

    protected static class ChoosenConfig {
        protected PanoFile panoFile;
        protected KrPanoConfig panoConfig;

        public ChoosenConfig(PanoFile panoFile, KrPanoConfig panoConfig) {
            this.panoFile = panoFile;
            this.panoConfig = panoConfig;
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }
}
