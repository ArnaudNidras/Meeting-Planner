package fr.arnaud_piriou.meetingplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by arnau on 19/12/2016.
 */



public class DownloadReceiver extends BroadcastReceiver {


    @Override

    public void onReceive(Context context, Intent intent) {

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Toast.makeText(context, context.getString(R.string.toast_download), Toast.LENGTH_LONG).show();
        v.vibrate(1000);

    }


}
