package ie.mydbs.ca_app.Utilities.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import ie.mydbs.ca_app.Utilities.API_Manager;
//Mobile Apps project for DBS. Copyright Conal O'Shiel
public class NetworkConnectionStatus {
    //static boolean method that returns true or false based on the status of the network connection
    public static boolean isConnectedToNetwork(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    //Mobile Apps project for DBS. Copyright Conal O'Shiel
}
