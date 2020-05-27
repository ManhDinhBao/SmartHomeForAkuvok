package com.lefatechs.smarthome.View.Activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lefatechs.smarthome.Custom.Global;
import com.lefatechs.smarthome.Model.User.User;
import com.lefatechs.smarthome.R;
import com.lefatechs.smarthome.Utils.Comm;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button mBtLogin;
    private CheckBox mCbRemember, mCbShowPassword;
    private EditText mEtUserName, mEtPassWord, mEtServerIP;
    final static String userPreference = "UserPref", languagePreference = "LanguagePref";
    private String mApartmentId, mAccount, mPassword, mUserId, mServerIP;
    private TextView mTvUserNameLabel, mTvPassWordLabel, mTvServerIP;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitControl();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences preferences = getSharedPreferences(userPreference, MODE_PRIVATE);
        if (preferences != null) {
            mCbRemember.setChecked(preferences.getBoolean("remember", false));
            mEtUserName.setText(preferences.getString("userName", ""));
            mEtPassWord.setText(preferences.getString("passWord", ""));
            mEtServerIP.setText(preferences.getString("serverIP", ""));
        }

        //Set button effect when user click button
        mBtLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int amberColor = getResources().getColor(R.color.amber);
                int whiteColor = getResources().getColor(R.color.white);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBtLogin.setTextColor(amberColor);
                        break;
                    case MotionEvent.ACTION_UP:
                        mBtLogin.setTextColor(whiteColor);
                        break;
                }
                return false;
            }
        });

        mCbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mEtPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Show or hide password base on check box
        //Event when user click login button
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckValidData()) {

                    mAccount = mEtUserName.getText().toString();
                    mPassword = mEtPassWord.getText().toString();
                    mServerIP = mEtServerIP.getText().toString();

                    SharedPreferences preferences = getSharedPreferences(userPreference, MODE_PRIVATE);

                    //Check remember account or not
                    if (mCbRemember.isChecked()) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear().apply();
                        editor.putBoolean("remember", true);
                        editor.putString("userName", mAccount);
                        editor.putString("passWord", mPassword);
                        editor.putString("serverIP", mEtServerIP.getText().toString());
                        editor.commit();
                    } else {
                        preferences.edit().clear().apply();
                    }

                    //Set Server IP and API
                    Comm.SERVER_IP = mEtServerIP.getText().toString();
                    Comm.API_ADDRESS = "http://" + mEtServerIP.getText().toString() + ":8093/api/";

                    //If network available -> Check valid account
                    if (Global.isNetworkAvailable(LoginActivity.this)) {
                        if (isPortOpen(mEtServerIP.getText().toString(),16707,2000)){
                            CheckAccount(mAccount, mPassword);
                        }
                        else
                        {
                            Global.ShowDialog(getResources().getString(R.string.dialog_title_error), getResources().getString(R.string.message_server_off), LoginActivity.this);
                        }


                    } else {
                        Global.ShowDialog(getResources().getString(R.string.dialog_title_error), getResources().getString(R.string.message_error_no_internet), LoginActivity.this);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(languagePreference, MODE_PRIVATE);
        if(preferences!=null){
            ChangeLanguage(preferences.getString("languageCode", "en"));
        }

        Comm.WEATHER_API_LANGUAGE = "&lang="+preferences.getString("languageCode", "en");

        mTvUserNameLabel.setText(R.string.label_user_name);
        mTvPassWordLabel.setText(R.string.label_password);
        mTvServerIP.setText(R.string.label_server_address);

        mCbRemember.setText(R.string.label_remember_account);
        mCbShowPassword.setText(R.string.label_show_password);

        mEtUserName.setHint(R.string.hint_user_name);
        mEtPassWord.setHint(R.string.hint_password);
        mEtServerIP.setHint(R.string.hint_server_address);

        mBtLogin.setText(R.string.label_login);

    }

    //Check null text and wrong format
    private boolean CheckValidData() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                getResources().getString(R.string.button_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        if (mEtUserName.getText().length() <= 0) {
            builder1.setMessage(getResources().getString(R.string.message_null_user_name));
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return false;
        }

        if (mEtPassWord.getText().length() <= 0) {
            builder1.setMessage(getResources().getString(R.string.message_null_password));
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return false;
        }

        if (mEtServerIP.getText().length() <= 0) {
            builder1.setMessage(getResources().getString(R.string.message_null_server_ip));
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return false;
        } else {
            if (ValidateIP(mEtServerIP.getText().toString()) == false) {
                builder1.setMessage(getResources().getString(R.string.message_wrong_server_ip_format));
                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }
        }
        return true;
    }

    //Check valid ip address format
    private boolean ValidateIP(String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ip.matches(PATTERN);
    }

    //Define control
    private void InitControl() {
        mBtLogin = findViewById(R.id.button_login);
        mCbRemember = findViewById(R.id.checkBox_login_remember);
        mCbShowPassword = findViewById(R.id.checkBox_login_password);
        mEtUserName = findViewById(R.id.editText_login_username);
        mEtPassWord = findViewById(R.id.editText_login_password);
        mEtServerIP = findViewById(R.id.editText_login_server);
        mTvUserNameLabel = findViewById(R.id.textView_loginActivity_userName);
        mTvPassWordLabel = findViewById(R.id.textView_loginActivity_passWord);
        mTvServerIP = findViewById(R.id.textView_loginActivity_server);

    }

    //Check valid account
    private void CheckAccount(String account, String passWord) {
        String languageCode = Locale.getDefault().getLanguage();

        final ProgressDialog LoginDialog = new ProgressDialog(LoginActivity.this);
        String Url = Comm.API_ADDRESS + "User?language=" + languageCode;
        final String user = account, pass = passWord;
        Map<String, String> params = new HashMap<String, String>();
        params.put("ID", "123");
        params.put("UserName", user);
        params.put("Password", pass);
        JSONObject parameters = new JSONObject(params);

        LoginDialog.setMessage(getResources().getString(R.string.message_logining));
        LoginDialog.setCancelable(false);
        LoginDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (LoginDialog.isShowing()) {
                    LoginDialog.dismiss();
                }
                try {
                    String userId = response.getString("ID");
                    Log.d("SmartHome", "Login User ID: " + userId);
                    //Valid account
                    if (!userId.matches("null")) {
                        mApartmentId = response.getString("ApartmentID");
                        Log.d("SmartHome", "Apartment ID: " + mApartmentId);

                        mUserId = userId;

                        //Save user information
                        User user = new User();
                        user.setId(response.getString("ID"));
                        user.setAddress(response.getString("Address"));
                        user.setDescription(response.getString("Description"));
                        user.setName(response.getString("Name"));
                        user.setPhoneNo(response.getString("PhoneNo"));

                        Global.sUser = user;

                        //Account doesn't manage any apartment
                        if (mApartmentId.matches("null")) {
                            Global.ShowDialog(getResources().getString(R.string.dialog_title_error), getResources().getString(R.string.message_error_no_apartment), LoginActivity.this);
                        }
                        //Go to main screen with account
                        else {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("LOGIN_APARTMENT_ID", mApartmentId);
                            intent.putExtra("LOGIN_USER_ACCOUNT", mAccount);
                            intent.putExtra("LOGIN_USER_PASSWORD", mPassword);
                            intent.putExtra("LOGIN_USER_ID", mUserId);
                            startActivity(intent, options.toBundle());
                        }
                    } else {
                        Global.ShowDialog(getResources().getString(R.string.dialog_title_error), getResources().getString(R.string.message_error_wrong_account), LoginActivity.this);
                    }
                } catch (JSONException ex) {
                    Global.ShowDialog(getResources().getString(R.string.dialog_title_error), getResources().getString(R.string.message_error_data), LoginActivity.this);
                    Log.e("SmartHome", "Data error: " + ex.getMessage());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (LoginDialog.isShowing()) {
                    LoginDialog.dismiss();
                }
                Global.ShowDialog(getResources().getString(R.string.dialog_title_error), getResources().getString(R.string.message_service_error), LoginActivity.this);
                Log.e("SmartHome", error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void ChangeLanguage(String lang){
        Locale myLocale = new Locale(lang);
        Resources res = getBaseContext().getResources();
        Configuration conf = res.getConfiguration();

        DisplayMetrics dm = res.getDisplayMetrics();
        Locale.setDefault(myLocale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(myLocale);
        } else {
            conf.locale = myLocale;
        }

        res.updateConfiguration(conf,dm);
        onConfigurationChanged(conf);
    }

    public static boolean isPortOpen(final String ip, final int port, final int timeout) {

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        }

        catch(ConnectException ce){
            return false;
        }

        catch (Exception ex) {
            return false;
        }
    }
}
