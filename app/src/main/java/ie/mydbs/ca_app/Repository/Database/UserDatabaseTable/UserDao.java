package ie.mydbs.ca_app.Repository.Database.UserDatabaseTable;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
//Data access object interface for Student object and Room. Allows Object to be retrieved with use of Querys.
@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Query("SELECT * FROM User LIMIT 1")
    User getUser();

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateUser(User user);

    @Query("DELETE FROM User")
    void removeAllUsers();

}
