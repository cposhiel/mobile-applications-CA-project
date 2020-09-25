package ie.mydbs.ca_app.Repository.Database.UserDatabaseTable;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
//User object. entity for database
@Entity
public class User{

    @PrimaryKey
    public final int User_ID;
    public String FullName;
    public String Email;
    public String Username;
    public String Password;
    public String User_Type;
    public byte[] Avatar;
    public String DateCreated;
    public String LastLogin;
    public int Active;


    public User(int User_ID, String FullName, String Email, String Username,
                String Password, String User_Type, byte[] Avatar, String DateCreated,
                String LastLogin, int Active) {

        this.User_ID = User_ID;
        this.FullName = FullName;
        this.Email = Email;
        this.Username = Username;
        this.Password = Password;
        this.User_Type = User_Type;
        this.Avatar = Avatar;
        this.DateCreated = DateCreated;
        this.LastLogin = LastLogin;
        this.Active = Active;
    }
}
