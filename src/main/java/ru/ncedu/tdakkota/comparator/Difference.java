package ru.ncedu.tdakkota.comparator;

import java.util.Map;

public interface Difference {
    public Map<String, FileDifference> getDiff();

    public String getLeftName();

    public String getRightName();
}
