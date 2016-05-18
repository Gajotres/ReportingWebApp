package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReportList implements Iterable<ReportFile> {
    private final List<ReportFile> fList = new ArrayList<ReportFile>();
    //...
    @Override
    public Iterator<ReportFile> iterator() {
        return new Iterator<ReportFile> () {
            private final Iterator<ReportFile> iter = fList.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public ReportFile next() {
                return iter.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("No changes allowed!");
            }
        };
    }
}
