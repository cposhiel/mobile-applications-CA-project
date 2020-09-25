//package ie.mydbs.ca_app.Utilities.Network;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//
//import ie.mydbs.ca_app.Utilities.Network.NetworkConnectionStatus;
//import ie.mydbs.ca_app.Utilities.Utilities;
//
//public class NetworkDetectionReceiver extends BroadcastReceiver{
//
///*      This code is no longer in use, having been replaced by the code in the network checker fragment.
//*/
//
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Bundle extras = intent.getExtras();
//        if(extras != null) {
//            if(!NetworkConnectionStatus.isConnectedToNetwork(Utilities.appContext)) {
//                String state = NetworkConnectionStatus.getNetworkStateString(context);
//                //Toast.makeText(context, state, Toast.LENGTH_SHORT).show();
//                //Make snack bar
//
//            }
//        }
//  }
//
//    public static void registerReceiver(Activity activity, NetworkDetectionReceiver networkDetectionReceiver){
//
//        try {
//            IntentFilter iFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
//            final Intent intentReceiver = activity.registerReceiver(networkDetectionReceiver, iFilter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void unregisterReceiver(Activity activity, NetworkDetectionReceiver networkDetectionReceiver){
//        try {
//            activity.unregisterReceiver(networkDetectionReceiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
