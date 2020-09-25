package ie.mydbs.ca_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import ie.mydbs.ca_app.Utilities.Network.NetworkChecker_Fragment;
import ie.mydbs.ca_app.Utilities.API_Manager;
import ie.mydbs.ca_app.Adapters.BooksAdapter;
import ie.mydbs.ca_app.Utilities.Network.NetworkConnectionStatus;
import ie.mydbs.ca_app.R;
import ie.mydbs.ca_app.Utilities.Network.iNetworkChecker;
import ie.mydbs.ca_app.Utilities.ObjectConverter;
import ie.mydbs.ca_app.Repository.Cache.RuntimeCache;
import ie.mydbs.ca_app.Utilities.Menu.iAppMenu;
import ie.mydbs.ca_app.Utilities.Utilities;

public class HomeActivity extends AppCompatActivity implements BooksAdapter.iOnBookListener, iAppMenu, iNetworkChecker{
    TextView Name, Email, LastLogin, noBooks;
    ImageView Avatar;
    RecyclerView bookView;
    //Mobile Apps project for DBS. By Conal O'Shiel (10523829)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //sets users details to relevant TextViews.
        setUserDetails();

        //attaches Fragment with snackbar and broadcast receiver, interface method below.
        implementNetworkChecker_Fragment(this);

        //Makes relevant API request if connected to the Network
        if (NetworkConnectionStatus.isConnectedToNetwork(Utilities.appContext) == true) {
            //Arguments for API call
            String url = getResources().getString(R.string.api_url) + "/Book/GetUserBooks";
            Map<String, String> params = new HashMap<String, String>();
            params.put("User_ID", String.valueOf(RuntimeCache.loggedInUser.User_ID));
            API_Manager.getAPIInfo(Request.Method.POST, url, "books", params);

            //gives time for data to be received and sets the recycler views
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    setBookView();
                }
            }, 200);
        } else {
            //sets recycler view with data from database
            setBookView();
        }
    }

    //Inflates Menu onCreate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //sends selected item to menu handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        handleMenu(item, HomeActivity.this);
        return true;
    }

    //sets Recycler view data of users books. Displays a 'no books' string if none exist.
    private void setBookView() {
        noBooks = (TextView) findViewById(R.id.noBooks);
        bookView = (RecyclerView) findViewById(R.id.books_recycler_view);
        if (RuntimeCache.usersBooks.isEmpty()) {
            bookView.setVisibility(View.GONE);
            noBooks.setVisibility(View.VISIBLE);
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Utilities.appContext, LinearLayoutManager.HORIZONTAL, false);

            bookView.setLayoutManager(layoutManager);

            RecyclerView.Adapter booksAdapter = new BooksAdapter(RuntimeCache.usersBooks, this);

            bookView.setAdapter(booksAdapter);
        }
    }

    //sets users details to relevant TextViews.
    private void setUserDetails() {
        Name = (TextView) findViewById(R.id.full_name);
        Email = (TextView) findViewById(R.id.email);
        LastLogin = (TextView) findViewById(R.id.loginDate);
        Avatar = (ImageView) findViewById(R.id.avatar);
        Name.setText(RuntimeCache.loggedInUser.FullName);
        Email.setText(RuntimeCache.loggedInUser.Email);
        LastLogin.setText(ObjectConverter.formatDate(RuntimeCache.loggedInUser.LastLogin, ObjectConverter.FormatType.dateTime));
        Avatar.setImageBitmap(ObjectConverter.convertByteArrayToBitmap(RuntimeCache.loggedInUser.Avatar));

    }

    //on book click method as per iOnBookClickListener interface. Sends user to bookinfo activity with the relevant books data.
    @Override
    public void onBookClick(int position) {
        //checks for network connection and allows access to books info activity if the connection is there.
        // Denies access otherwise; due to books info data not being stored on database but only accessed via API.
        if (NetworkConnectionStatus.isConnectedToNetwork(Utilities.appContext)) {
            Intent i = new Intent(HomeActivity.this, BooksInformationActivity.class);
            i.putExtra("Book_ID", RuntimeCache.usersBooks.get(position).Book_ID);
            startActivity(i);
        } else {
            Toast.makeText(Utilities.appContext, "Network connection required", Toast.LENGTH_SHORT).show();
        }
    }

    //interface method for menu sends to the Utilities menu object's handle menu function
    @Override
    public void handleMenu(MenuItem item, FragmentActivity activity) {
        Utilities.menu.handleMenu(item, activity);
    }
    //interface method for network checker, contract purposes
    @Override
    public void implementNetworkChecker_Fragment(FragmentActivity activity) {
        NetworkChecker_Fragment.implementNetworkChecker(activity);
    }
}
    //Refactored code.
//    @Override
//    protected void onPause() {
//        NetworkDetectionReceiver.unregisterReceiver(this, Utilities.networkDetectionReceiver);
//        super.onPause();
//
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        NetworkDetectionReceiver.registerReceiver(this, Utilities.networkDetectionReceiver);
//
//    }
//
//          Mobile Apps project for DBS. By Conal O'Shiel (10523829)
//    @Override
//    protected void onDestroy() {
//        NetworkDetectionReceiver.unregisterReceiver(this, Utilities.networkDetectionReceiver);
//        super.onDestroy();
//
//    }


//    private void getAPIBooksData(String url) {
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//
//                    Map apiResponse = API_Manager.toMap(new JSONObject(response));
//
//                    if (apiResponse.get("status").toString().equals("success")) {
//                        List<Object> books = (ArrayList) apiResponse.get("books");
//                        List<Book> bookList = ObjectConverter.ConvertObjectListToBookList(books);
//                        RuntimeCache.usersBooks = (ArrayList<Book>) bookList;
//                        BookDatabaseQueries.addBooksToDatabase(bookList);
//                        setRecyclerView();
//                        Log.v("Success", "SUCCESS");
//                    } else {
//                        Log.v("error", apiResponse.get("message").toString());
//                    }
//
//                } catch (JSONException e) {
//                    Log.v("Error ", e.getMessage());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.v("Response is: ", error.getMessage());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("User_ID", String.valueOf(RuntimeCache.loggedInUser.User_ID));
//                return params;
//            }
//        };
//
//        API_Manager.addToRequestQueue(stringRequest);
//
//    }
//}