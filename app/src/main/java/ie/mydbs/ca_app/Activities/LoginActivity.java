package ie.mydbs.ca_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import ie.mydbs.ca_app.Utilities.Network.NetworkChecker_Fragment;
import ie.mydbs.ca_app.Utilities.API_Manager;
import ie.mydbs.ca_app.Utilities.Network.NetworkConnectionStatus;
import ie.mydbs.ca_app.R;
//import ie.mydbs.ca_app.Utilities.Network.NetworkDetectionReceiver;

import ie.mydbs.ca_app.Utilities.Network.iNetworkChecker;
import ie.mydbs.ca_app.Utilities.Utilities;

//Mobile Apps project for DBS. By Conal O'Shiel (10523829)

public class LoginActivity extends AppCompatActivity implements iNetworkChecker {

    public EditText email;
    public EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //attaches Fragment with snackbar and broadcast receiver
        implementNetworkChecker_Fragment(this);

        email = (EditText) findViewById(R.id.emailLogin);
        password = (EditText) findViewById(R.id.passwordLogin);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checks network connection and if connected makes API request
                if (NetworkConnectionStatus.isConnectedToNetwork(Utilities.appContext) == true) {

                    //Arguments for API call
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email.getText().toString());
                    params.put("password", password.getText().toString());
                    final String url = getResources().getString(R.string.api_url) + "/User/Login";

                    //API user request
                    API_Manager.getAPIInfo(Request.Method.POST, url, "user", params);

                    //Handles API request and if successful moves to users home page; if not displays relevant API error message
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (API_Manager.getCallSuccess()) {
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(Utilities.appContext, API_Manager.errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 400);

                } else {
                    Toast.makeText(Utilities.appContext, getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Contract method sends to NetworkChecker_Fragment for implementation
    @Override
    public void implementNetworkChecker_Fragment(FragmentActivity activity) {
        NetworkChecker_Fragment.implementNetworkChecker(activity);
    }
}

    //previous implementation; now refactored.
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
//    }

//      Mobile Apps project for DBS. By Conal O'Shiel (10523829)
//    private void getAPIUserData(String url) {
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Map loginResponse = API_Manager.toMap(new JSONObject(response));
//                            if (loginResponse.get("status").toString().equals("success")) {
//                                //Log.v("user", loginResponse.get("user").toString());
//                                User user = ObjectConverter.ConvertObjectListToUser((HashMap)loginResponse.get("user"));
//                                UserDatabaseQueries.AddUserToDatabase(user);
//                                RuntimeCache.loggedInUser = user;
//                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//                                startActivity(i);
//                                LoginActivity.this.finish();
//
//                            } else {
//                                Toast.makeText(Utilities.appContext, "Invalid username or password.", Toast.LENGTH_SHORT).show();
//                                //Log.v("error", loginResponse.get("message").toString());
//                            }
//                        } catch (Exception ex) {
//                            Log.v("Error:", ex.getMessage());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.v("Response is:", error.getMessage());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email.getText().toString());
//                params.put("password", password.getText().toString());
//                return params;
//            }
//        };
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                API_Manager.addToRequestQueue(stringRequest);
//            }
//        },200);
//    }
//}
