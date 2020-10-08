package ru.ncedu.tdakkota.archive;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

public class ArchiveFile {
    private String path;
    private long size;
    private byte[] hash;

    public ArchiveFile(String path, long size, byte[] hash) {
        this.path = path;
        this.size = size;
        this.hash = hash;
    }

    public ArchiveFile(String path, long size, InputStream in) throws IOException {
        this(path, size, DigestUtils.sha1(in));
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public byte[] getHash() {
        return hash;
    }
}

