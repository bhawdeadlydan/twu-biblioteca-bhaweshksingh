package com.twu.biblioteca;

import com.twu.biblioteca.action.ListBooks;
import com.twu.biblioteca.action.MenuAction;
import com.twu.biblioteca.action.Quit;
import com.twu.biblioteca.collection.Books;
import com.twu.biblioteca.constants.Messages;
import com.twu.biblioteca.controller.LibrarianMenuExecutor;
import com.twu.biblioteca.menu.Menu;
import com.twu.biblioteca.model.Book;
import com.twu.biblioteca.view.ConsoleView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class BibliotecaAppTest {
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private BibliotecaApp bibliotecaApp;
    private HashMap<Integer, String> menuMap;
    private Menu menu;
    private Books books;
    LibrarianMenuExecutor menuExecutor;

    @Mock
    ConsoleView consoleView;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ArrayList<Book> availableBookList = new ArrayList<Book>();
        availableBookList.add(new Book("Book 1", "JK Rowling", 2003));
        availableBookList.add(new Book("Book 2", "Arthur Conan Doyle", 1886));
        availableBookList.add(new Book("Book 3", "Agatha Christie", 1800));

        ArrayList<Book> checkedOutBookList = new ArrayList<Book>();
        books = new Books(availableBookList, checkedOutBookList);
        menuMap = new HashMap<Integer, String>();
        menuMap.put(1, Messages.LIST_BOOKS);
        menuMap.put(2, Messages.QUIT);
        menu = new Menu(menuMap);
        consoleView = new ConsoleView(new BufferedReader(new InputStreamReader(System.in)));
        HashMap<Integer, MenuAction> menuActionMap = new HashMap<Integer, MenuAction>();
        menuActionMap.put(1, new ListBooks(books, consoleView));
        menuActionMap.put(2, new Quit());
        menuExecutor = new LibrarianMenuExecutor(menuActionMap, consoleView);
        bibliotecaApp = new BibliotecaApp(consoleView, menu, menuExecutor);
        System.setOut(new PrintStream(outputStream));

    }

    @Test
    public void shouldDisplayWelcomeMessageWhenBibliotecaAppStarts() throws IOException {
        ConsoleView consoleViewStub = mock(ConsoleView.class);
        ConsoleView consoleViewStub1 = mock(ConsoleView.class);
        Books booksStub = mock(Books.class);
        HashMap<Integer, MenuAction> menuActionMap = new HashMap<Integer, MenuAction>();
        menuActionMap.put(1, new ListBooks(booksStub, consoleView));
        menuActionMap.put(2, new Quit());
        LibrarianMenuExecutor menuExecutor = new LibrarianMenuExecutor(menuActionMap, consoleViewStub1);
        BibliotecaApp bibliotecaApp = new BibliotecaApp(consoleViewStub, menu, menuExecutor);
        when(consoleViewStub1.read()).thenReturn(1, 2);

        bibliotecaApp.start();

        verify(consoleViewStub, times(2)).print(Messages.WELCOME_MESSAGE);
    }

    @Test
    public void shouldDisplayListOfAllLibraryBooksWithNameAuthorYearOfPublication() throws IOException {
        ConsoleView consoleViewStub1 = mock(ConsoleView.class);
        ConsoleView consoleViewStub2 = mock(ConsoleView.class);
        HashMap<Integer, MenuAction> menuActionMap = new HashMap<Integer, MenuAction>();
        menuActionMap.put(1, new ListBooks(books, consoleViewStub2));
        menuActionMap.put(2, new Quit());
        LibrarianMenuExecutor menuExecutor = new LibrarianMenuExecutor(menuActionMap, consoleViewStub1);
        BibliotecaApp bibliotecaApp = new BibliotecaApp(consoleView, menu, menuExecutor);

        when(consoleViewStub1.read()).thenReturn(1, 2);

        bibliotecaApp.start();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(consoleViewStub2, times(1)).print(stringArgumentCaptor.capture());
        List<String> capturedStrings = stringArgumentCaptor.getAllValues();
        String expectedString = "\nName\tAuthor\tPublication Year\nBook 1\tJK Rowling\t2003\nBook 2\tArthur Conan Doyle\t1886\nBook 3\tAgatha Christie\t1800";

        assertThat(capturedStrings.get(0), is(expectedString));
    }

    @Test
    public void shouldDisplayMenuOptions() throws IOException {
        ConsoleView consoleViewStub1 = mock(ConsoleView.class);
        ConsoleView consoleViewStub2 = mock(ConsoleView.class);
        HashMap<Integer, MenuAction> menuActionMap = new HashMap<Integer, MenuAction>();
        menuActionMap.put(1, new ListBooks(books, consoleView));
        menuActionMap.put(2, new Quit());
        LibrarianMenuExecutor menuExecutor = new LibrarianMenuExecutor(menuActionMap, consoleViewStub2);
        BibliotecaApp bibliotecaApp = new BibliotecaApp(consoleViewStub1, menu, menuExecutor);
        when(consoleViewStub2.read()).thenReturn(1, 2);
        bibliotecaApp.start();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(consoleViewStub1, times(6)).print(stringArgumentCaptor.capture());

        List<String> capturedStrings = stringArgumentCaptor.getAllValues();

        String actualMenu = capturedStrings.get(1);
        String expectedMenu = "\n1 " + Messages.LIST_BOOKS +
                "\n2 " + Messages.QUIT;

        assertEquals(expectedMenu, actualMenu);
    }

    @Test
    public void shouldQuitWhenOptionIsSelected() throws IOException {
        Quit quitAction = mock(Quit.class);
        ConsoleView consoleViewStub1 = mock(ConsoleView.class);
        HashMap<Integer, MenuAction> menuActionMap = new HashMap<Integer, MenuAction>();
        menuActionMap.put(1, new ListBooks(books, consoleView));
        menuActionMap.put(2, quitAction);
        menuExecutor = new LibrarianMenuExecutor(menuActionMap, consoleViewStub1);
        BibliotecaApp bibliotecaApp = new BibliotecaApp(consoleView, menu, menuExecutor);
        when(consoleViewStub1.read()).thenReturn(2);

        bibliotecaApp.start();

        assertFalse(menuExecutor.executeUserCommand());
    }

    @Test
    public void shouldContinueToGiveMenuUntilQuitIsCalled() throws IOException {
        Quit quitAction = mock(Quit.class);
        ConsoleView consoleViewStub1 = mock(ConsoleView.class);
        HashMap<Integer, MenuAction> menuActionMap = new HashMap<Integer, MenuAction>();
        menuActionMap.put(1, new ListBooks(books, consoleView));
        menuActionMap.put(2, quitAction);
        LibrarianMenuExecutor menuExecutor = new LibrarianMenuExecutor(menuActionMap, consoleViewStub1);
        BibliotecaApp bibliotecaApp = new BibliotecaApp(consoleView, menu, menuExecutor);
        when(consoleViewStub1.read()).thenReturn(1, 1, 2);

        bibliotecaApp.start();

        verify(consoleViewStub1, times(3)).read();
        assertFalse(menuExecutor.executeUserCommand());

    }


    @After
    public void tearDown() {
        System.setOut(null);
    }
}
