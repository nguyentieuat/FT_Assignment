package Assignment3.Module2.filter;

import Assignment3.Module2.MovieDatabase;

public class DirectorsFilter implements Filter {
    String directorsList;

    public DirectorsFilter(String directors) {
        directorsList = directors;
    }

    public boolean satisfies(String id) {
        String[] directorsSplit = directorsList.split(",");
        for (int i = 0; i < directorsSplit.length; i++) {
            if (MovieDatabase.getDirector(id).indexOf(directorsSplit[i]) != -1) {
                return true;
            }
        }
        return false;
    }
}
