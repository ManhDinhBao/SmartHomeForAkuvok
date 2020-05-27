package com.lefatechs.smarthome.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.lefatechs.smarthome.Custom.Broadcast;
import com.lefatechs.smarthome.View.Fragment.Engergy.EnergyFragment;
import com.lefatechs.smarthome.View.Fragment.Home.HomeFragment;
import com.lefatechs.smarthome.View.Fragment.Notification.NotificationFragment;
import com.lefatechs.smarthome.View.Fragment.SettingFragment;
import com.lefatechs.smarthome.Custom.Global;
import com.lefatechs.smarthome.R;
import com.lefatechs.smarthome.Model.TCPClient;
import com.lefatechs.smarthome.Model.TCPMessageObject;
import com.lefatechs.smarthome.Utils.Comm;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBnvMain;
    private FragmentManager mFrMngMain;
    private BroadcastReceiver broadcast;
    private String mApartmentId, mAccount, mPassword, mUserId;
    public static TCPClient mTcpClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitControl();
        if (mAccount == null || mPassword == null) {
            GetValue();
        }

        mBnvMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = mBnvMain.getMenu();

                switch (item.getItemId()) {
                    case R.id.item_main_home:
                        Bundle bundle = new Bundle();
                        bundle.putString("MAIN_APARTMENT_ID", mApartmentId);
                        HomeFragment fragment = new HomeFragment();
                        fragment.setArguments(bundle);

                        //Send apartment id to home fragment
                        LoadFragment(fragment);
                        break;
                    case R.id.item_main_notify:
                        LoadFragment(new NotificationFragment());
                        break;
                    case R.id.item_main_energy:
                        LoadFragment(new EnergyFragment());
                        break;
                    case R.id.item_main_setting:
                        LoadFragment(new SettingFragment());
                        break;
                }
                return true;
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("MAIN_APARTMENT_ID", mApartmentId);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);

        //Add default fragment
        AddFragment(fragment);

