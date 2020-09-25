package ie.mydbs.ca_app.Repository.Cache;

import java.util.ArrayList;
import java.util.HashMap;

import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.Book;
import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.User;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
public class RuntimeCache {
    //Runtime Cache, this is where the data to be displayed is stored in memory.
    //If Network is available the data comes from an API call if not it comes from the Database
    //with the exception of the user who is only requested from the API when logging in.
    //if logged in they are retrieved from the database.
    public static User loggedInUser;
    public static ArrayList<Book> usersBooks = new ArrayList<Book>();
    public static ArrayList<HashMap> bookInfoList = new ArrayList<HashMap>();

}
