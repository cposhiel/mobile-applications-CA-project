package ie.mydbs.ca_app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import ie.mydbs.ca_app.Logout_Dialog.Logout_Dialog;
import ie.mydbs.ca_app.Utilities.API_Manager;
import ie.mydbs.ca_app.Repository.Database.AppDatabase;
import ie.mydbs.ca_app.R;
import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.BookDatabaseQueries;
import ie.mydbs.ca_app.Repository.Cache.RuntimeCache;
import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.UserDatabaseQueries;
import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.User;
import ie.mydbs.ca_app.Utilities.Menu.AppMenu;
//import ie.mydbs.ca_app.Utilities.Network.NetworkDetectionReceiver;
import ie.mydbs.ca_app.Utilities.Network.NetworkConnectionStatus;
import ie.mydbs.ca_app.Utilities.Utilities;

public class SplashActivity extends Activity {

    //Mobile Apps project for DBS. By Conal O'Shiel (10523829)

    private static boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Initiate utilities
        Utilities.appContext = getApplicationContext();
        Utilities.database = AppDatabase.getDatabase();
        Utilities.menu = new AppMenu();
        Utilities.logout_dialog = new Logout_Dialog();

        //checks for request queue and initates one if one does not exist.
        API_Manager.checkRequestQueue();

        //Gets a user from the database (if there is one).
        final User user = UserDatabaseQueries.getUserFromDatabase();

        //sets boolean loggedIn value based on whether a user is present in the database.
        checkLoggedIn(user);

        //Handles new activity intents, dealying the request until the relevant prior data is gathered.
        //if there is a user in the database, then the user is logged in and so goes straight to their home page.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loggedIn == true) {
                  Intent  iHome = new Intent (SplashActivity.this, HomeActivity.class);
                  RuntimeCache.loggedInUser = user;
                  RuntimeCache.usersBooks = BookDatabaseQueries.getBooksByUser_ID(RuntimeCache.loggedInUser.User_ID);
                  startActivity(iHome);
                  finish();
                } else {
                    //Redirects user to Log in page.
                    Intent iLogin = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(iLogin);
                    finish();
                }
            }
        },300);

    }

    //sets boolean loggedIn value based on whether a user is present in the database.
    public void checkLoggedIn(User user) {
        loggedIn = user != null;
    }

//    @Override
//    protected void onDestroy() {
//        NetworkDetectionReceiver.unregisterReceiver(this, Utilities.networkDetectionReceiver);
//        super.onDestroy();
//    }
//          Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}