package ru.ncedu.tdakkota.comparator;

import org.junit.jupiter.api.Test;
import ru.ncedu.tdakkota.archive.Archive;
import ru.ncedu.tdakkota.archive.ArchiveFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparatorTest {
    class MockArchive implements Archive {
        String name;
        Map<String, ArchiveFile> files = new HashMap<>();

        public MockArchive(String name) {
            this.name = name;
        }

        @Override
        public void open(InputStream s) {
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Map<String, ArchiveFile> files() {
            return files;
        }
    }

    ArchiveDifference getTestDifference() {
        MockArchive a = new MockArchive("a");
        ArchiveFile doNotTouchMe = new ArchiveFile("do-not-touch-me", 1, new byte[]{1, 2, 3});
        a.files.put("do-not-touch-me", new ArchiveFile("do-not-touch-me", 1, new byte[]{1, 2, 3}));
        a.files.put("delete-me", new ArchiveFile("delete-me", 2, new byte[]{1, 2, 3}));
        a.files.put("modify-me", new ArchiveFile("modify-me", 3, new byte[]{1, 2, 3}));
        a.files.put("rename-me", new ArchiveFile("rename-me", 4, new byte[]{1, 2, 3}));

        MockArchive b = new MockArchive("b");
        b.files.put("do-not-touch-me", new ArchiveFile("do-not-touch-me", 1, new byte[]{1, 2, 3}));
        b.files.put("add-me", new ArchiveFile("add-me", 5, new byte[]{1, 2, 3}));
        b.files.put("modify-me", new ArchiveFile("modify-me", 6, new byte[]{3, 2, 1}));
        b.files.put("i-was-renamed", new ArchiveFile("i-was-renamed", 4, new byte[]{1, 2, 3}));

        ArchiveComparator comparator = new ArchiveComparator(a);
        return comparator.compare(b);
    }

    @Test
    void compare() {
        ArchiveDifference d = getTestDifference();
        Map<String, FileDifference> m = d.getDiff();

        assertEquals(FileDifference.Type.NO_DIFFERENCE, m.get("do-not-touch-me").getType());
        assertEquals(FileDifference.Type.ADDED, m.get("add-me").getType());
        assertEquals(FileDifference.Type.DELETED, m.get("delete-me").getType());
        assertEquals(FileDifference.Type.CHANGED, m.get("modify-me").getType());
        assertEquals(FileDifference.Type.POSSIBLY_RENAMED_R, m.get("rename-me").getType());
        assertEquals(FileDifference.Type.POSSIBLY_RENAMED_L, m.get("i-was-renamed").getType());
    }

    @Test
    void render() {
        ConsoleRenderer r = new ConsoleRenderer();
        ArchiveDifference d = getTestDifference();
        r.render(d);
    }
}