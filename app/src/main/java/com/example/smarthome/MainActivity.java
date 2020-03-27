package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.smarthome.Fragment.EnergyFragment;
import com.example.smarthome.Fragment.Home.HomeFragment;
import com.example.smarthome.Fragment.Notification.NotifyFragment;
import com.example.smarthome.Fragment.Setting.SettingFragment;
import com.example.smarthome.Utils.Comm;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBnvMain;
    private FragmentManager mFrMngMain;
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

        mBnvMain.setItemIconTintList(null);
        mBnvMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = mBnvMain.getMenu();

                //Reset item bottom navigation view to default
                //menu.findItem(R.id.item_main_home).setIcon(R.drawable.home_none_30px);
                //menu.findItem(R.id.item_main_notify).setIcon(R.drawable.notify_none_30px);
                //menu.findItem(R.id.item_main_energy).setIcon(R.drawable.graph_none_30px);
                //menu.findItem(R.id.item_main_setting).setIcon(R.drawable.setting_none_30px);

                switch (item.getItemId()) {
                    case R.id.item_main_home:
                        //menu.findItem(R.id.item_main_home).setIcon(R.drawable.home_pressed_30px);
                        //Wait until connected to server

                        Bundle bundle = new Bundle();
                        bundle.putString("MAIN_APARTMENT_ID", mApartmentId);
                        HomeFragment fragment = new HomeFragment();
                        fragment.setArguments(bundle);

                        //Send apartment id to home fragment
                        LoadFragment(fragment);
                        break;
                    case R.id.item_main_notify:
                        //menu.findItem(R.id.item_main_notify).setIcon(R.drawable.notify_pressed_30px);
                        //Send apartment id to home fragment
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("MAIN_USER_ID", mUserId);
                        HomeFragment fragment1 = new HomeFragment();
                        fragment1.setArguments(bundle1);

                        LoadFragment(fragment1);
                        break;
                    case R.id.item_main_energy:
                        //menu.findItem(R.id.item_main_energy).setIcon(R.drawable.graph_pressed_30px);
                        LoadFragment(new EnergyFragment());
                        break;
                    case R.id.item_main_setting:
                        //menu.findItem(R.id.item_main_setting).setIcon(R.drawable.setting_pressed_30px);
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

        while (mTcpClient != null) {

        }
        //Add default fragment
        AddFragment(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAccount == null || mPassword == null) {
            GetValue();
        }

        //Connect to server via TCP if not connect
        if (mTcpClient == null || mTcpClient.isConnected() == false) {
            ConnectServer();

            //Wait for connected to server
            while (mTcpClient == null) {

            }

            while (!mTcpClient.isConnected()) {

            }

            ArrayList<String> params = new ArrayList<>();
            params.add(mAccount);
            params.add(mPassword);
            TCPMessageObject loginMess = new TCPMessageObject(Comm.MESSAGE_CODE_LOGIN, params);

            //Send TCP login message
            mTcpClient.SendMessage(loginMess.toString());
            Log.d("SmartHome", "Login message have been sent");
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_connected), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_reconnect), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        ShowDialogFinish(getResources().getString(R.string.label_logout), getResources().getString(R.string.message_question_logout));
    }

    private void LoadFragment(Fragment fragment) {
        FragmentTransaction ft_rep = mFrMngMain.beginTransaction();
        ft_rep.replace(R.id.relaytiveLayout_main, fragment);
        //  t_rep.addToBackStack(null);
        ft_rep.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft_rep.commit();
    }

    private void AddFragment(Fragment fragment) {
        FragmentTransaction ft_add = mFrMngMain.beginTransaction();
        ft_add.add(R.id.relaytiveLayout_main, fragment);
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
            });
            mTcpClient.Connect();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //Process message
            String[] Data = values[0].split(";");
            Log.d("SmartHome", "Message from server: " + values[0]);

            if (Data.length > 0)
                switch (Data[0]) {
                    case Comm.MESSAGE_CODE_LOGIN_RSP:
                        Log.d("SmartHome", "Response login message from server");
                        break;
                    // Receive status update from server
                    case Comm.MESSAGE_CODE_SEND_POINT:
                        try {
                            String[] Data1 = values[0].split("1203");
                            if (Data1.length > 2) {

                                for (int i = 1; i < Data1.length; i++) {
                                    String message = "1203" + Data1[i];
                                    String[] DataNew = message.split(";");

                                    String topic = DataNew[2];
                                    String value = DataNew[3];
                                    Log.d("SmartHome", "POINT - Topic: " + topic);
                                    Log.d("SmartHome", "POINT - Message: " + value);
                                    Global.UpdatePointValueByTopic(topic, value);
                                }
                            } else {
                                String topic = Data[2];
                                String value = Data[3];
                                Log.d("SmartHome", "POINT - Topic: " + topic);
                                Log.d("SmartHome", "POINT - Message: " + value);
                                Global.UpdatePointValueByTopic(topic, value);
                            }
                        } catch (Exception ex) {
                            Log.e("SmartHome", "Error when receiver message from server: " + ex);
                        }
                        break;
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
                mTcpClient.DisConnect();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
