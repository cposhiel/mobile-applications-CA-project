package ie.mydbs.ca_app.Logout_Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import ie.mydbs.ca_app.Activities.LoginActivity;
import ie.mydbs.ca_app.Repository.Cache.RuntimeCache;
import ie.mydbs.ca_app.Repository.Database.AppDatabase;
import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.BookDatabaseQueries;
import ie.mydbs.ca_app.Repository.Database.UserDatabaseTable.UserDatabaseQueries;
import ie.mydbs.ca_app.Utilities.API_Manager;
import ie.mydbs.ca_app.Utilities.Network.NetworkConnectionStatus;
import ie.mydbs.ca_app.R;
import ie.mydbs.ca_app.Utilities.Utilities;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
public class Logout_Dialog extends AppCompatDialogFragment implements iLogout_Dialog {
    //This class is a dialog fragment with a singular static instance connected to the
    //activites via the AppMenu which connects to the instance with an interface object.
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.logout_dialog, null);
        builder.setView(view);
        view.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkConnectionStatus.isConnectedToNetwork(Utilities.appContext)) {
                    logoutAPI(getActivity());
                }
                else{
                    logOut(getActivity());
                }
            }
        });
        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return builder.create();
    }

    //Method to logOut of API
    private void logoutAPI(final Activity activity){
        String url = Utilities.appContext.getResources().getString(R.string.api_url) + "/User/Logout";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                logOut(activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("LogoutFailed", "API Log out call failed");
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                API_Manager.addToRequestQueue(stringRequest);
            }
        },200);

    }
    //Method to logOut of system.
    private void logOut(final Activity activity){

        clearDB();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent iLogin = new Intent (activity, LoginActivity.class);
                activity.startActivity(iLogin);
                activity.finish();
                Logout_Dialog.this.dismiss();
                Toast.makeText(Utilities.appContext,"Logout successful",Toast.LENGTH_SHORT).show();
            }
        },200);

    }
//Method to clear room_database of user
    private void clearDB(){
        RuntimeCache.bookInfoList = null;
        RuntimeCache.usersBooks = null;
        RuntimeCache.loggedInUser=null;
        BookDatabaseQueries.removeBooks();
        UserDatabaseQueries.removeUser();
        AppDatabase.destroyInstance();
    }
    //opens Logout Dialog Fragment
    public void openLogoutDialogue(FragmentActivity activity) {
        this.show(activity.getSupportFragmentManager(), "Logout");
    }
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}
