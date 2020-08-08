package Assignment3.Module2.filter;

import Assignment3.Module2.MovieDatabase;

public class GenreFilter implements Filter {
    private String myGenre;
    
    public GenreFilter (String genre) {
        myGenre = genre;
    }

    public boolean satisfies(String id) {
        return MovieDatabase.getGenres(id).contains(myGenre);
    }
}
