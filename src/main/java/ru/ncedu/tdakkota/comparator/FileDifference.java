package ru.ncedu.tdakkota.comparator;

import ru.ncedu.tdakkota.archive.ArchiveFile;

public class FileDifference implements Comparable<FileDifference> {
    public enum Type {
        NO_DIFFERENCE,
        ADDED,
        DELETED,
        CHANGED,
        POSSIBLY_RENAMED_L,
        POSSIBLY_RENAMED_R;

        @Override
        public String toString() {
            switch (this) {
                case ADDED:
                    return "+";
                case DELETED:
                    return "-";
                case CHANGED:
                    return "*";
                case POSSIBLY_RENAMED_L:
                case POSSIBLY_RENAMED_R:
                    return "?";
            }

            return "";
        }

        public boolean isLeft() {
            return this != POSSIBLY_RENAMED_R && this != ADDED;
        }

        public boolean isRight() {
            return this != POSSIBLY_RENAMED_L && this != DELETED;
        }
    }

    private ArchiveFile file;
    private Type type;

    public FileDifference(ArchiveFile file, Type type) {
        this.file = file;
        this.type = type;
    }

    public ArchiveFile getFile() {
        return file;
    }

    public Type getType() {
        return type;
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
