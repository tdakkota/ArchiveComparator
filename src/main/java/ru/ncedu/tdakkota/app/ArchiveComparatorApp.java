package ru.ncedu.tdakkota.app;

import ru.ncedu.tdakkota.archive.ZipArchive;
import ru.ncedu.tdakkota.comparator.ArchiveComparator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class ArchiveComparatorApp extends JFrame {
    private final String[][] FILTERS = {{"zip", "ZIP Archive (*.zip)", "jar", "Java Archive (*.jar)"}};

    public static void main(String[] args) {
        try {
            new ArchiveComparatorApp().run(args);
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    private File askFile() throws IOException {
        JFileChooser c = new JFileChooser();
        c.setMultiSelectionEnabled(false);
        for (int i = 0; i < FILTERS[0].length; i++) {
            FileNameExtensionFilter eff = new FileNameExtensionFilter(FILTERS[i][0], FILTERS[i][1]);
            c.addChoosableFileFilter(eff);
        }

        int ret = c.showOpenDialog(this);
        if (ret != JFileChooser.APPROVE_OPTION) {
            throw new IOException("failed to open file");
        }

        return c.getSelectedFile();
    }

    private void run(String[] args) throws IOException {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        File a, b;
        if (args.length == 0) {
            a = askFile();
            b = askFile();
        } else if (args.length == 1) {
            a = new File(args[0]);
            b = askFile();
        } else {
            a = new File(args[0]);
            b = new File(args[1]);
        }

        ArchiveComparator.compare(new ZipArchive(a), new ZipArchive(b));
    }
}
