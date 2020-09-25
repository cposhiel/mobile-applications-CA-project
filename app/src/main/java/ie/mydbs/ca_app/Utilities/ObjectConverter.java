package ie.mydbs.ca_app.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.Book;
import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.User;

import static ie.mydbs.ca_app.Utilities.ObjectConverter.FormatType.*;
//A class dedicated to convert objects from type to another
public class ObjectConverter {
    //Mobile Apps project for DBS. By Conal O'Shiel (10523829)
    public static User ConvertObjectMapToUser(HashMap userMap){

        int User_ID = Integer.parseInt(userMap.get("User_ID").toString());
        String FullName = userMap.get("FullName").toString();
        String Email = userMap.get("Email").toString();
        String Username = userMap.get("Username").toString();
        String Password = userMap.get("Password").toString();
        String User_Type = userMap.get("User_Type").toString();
        String DateCreated = userMap.get("DateCreated").toString();
        String LastLogin = userMap.get("LastLogin").toString();
        int Active = Integer.parseInt(userMap.get("Active").toString());

        Bitmap bmImage = API_Manager.getBitmapImage(userMap.get("Avatar").toString());
        byte[] Avatar = convertBitmapToByteArray(bmImage);

        User user = new User(User_ID, FullName, Email, Username, Password, User_Type, Avatar, DateCreated, LastLogin, Active);

        return user;
    }

    public static List<Book> ConvertObjectListToBookList(List<Object> APIDataList){

        List<Book> bookList= new ArrayList<Book>();

        int i;
        for( i=0; i < APIDataList.size(); i++)
        {
            Map<String, Object> bookMap =(HashMap)APIDataList.get(i);

            int Book_ID = Integer.parseInt(bookMap.get("Book_ID").toString());
            int User_ID = Integer.parseInt(bookMap.get("User_ID").toString());
            String Borrowed_Date = bookMap.get("Borrowed_Date").toString();
            String Due_Date = bookMap.get("Due_Date").toString();
            String Last_Updated = bookMap.get("Last_Updated").toString();
            String BookName = bookMap.get("BookName").toString();
            int Volume = Integer.parseInt(bookMap.get("Volume").toString());
            int Edition = Integer.parseInt(bookMap.get("Edition").toString());
            int ISBN = Integer.parseInt(bookMap.get("ISBN").toString());
            String Author = bookMap.get("Author").toString();

            Bitmap bmImage = API_Manager.getBitmapImage(bookMap.get("Author_Image").toString());
            byte[] Author_Image = convertBitmapToByteArray(bmImage);


            Book book = new Book(Book_ID, User_ID, Borrowed_Date, Due_Date, Last_Updated, BookName,
                                Volume, Edition, ISBN, Author, Author_Image);
            bookList.add(book);
        }
        return bookList;

    }
    //Converts bitMap to byte[] for database storage
    private static byte[] convertBitmapToByteArray(Bitmap bmImage){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmImage.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte[] imageInBytes = outputStream.toByteArray();
        return imageInBytes;

    }

    //Converts byte[] to Bitmap to display on UI from database
    public static Bitmap convertByteArrayToBitmap(byte[] imgBytes){
        Bitmap image = BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.length);
        return image;
    }

    //converts Strings of Dates from the API to Date objects
    public static Date convertStringToDate(String stringDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
           date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.getMessage();
        }
        return date;
    }

    //Formats String dates from the API into one of two types using and enum for selection
    public static String formatDate(String strDate, FormatType formatType){
        Date date = convertStringToDate(strDate);
        String formattedDate = null;
        SimpleDateFormat dateFormat;
        switch (formatType) {
            case date:
                 dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                formattedDate = dateFormat.format(date);
                break;
            case dateTime:
                dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
                formattedDate = dateFormat.format(date);
                break;
        }
        return formattedDate;
    }
    //enum for formatDate function
    public enum FormatType{
        date,
        dateTime
    }
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}
