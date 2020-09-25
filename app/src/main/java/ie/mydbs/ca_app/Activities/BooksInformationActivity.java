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
import android.widget.TextView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ie.mydbs.ca_app.Adapters.BookInfoAdapter;
import ie.mydbs.ca_app.Utilities.Network.NetworkChecker_Fragment;
import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.Book;
import ie.mydbs.ca_app.Repository.Cache.RuntimeCache;
import ie.mydbs.ca_app.Utilities.API_Manager;
import ie.mydbs.ca_app.Utilities.Network.NetworkConnectionStatus;
import ie.mydbs.ca_app.R;
import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.BookDatabaseQueries;
import ie.mydbs.ca_app.Utilities.Menu.iAppMenu;
//import ie.mydbs.ca_app.Utilities.Network.NetworkDetectionReceiver;
import ie.mydbs.ca_app.Utilities.Network.iNetworkChecker;
import ie.mydbs.ca_app.Utilities.Utilities;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
public class BooksInformationActivity extends AppCompatActivity implements iAppMenu, iNetworkChecker {

    private Book book;
    TextView BookName, Author, ISBN, Edition, Volume;
    RecyclerView infoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        //attaches Fragment with snackbar and broadcast receiver
        implementNetworkChecker_Fragment(this);

        //gets relevant Book_ID from the extras from the intent.
        Intent i = getIntent();
        int Book_ID = i.getExtras().getInt("Book_ID");
        book = BookDatabaseQueries.getBookByBookID(Book_ID);

        //checks network connection and if connected makes API request.
        if (NetworkConnectionStatus.isConnectedToNetwork(Utilities.appContext) == true) {
            //Arguments for API call
            String url = getResources().getString(R.string.api_url) + "/Book/GetBookInfo";
            Map<String, String> params = new HashMap<String, String>();
            params.put("Book_ID", String.valueOf(book.Book_ID));
            API_Manager.getAPIInfo(Request.Method.POST, url, "book", params);
            //gives time for data to be received and sets the views
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setBookInfoViews();
                    setExtraInformationView(RuntimeCache.bookInfoList);
                }
            }, 200);

        }
    }
    //Inflates Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //sends selected item to menu handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        handleMenu(item, BooksInformationActivity.this);
        return true;
    }
    //Functions to set data to Views
    private void setBookInfoViews() {
        BookName = (TextView) findViewById(R.id.book_info_name);
        Author = (TextView) findViewById(R.id.book_info_author);
        ISBN = (TextView) findViewById(R.id.book_info_isbn);
        Edition = (TextView) findViewById(R.id.book_info_edition);
        Volume = (TextView) findViewById(R.id.book_info_volume);

        BookName.setText(book.BookName);
        Author.setText(book.Author);
        ISBN.setText(Integer.toString(book.ISBN));
        Edition.setText(Integer.toString(book.Edition));
        Volume.setText(Integer.toString(book.Volume));
    }
    private void setExtraInformationView(ArrayList<HashMap> data) {
        infoView = (RecyclerView) findViewById(R.id.book_info_recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(Utilities.appContext);
        infoView.setLayoutManager(manager);
        RecyclerView.Adapter infoAdapter = new BookInfoAdapter(data);
        infoView.setAdapter(infoAdapter);
    }
    //***       ***         ***
    //interface method for menu sends to the Utilities menu object's handle menu function
    @Override
    public void handleMenu(MenuItem item, FragmentActivity activity) {
        Utilities.menu.handleMenu(item, activity);
    }
    //Contract method sends to NetworkChecker_Fragment for implementation
    @Override
    public void implementNetworkChecker_Fragment(FragmentActivity activity) {
        NetworkChecker_Fragment.implementNetworkChecker(activity);
    }
}

//refactored code.
//    @Override
//    protected void onPause() {
//        NetworkDetectionReceiver.unregisterReceiver(this, Utilities.networkDetectionReceiver);
//        super.onPause();
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        NetworkDetectionReceiver.registerReceiver(this, Utilities.networkDetectionReceiver);
//    }
//
//    @Override
//    protected void onDestroy() {
//        NetworkDetectionReceiver.unregisterReceiver(this, Utilities.networkDetectionReceiver);
//        super.onDestroy();
//
//    }

//    private void getBookInfoAPI(){
//        String url = getResources().getString(R.string.api_url) + "/Book/GetBookInfo";
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//
//                    Map apiResponse = API_Manager.toMap(new JSONObject(response));
//
//                    if (apiResponse.get("status").toString().equals("success")) {
//                        ArrayList<HashMap> bookInfoList = (ArrayList<HashMap>)apiResponse.get("book");
//                        setTextViews();
//                        setInformationView(bookInfoList);
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
//        })
//        {
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Book_ID", String.valueOf(book.Book_ID));
//                return params;
//            }
//        };
//        API_Manager.addToRequestQueue(stringRequest);
//    }
//}
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)