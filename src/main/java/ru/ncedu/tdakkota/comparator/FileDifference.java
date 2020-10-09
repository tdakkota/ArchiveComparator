package ru.ncedu.tdakkota.comparator;

import ru.ncedu.tdakkota.archive.ArchiveFile;

public class FileDifference implements Comparable<FileDifference> {
    public enum Type {
        NO_DIFFERENCE,
        ADDED,
        DELETED,
        CHANGED,
        POSSIBLY_RENAMED;

        @Override
        public String toString() {
            switch (this) {
                case ADDED:
                    return "+";
                case DELETED:
                    return "-";
                case CHANGED:
                    return "*";
                case POSSIBLY_RENAMED:
                    return "?";
            }

            return "";
        }
    }

    private ArchiveFile file;
    private ArchiveFile possibleRenamedTo;
    private Type type;

    public FileDifference(ArchiveFile file, Type type) {
        this(file, type, null);
    }

    public FileDifference(ArchiveFile file, Type type, ArchiveFile possibleRenamedTo) {
        this.file = file;
        this.type = type;
        this.possibleRenamedTo = possibleRenamedTo;
    }

    public ArchiveFile getFile() {
        return file;
    }

    public Type getType() {
        return type;
    }

    public FileDifference getPossibleRenamedTo() {
        if (possibleRenamedTo != null) {
            return new FileDifference(possibleRenamedTo, type, this.file);
        }
        return null;
    }

    @Override
    public String toString() {
        if (type == Type.NO_DIFFERENCE) {
            return file.getPath();
        }
        return getType() + " " + file.getPath();
    }

    @Override
    public int compareTo(FileDifference o) {
        return this.getFile().getPath().compareTo(o.getFile().getPath());
    }
}
