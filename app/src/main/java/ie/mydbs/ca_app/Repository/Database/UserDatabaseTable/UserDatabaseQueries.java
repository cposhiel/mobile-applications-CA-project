package ie.mydbs.ca_app.Repository.Database.UserDatabaseTable;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ie.mydbs.ca_app.Utilities.Utilities;
//Querys used in the App for Userobjects. All exist off the main thread. Some are run through
//executor service to allow an object to be returned with the use of Future.
public class UserDatabaseQueries {
    public static void AddUserToDatabase(final User user) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                Utilities.database.userDao().addUser(user);
            }

        };
        t.start();
    }
    public static void removeUser() {
        final Thread t = new Thread() {
            @Override
            public void run() {
                Utilities.database.userDao().removeAllUsers();
            }

        };
        t.start();
    }

    public static User getUserFromDatabase(){
        User user = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<User> callable = new Callable<User>() {
            @Override
            public User call() throws Exception {
                User userInDB = Utilities.database.userDao().getUser();
                return userInDB;
            }
        };
        Future<User> future = executor.submit(callable);
        try {
            user = future.get(3000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Future_Error", e.getMessage());
        }
        executor.shutdown();
        return user;
    }
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}
