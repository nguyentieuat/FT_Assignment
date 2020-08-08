package Assignment3.Module2.filter;

import Assignment3.Module2.MovieDatabase;

public class YearAfterFilter implements Filter {
    private int myYear;
    
    public YearAfterFilter(int year) {
        myYear = year;
    }

    public boolean satisfies(String id) {
        return MovieDatabase.getYear(id) >= myYear;
    }
}
