package Assignment3.Module2.filter;

import java.util.ArrayList;

public class AllFilters implements Filter {
    ArrayList<Filter> filters;

    public AllFilters() {
        filters = new ArrayList<Filter>();
    }

    public void addFilter(Filter f) {
        filters.add(f);
    }

    public boolean satisfies(String id) {
        for (Filter f : filters) {
            if (!f.satisfies(id)) {
                return false;
            }
        }

        return true;
    }
}
