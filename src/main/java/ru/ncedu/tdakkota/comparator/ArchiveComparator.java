package ru.ncedu.tdakkota.comparator;

import ru.ncedu.tdakkota.archive.Archive;
import ru.ncedu.tdakkota.archive.ArchiveFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ArchiveComparator {
    private Archive archive;
    // Optimize search by size.
    private Map<Long, ArchiveFile> sizesMap;

    public ArchiveComparator(Archive a) {
        this.archive = a;
        this.sizesMap = getSizesMap(this.archive);
    }

    public static ArchiveDifference compare(Archive a, Archive b) {
        return new ArchiveComparator(a).compare(b);
    }

    private Map<Long, ArchiveFile> getSizesMap(Archive a) {
        Map<Long, ArchiveFile> ss = new HashMap<>();
        a.files().forEach((name, file) -> ss.put(file.getSize(), file));
        return ss;
    }

    public ArchiveDifference compare(Archive b) {
        Map<String, ArchiveFile> leftFiles = this.archive.files();
        Map<String, ArchiveFile> rightFiles = b.files();
        Map<Long, ArchiveFile> rightSizes = getSizesMap(b);

        ArchiveDifference m = new ArchiveDifference(archive, b);
        HashSet<String> paths = new HashSet<>(rightFiles.keySet());
        paths.addAll(leftFiles.keySet());

        for (String name : paths) {
            ArchiveFile left = leftFiles.get(name);
            ArchiveFile right = rightFiles.get(name);
            boolean hasLeft = left != null;
            boolean hasRight = right != null;
            if (!hasLeft && !hasRight) {
                throw new IllegalStateException("File list was modified: one or more entries was deleted.");
            }

            ArchiveFile possibleRenamedTo = null;
            FileDifference.Type t = FileDifference.Type.NO_DIFFERENCE;
            if (!hasLeft && this.sizesMap.containsKey(right.getSize())) {
                // old archive does not contain this name, but contain file with same size.
                t = FileDifference.Type.POSSIBLY_RENAMED;
                possibleRenamedTo = this.sizesMap.get(right.getSize());
            } else if (!hasRight && rightSizes.containsKey(left.getSize())) {
                // new archive does not contain this name, but contain file with same size.
                t = FileDifference.Type.POSSIBLY_RENAMED;
                possibleRenamedTo = rightSizes.get(left.getSize());
            } else if (!hasLeft) {
                // old archive does not contain this name and not contain files with same size.
                t = FileDifference.Type.ADDED;
            } else if (!hasRight) {
                // new archive does not contain this name, but old does.
                t = FileDifference.Type.DELETED;
            } else if (left.getSize() != right.getSize() || !Arrays.equals(left.getHash(), right.getHash())) {
                t = FileDifference.Type.CHANGED;
            }

            if (hasLeft) {
                m.add(new FileDifference(left, t, possibleRenamedTo));
            } else {
                m.add(new FileDifference(right, t, possibleRenamedTo));
            }
        }
        return m;
    }
}
