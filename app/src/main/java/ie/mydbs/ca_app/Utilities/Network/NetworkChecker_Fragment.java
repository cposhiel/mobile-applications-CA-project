package ie.mydbs.ca_app.Utilities.Network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

//import ie.mydbs.ca_app.Utilities.Network.NetworkDetectionReceiver;
import ie.mydbs.ca_app.Utilities.Utilities;


/*This fragment is a headless fragment running the Broadcast receiver without a UI,
    when it receives a null it triggers the  passed activities view and implements a snackbar
    the fragments lifecycle means I don't need to  register and unregister  the receiver in every
    activity lifecycle, I can instead just add the fragment to the activity. I have added an static function
    for fragment implementation too. Though it only works with FragmentActivities.
 */
public class NetworkChecker_Fragment extends Fragment{

        //Mobile Apps project for DBS. Copyright Conal O'Shiel

        //Broadcast Receiver to receive netConnection broadcast
        public BroadcastReceiver netReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if(extras != null) {
                    checkConnection();
                }
            }
        };

        private Snackbar snackbar;
    private Activity activity;


    //Constructors one overloaded for the Activity + Snackbar and one default for system instantiation.
    public NetworkChecker_Fragment(){}
    public NetworkChecker_Fragment(Activity activity) {
        super();
        this.activity = activity;
        snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "No internet connection detected.", Snackbar.LENGTH_INDEFINITE);
    }

    //Fragment lifecycle, registering and unregistering the broadcast.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(activity);
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(activity);
    }
    @Override
    public void onPause() {
        unregisterReceiver();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    //check connection with Snackbar for no connection message.
    public void checkConnection() {
        if(!NetworkConnectionStatus.isConnectedToNetwork(Utilities.appContext)){
            snackbar.show();
        }
        else {
            snackbar.dismiss();
        }
    }

    //Intent to register receiver
    private void registerReceiver(Activity activity){
        try {
            IntentFilter iFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            final Intent intentReceiver = activity.registerReceiver(netReceiver, iFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //function to unregister Recevier
    private void unregisterReceiver(){
        try {
            activity.unregisterReceiver(netReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //static function for activities to add this Fragment
    public static void implementNetworkChecker(FragmentActivity activity){
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(new NetworkChecker_Fragment(activity),"CheckNet");
        ft.commit();
    }
    //Mobile Apps project for DBS. Copyright Conal O'Shiel
}
