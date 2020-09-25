package ie.mydbs.ca_app.Repository.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.Book;
import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.BookDao;

import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.User;
import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.UserDao;
import ie.mydbs.ca_app.Utilities.Utilities;
//Room database for persistent storage of a users data while they remain logged in.
//Means most recent data will persist when network is down, allowing user to still interact with the app.
@Database(entities = {User.class, Book.class,}, version = 22, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    //Mobile Apps project for DBS. By Conal O'Shiel (10523829)
    //To do -- migrate executor service threads and create on static multi-thread in here?? Further research required.
    private static AppDatabase INSTANCE;
    public abstract UserDao userDao();
    public abstract BookDao bookDao();
    public static AppDatabase getDatabase() {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(Utilities.appContext, AppDatabase.class, "AppDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