//        broadcast = new Broadcast();
//        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
//        IntentFilter filter1 = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
//        registerReceiver(broadcast, filter);
//        registerReceiver(broadcast, filter1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBnvMain = findViewById(R.id.bottomNavigationView_main_menu);
        Menu menu = mBnvMain.getMenu();

        menu.findItem(R.id.item_main_home).setTitle(getResources().getString(R.string.label_home));
        menu.findItem(R.id.item_main_notify).setTitle(getResources().getString(R.string.label_notification));
        menu.findItem(R.id.item_main_energy).setTitle(getResources().getString(R.string.label_energy));
        menu.findItem(R.id.item_main_setting).setTitle(getResources().getString(R.string.label_setting));

        if (mAccount == null || mPassword == null) {
            GetValue();
        }

        ArrayList<String> params = new ArrayList<>();
        params.add(mAccount);
        params.add(mPassword);
        TCPMessageObject loginMess = new TCPMessageObject(Comm.MESSAGE_CODE_LOGIN, params);

        try {
            //Connect to server via TCP if not connect
            if (mTcpClient == null || mTcpClient.isConnected() == false) {
                ConnectServer();

                ProgressDialog LoginDialog = new ProgressDialog(MainActivity.this);
                LoginDialog.setMessage(getResources().getString(R.string.message_connecting_server));
                LoginDialog.setCancelable(false);
                LoginDialog.show();
                //Wait for connected to server
                while (mTcpClient == null) {

                }

                while (!mTcpClient.isConnected()) {

                }
                LoginDialog.dismiss();
                //Send TCP login message
                mTcpClient.SendMessage(loginMess.toString());
                Log.d("SmartHome", "Login message have been sent: " + loginMess.toString());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_connected), Toast.LENGTH_SHORT).show();
            } else {
                //Send TCP login message
                mTcpClient.SendMessage(loginMess.toString());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_reconnect), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.message_error_server));
            builder.setMessage(getResources().getString(R.string.message_server_off));
            builder.setCancelable(false);
            builder.setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            if (mTcpClient != null && mTcpClient.isConnected()) {
                mTcpClient.DisConnect();
            }

            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcast);
    }

    @Override
    public void onBackPressed() {
        ShowDialogFinish(getResources().getString(R.string.label_logout), getResources().getString(R.string.message_question_logout));
    }

    private void LoadFragment(Fragment fragment) {
        FragmentTransaction ft_rep = mFrMngMain.beginTransaction();
        ft_rep.replace(R.id.relativeLayout_main, fragment);
        //  t_rep.addToBackStack(null);
        ft_rep.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft_rep.commit();
    }

    private void AddFragment(Fragment fragment) {
        FragmentTransaction ft_add = mFrMngMain.beginTransaction();
        ft_add.add(R.id.relativeLayout_main, fragment);
        //ft_rep.addToBackStack(null);
        ft_add.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft_add.commit();

    }

    private void InitControl() {
        mBnvMain = findViewById(R.id.bottomNavigationView_main_menu);
        mFrMngMain = getSupportFragmentManager();
    }

    private void GetValue() {
        //Get Apartment ID from login activity
        mApartmentId = getIntent().getStringExtra("LOGIN_APARTMENT_ID");
        mAccount = getIntent().getStringExtra("LOGIN_USER_ACCOUNT");
        mPassword = getIntent().getStringExtra("LOGIN_USER_PASSWORD");
        mUserId = getIntent().getStringExtra("LOGIN_USER_ID");
    }

    private boolean ConnectServer() {
        //Connect server
        try {
            new connectTask().execute("");
            return true;
        } catch (Exception ex) {
            Global.ShowDialog(getResources().getString(R.string.dialog_title_error), getResources().getString(R.string.message_error_server), MainActivity.this);
            return false;
        }
    }

    public class connectTask extends AsyncTask<String, String, TCPClient> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected TCPClient doInBackground(String... message) {
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            }, new TCPClient.OnServerClosed() {
                @Override
                public void serverClosed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle(getResources().getString(R.string.message_error_server));
                            builder.setMessage(getResources().getString(R.string.label_server_disconnect));
                            builder.setCancelable(false);
                            builder.setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            if (mTcpClient != null && mTcpClient.isConnected()) {
                                mTcpClient.DisConnect();
                            }

                        }
                    });

                }
            }, new TCPClient.OnConnectionTimeOut() {
                @Override
                public void timeOut() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("");
                            builder.setMessage("Time out");
                            builder.setCancelable(false);
                            builder.setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            if (mTcpClient != null && mTcpClient.isConnected()) {
                                mTcpClient.DisConnect();
                            }

                        }
                    });
                }
            });

            mTcpClient.Connect();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //Process message
            String[] Data = values[0].split("<>");
            Log.d("SmartHome", "Message from server: " + values[0]);

            if (Data.length > 0) {
                for (int i = 0; i < Data.length; i++) {
                    String message = Data[i];
                    Log.d("TCP", "Message: " + message);

                    String[] Data1 = Data[i].split(";");

                    switch (Data1[0]) {
                        case Comm.MESSAGE_CODE_LOGIN_RSP:
                            Log.d("SmartHome", "Response login message from server");
                            break;
                        // Receive status update from server
                        case Comm.MESSAGE_CODE_SEND_POINT:
                            try {
                                String topic = Data1[2];
                                String value = Data1[3];
                                Log.d("SmartHome", "POINT - Topic: " + topic);
                                Log.d("SmartHome", "POINT - Message: " + value);
                                Global.UpdatePointValueByTopic(topic, value);

                            } catch (Exception ex) {
                                Log.e("SmartHome", "Error when receiver message from server: " + ex);
                                Log.e("SmartHome", ": " + values[0]);
                            }
                            break;

                    }
                }
            }

        }
    }


    public void ShowDialogFinish(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mTcpClient!=null && mTcpClient.isConnected())
                mTcpClient.DisConnect();
//                unregisterReceiver(broadcast);
//                Global.sApartment = null;
//
//                Intent mStartActivity = new Intent(MainActivity.this, LoginActivity.class);
//                int mPendingIntentId = 123456;
//                PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId,mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//                AlarmManager mgr = (AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);
//                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//                System.exit(0);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
