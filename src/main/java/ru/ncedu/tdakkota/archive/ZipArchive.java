package ru.ncedu.tdakkota.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipArchive implements Archive {
    private String path;
    private Map<String, ArchiveFile> files = new HashMap<>();

    public ZipArchive() {
    }

    public ZipArchive(String path) throws IOException {
        this(new FileInputStream(path), path);
    }

    public ZipArchive(InputStream s) throws IOException {
        this(s, "archive");
    }

    public ZipArchive(File s) throws IOException {
        this(new FileInputStream(s), s.getPath());
    }

    public ZipArchive(InputStream s, String path) throws IOException {
        this.path = path;
        this.open(s);
    }

    public void open(InputStream s) throws IOException {
        try (ZipInputStream in = new ZipInputStream(s)) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                files.put(entry.getName(), new ArchiveFile(entry.getName(), entry.getSize(), in));
                in.closeEntry();
            }
        }
    }

    @Override
    public String getName() {
        return Paths.get(path).getFileName().toString();
    }

    public Map<String, ArchiveFile> files() {
        return files;
    }
}
