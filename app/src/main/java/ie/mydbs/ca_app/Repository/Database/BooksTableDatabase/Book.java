package ie.mydbs.ca_app.Repository.Database.BooksTableDatabase;

//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
//Book object. entity for database
@Entity
public class Book {
    @PrimaryKey
    public int Book_ID;
    public int User_ID;
    public String Borrowed_Date;
    public String Due_Date;
    public String Last_Updated;
    public String BookName;
    public int Volume;
    public int Edition;
    public int ISBN;
    public String Author;
    public byte[] Author_Image;

    public Book (int Book_ID, int User_ID, String Borrowed_Date, String Due_Date, String Last_Updated,
                 String BookName,int Volume, int Edition, int ISBN, String Author, byte[] Author_Image)
    {
        this.Book_ID = Book_ID;
        this.User_ID = User_ID;
        this.Borrowed_Date = Borrowed_Date;
        this.Due_Date = Due_Date;
        this.Last_Updated = Last_Updated;
        this.BookName = BookName;
        this.Volume = Volume;
        this.Edition = Edition;
        this.ISBN = ISBN;
        this.Author = Author;
        this.Author_Image = Author_Image;
    }
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}
