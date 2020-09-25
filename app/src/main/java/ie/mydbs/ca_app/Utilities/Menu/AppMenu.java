package ie.mydbs.ca_app.Utilities.Menu;

import android.view.MenuItem;

import androidx.fragment.app.FragmentActivity;

import ie.mydbs.ca_app.Logout_Dialog.iLogout_Dialog;
import ie.mydbs.ca_app.R;
import ie.mydbs.ca_app.Utilities.Utilities;

public class AppMenu implements iAppMenu, iLogout_Dialog {
    //Mobile Apps project for DBS. By Conal O'Shiel (10523829)
    //interface equal to Utilities.logout_dialog to access its method and open dialog
    iLogout_Dialog iLogout_dialog;
    @Override
    public void openLogoutDialogue(FragmentActivity activity) {
        iLogout_Dialog logout = Utilities.logout_dialog;
        logout.openLogoutDialogue(activity);
    }

    //Switch statement to handle menu options
    @Override
    public void handleMenu(MenuItem item, FragmentActivity activity) {
        switch(item.getItemId()){
            case R.id.action_refresh:
                activity.recreate();
                break;
            case R.id.action_logout:
                openLogoutDialogue(activity);
                break;
        }
    }
}
