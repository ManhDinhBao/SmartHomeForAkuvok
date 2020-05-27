package com.lefatechs.smarthome.Custom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.lefatechs.smarthome.R;
import com.lefatechs.smarthome.Utils.NetworkUtil;
import com.lefatechs.smarthome.View.Activity.LoginActivity;
import com.lefatechs.smarthome.View.Activity.MainActivity;

public class Broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Boolean status = NetworkUtil.getConnectivityStatusString(context);
        if (!status){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.message_error_server));
            builder.setMessage(context.getResources().getString(R.string.label_server_disconnect));
            builder.setCancelable(false);
            builder.setPositiveButton(context.getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (MainActivity.mTcpClient!=null && MainActivity.mTcpClient.isConnected()){
                        MainActivity.mTcpClient.DisConnect();

                    }
                    Intent mStartActivity = new Intent(context, LoginActivity.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                    System.exit(0);

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

}
