package com.twu.biblioteca.action;

import com.twu.biblioteca.collection.Movies;
import com.twu.biblioteca.view.ConsoleView;

public class ListMovies implements MenuAction {
    private ConsoleView consoleView;
    private Movies movies;

    public ListMovies(ConsoleView consoleView, Movies movies) {
        this.consoleView = consoleView;
        this.movies = movies;
    }

    @Override
    public void performAction() {
        consoleView.print(movies.toString());
    }
}
