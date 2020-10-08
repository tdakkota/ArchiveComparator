package ru.ncedu.tdakkota.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface Archive {
    public void open(InputStream s) throws IOException;

    public String getName();

    public Map<String, ArchiveFile> files();
}
