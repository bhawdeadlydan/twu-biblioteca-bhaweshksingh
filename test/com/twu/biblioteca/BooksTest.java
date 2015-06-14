package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class BooksTest {
    private Books books;

    @Before
    public void setUp() {
        ArrayList<String[]> bookList = new ArrayList<String[]>();
        bookList.add(new String[]{"Book 1", "JK Rowling", "2003"});
        bookList.add(new String[]{"Book 2", "Arthur Conan Doyle", "1886"});
        bookList.add(new String[]{"Book 3", "Agatha Christie", "1800"});

        books = new Books(bookList);
    }

    @Test
    public void shouldGiveAllBooksFromLibrary() {

        String actualBooksInLibrary = books.toString();
        String expectedBooksInLibrary = "\nName\tAuthor\tPublication Year\nBook 1\tJK Rowling\t2003\nBook 2\tArthur Conan Doyle\t1886\nBook 3\tAgatha Christie\t1800";

        assertThat(actualBooksInLibrary, is(expectedBooksInLibrary));
    }

}