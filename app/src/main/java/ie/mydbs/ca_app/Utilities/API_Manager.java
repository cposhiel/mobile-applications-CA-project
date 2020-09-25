package ie.mydbs.ca_app.Utilities;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.Book;
import ie.mydbs.ca_app.R;
import ie.mydbs.ca_app.Repository.Cache.RuntimeCache;
import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.BookDatabaseQueries;
import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.User;
import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.UserDatabaseQueries;
//Class of static functions related to API calls.
public class API_Manager extends Application{
    public static String errorMessage;
    private static RequestQueue queue;
    private static boolean isCallSuccessful;
    //Mobile Apps project for DBS. Copyright Conal O'Shiel
    //returns boolean property based on whether the call was successful or not.
    public static boolean getCallSuccess(){
        return isCallSuccessful;
    }
    //checks for a request queue and initaties one if there isn't one.
    public static RequestQueue checkRequestQueue(){
        if(queue == null){
            queue = Volley.newRequestQueue(Utilities.appContext);
        }
        return queue;
    }
    //adds requests to the private statoc request queue
    public static  <T> void addToRequestQueue(Request<T> request)
    {
        queue.add(request);
    }
    //Converts JSONObjects to a Map Object
    private static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object val = object.get(key);
            if(val instanceof JSONArray) {
                val = toList((JSONArray) val);
            } else if (val instanceof JSONObject) {
                val = toMap((JSONObject) val);
            }
            map.put(key, val);
        }
        return map;
    }
    //Converts JSONArrays to List<Object>
    private static List<Object> toList(JSONArray array){
        List<Object> list = new ArrayList<Object>();
        try{
            for(int i = 0; i < array.length(); i++){
                Object val = array.get(i);
                if(val instanceof JSONArray){
                    val = toList((JSONArray)val);
                } else if (val instanceof JSONObject){
                    val = toMap((JSONObject)val);
                }
                list.add(val);
            }

        }catch (Exception ex){
            Log.e("Exception", ex.getMessage());
        }
        return list;
    }
    //initiates an executor service new single thread to return a Bitmap image.
    public static Bitmap getBitmapImage(final String endpoint){
        Bitmap image = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Bitmap> callable = new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                URL url = new URL(Utilities.appContext.getResources().getString(R.string.api_url) + endpoint);
                Bitmap pulledImg = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return pulledImg;
            }
        };
        Future<Bitmap> fImage = executor.submit(callable);
        try {
            image = fImage.get();
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Future_Error", "Error retrieving user from database.");
        }
        executor.shutdown();
        return image;
    }
    //generic API call, allows for arguments, including request method, key, url and params to be passed through and fetches the relevant
    //data from the API.
    public static void getAPIInfo (int requestMethod,  String url,  final String call,  final Map<String,String> apiParams) {
        isCallSuccessful = false;

            final StringRequest stringRequest = new StringRequest(requestMethod, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {

                        Map apiResponse = API_Manager.toMap(new JSONObject(response));
                        if (apiResponse.get("status").toString().equals("success")) {
                            sortCall(call, apiResponse);
                            isCallSuccessful = true;
                            Log.v("Success", "SUCCESS");
                        } else {
                            errorMessage = apiResponse.get("message").toString();
                        }
                    } catch (JSONException e) {
                        Log.v("Error ", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("Response is: ", error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = apiParams;
                    return params;
                }
            };
            API_Manager.addToRequestQueue(stringRequest);
    }
    //Switch function that sorts the Data into relevant the runtime cache and database based on the call passed through to the API.
    private static void sortCall(String call, Map apiResponse){
        switch (call){
            case "user":
                RuntimeCache.loggedInUser = ObjectConverter.ConvertObjectMapToUser((HashMap) apiResponse.get(call));
                UserDatabaseQueries.AddUserToDatabase(RuntimeCache.loggedInUser);
                break;
            case "books":
                List<Book> bookList = ObjectConverter.ConvertObjectListToBookList((ArrayList)apiResponse.get(call));
                RuntimeCache.usersBooks = (ArrayList<Book>) bookList;
                BookDatabaseQueries.addBooksToDatabase(bookList);
                break;
            case "book":
                ArrayList<HashMap> bookInfoList = (ArrayList)apiResponse.get(call);
                RuntimeCache.bookInfoList = bookInfoList;
                break;
            default:
                break;
        }
    }
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}
