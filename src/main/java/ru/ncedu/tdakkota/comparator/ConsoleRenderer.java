package ru.ncedu.tdakkota.comparator;

import java.io.PrintStream;
import java.util.Map;

public class ConsoleRenderer implements DifferenceRenderer {
    private PrintStream out;
    private int maxColumnLength = -1;
    private int defaultColumnLength = 15;

    public ConsoleRenderer() {
        this(System.out);
    }

    public ConsoleRenderer(PrintStream out) {
        this.out = out;
    }

    public int getDefaultColumnLength() {
        return defaultColumnLength;
    }

    public void setDefaultColumnLength(int defaultColumnLength) {
        this.defaultColumnLength = defaultColumnLength;
    }

    public int getMaxColumnLength() {
        return maxColumnLength;
    }

    public void setMaxColumnLength(int maxColumnLength) {
        this.maxColumnLength = maxColumnLength;
    }

    private String getCutName(String v) {
        if (maxColumnLength > 0 && v.length() + 3 > maxColumnLength)
            return v.substring(0, maxColumnLength) + "...";
        return v;
    }

    @Override
    public void render(Difference d) {
        Map<String, FileDifference> diff = d.getDiff();
        // Find the longest name
        int columnSize = diff.keySet().
                stream().
                map(String::length).
                max(Integer::compareTo).
                orElse(defaultColumnLength);

        String format = "| %-" + columnSize + "s | %-" + columnSize + "s |%n";
        out.format(format, getCutName(d.getLeftName()), getCutName(d.getRightName()));
        out.format("| " + "-".repeat(columnSize) + " | " + "-".repeat(columnSize) + " |\n");
        diff.keySet().stream().sorted().forEach(name -> {
            FileDifference fileDifference = diff.get(name);
            FileDifference.Type t = fileDifference.getType();

            String left = fileDifference.toString();
            String right = left;
            switch (t) {
                case ADDED:
                    left = "";
                    break;
                case DELETED:
                    right = "";
                    break;
                case CHANGED:
                    break;
                case POSSIBLY_RENAMED:
                    right = fileDifference.getPossibleRenamedTo().toString();
                    break;
            }

            out.format(format, getCutName(left), getCutName(right));
        });
    }
}
