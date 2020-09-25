package ie.mydbs.ca_app.Repository.Database.BooksTableDatabase;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
//Data access object interface for Book object and Room. Allows Object to be retrieved with use of Querys.
@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBook(Book book);

//    @Query("SELECT * FROM Book")
//    List<Book> getAllBooks();

    @Query("SELECT * FROM Book Where User_ID = :User_ID")
    List<Book> getUsersBooks(int User_ID);


    @Query ("SELECT * FROM Book Where Book_ID = :Book_ID")
    Book getBook(int Book_ID);

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateBook(Book book);

    @Query("DELETE FROM Book")
    void removeAllBooks();
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}

