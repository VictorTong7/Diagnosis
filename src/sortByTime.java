import java.util.Comparator;

public class sortByTime implements Comparator<Data> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(Data a, Data b)
    {
        return a.getTime().compareTo(b.getTime());
    }
}
