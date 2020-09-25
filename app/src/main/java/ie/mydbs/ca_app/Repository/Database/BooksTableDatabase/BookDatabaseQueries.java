package ie.mydbs.ca_app.Repository.Database.BooksTableDatabase;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ie.mydbs.ca_app.Utilities.Utilities;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
//Querys used in the App for Book objects. All exist off the main thread. Some are run through
//executor service to allow an object to be returned with the use of Future.
public class BookDatabaseQueries {
    public static void addBooksToDatabase(final List<Book> books){
        Thread t = new Thread(){
            @Override
            public void run() {
                for (Book book:
                        books) {
                    Utilities.database.bookDao().addBook(book);
                }
            }
        };
        t.start();
    }
    public static void removeBooks(){
        Thread t = new Thread(){
            @Override
            public void run() {
                Utilities.database.bookDao().removeAllBooks();
            }
        };
        t.start();
    }
    public static Book getBookByBookID(final int Book_ID){
        Book book = null;
        //Executor service is a service that allows asynchronus tasking. Async-Task is deprecated
        // //so I have opted to use this to return datatypes by running a callable thread followed by Future.
        //I have attached a timeout of 3 seconds for the operations. In case of blocking
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Book> callable = new Callable<Book>() {
            @Override
            public Book call() throws Exception {
                return Utilities.database.bookDao().getBook(Book_ID);
            }
        };
        Future<Book> fBook = executor.submit(callable);
        try {
            book = fBook.get(3000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Future_Error", e.getMessage());
        }
        executor.shutdown();
        return book;
    }
    public static ArrayList<Book> getBooksByUser_ID(final int User_ID){
        List<Book> books = new ArrayList<Book>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<List<Book>> callable = new Callable<List<Book>>() {
            @Override
            public List<Book> call() throws Exception {
                List<Book> booksInDB = Utilities.database.bookDao().getUsersBooks(User_ID);
                return booksInDB;
            }
        };
        Future<List<Book>> fBooks = executor.submit(callable);
        try {
            books.addAll(fBooks.get(3000, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Future_Error", e.getMessage());
        }
        executor.shutdown();
        return (ArrayList<Book>) books;
    }
}
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
