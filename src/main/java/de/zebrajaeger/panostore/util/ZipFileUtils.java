package de.zebrajaeger.panostore.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;

public class ZipFileUtils {

    private List<String> sharedPath = null;

    public void reset() {
        sharedPath = null;
    }

    public int size() {
        return sharedPath.size();
    }

    public void addPath(ZipEntry zipEntry) {
        if (!zipEntry.isDirectory()) {
            addPath(zipEntry.getName());
        }
    }

    public String removeSharedPath(String path) {
        List<String> parts = pathToParts(path);
        if (startsWithSharedPath(parts)) {
            parts = parts.subList(size(), parts.size());
            return StringUtils.join(parts, '/');
        } else {
            throw new RuntimeException("path does not match");
        }
    }

    protected void addPath(String zipEntryPath) {
        List<String> path = pathToParts(zipEntryPath);
        path = path.subList(0, path.size() - 1);
        if (sharedPath == null) {
            sharedPath = path;
        } else {
            int equalPos = findSharedPathPos(path);
            if (equalPos < size()) {
                sharedPath = sharedPath.subList(0, equalPos);
            }
        }
    }

    private boolean startsWithSharedPath(List<String> path) {
        int pos = findSharedPathPos(path);
        return pos >= size();
    }

    private int findSharedPathPos(List<String> path) {
        int equalPos = 0;
        for (int i = 0; i < size() && i < path.size(); ++i) {
            if (StringUtils.equals(sharedPath.get(i), path.get(i))) {
                equalPos++;
            } else {
                break;
            }
        }
        return equalPos;
    }

    public List<String> getSharedPath() {
        return sharedPath;
    }
    public String getSharedPathAsString() {
        return StringUtils.join(sharedPath,'/');
    }

    protected List<String> pathToParts(String path) {
        String normalized = FilenameUtils.normalize(Objects.requireNonNull(path), true);
        String[] parts = StringUtils.split(normalized, '/');
        return Arrays.asList(parts);
    }
}
