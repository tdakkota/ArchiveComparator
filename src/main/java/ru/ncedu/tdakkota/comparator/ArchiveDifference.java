package ru.ncedu.tdakkota.comparator;

import ru.ncedu.tdakkota.archive.Archive;

import java.util.HashMap;
import java.util.Map;

public class ArchiveDifference implements Difference {
    private Archive leftArchive;
    private Archive rightArchive;

    private Map<String, FileDifference> diff = new HashMap<>();

    public ArchiveDifference(Archive leftArchive, Archive rightArchive) {
        this.leftArchive = leftArchive;
        this.rightArchive = rightArchive;
    }

    public void add(FileDifference d) {
        diff.put(d.getFile().getPath(), d);
    }

    public Map<String, FileDifference> getDiff() {
        return diff;
    }

    public String getLeftName() {
        return leftArchive.getName();
    }

    public String getRightName() {
        return rightArchive.getName();
    }
}
