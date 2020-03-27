package com.example.smarthome;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smarthome.Utils.Comm;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button mBtLogin;
    private CheckBox mCbRemember, mCbShowPassword;
    private EditText mEtUserName, mEtPassWord, mEtServerIP;
    final static String filename = "account.xml";
    private String mApartmentId,mAccount, mPassword, mUserId;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitControl();

        //Load account.xml file
        //If system have file account -> Set checkbox remember account checked
        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(storageDir,filename);
            if(file.exists()){
                GetFileSetting();
                mCbRemember.setChecked(true);
            }
        }
        catch (Exception ex){
            Log.d("SmartHome","Get account file error: " + ex.toString());
        }

        //Set button effect when user click button
        mBtLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int amberColor = getResources().getColor(R.color.amber);
                int whiteColor = getResources().getColor(R.color.white);
                switch (motionEvent.getAction()){
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
                if (b){
                    mEtPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    mEtPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Delete file account if check box unchecked
        mCbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==false){
                    try {
                        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

                        File file = new File(storageDir,filename);
                        if(file.exists()){
                           file.delete();
                        }
                    }
                    catch (Exception ex){

                    }
                }
            }
        });

        //Show or hide password base on check box
        //Event when user click login button
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckValidData()) {
                    //Check remember account or not
                    if (mCbRemember.isChecked()){
                        SaveFileSetting(mEtUserName.getText().toString(),mEtPassWord.getText().toString(),mEtServerIP.getText().toString());
                    }

                    mAccount=mEtUserName.getText().toString();
                    mPassword=mEtPassWord.getText().toString();

                    //If network available -> Check valid account
                    if (Global.isNetworkAvailable(LoginActivity.this)) {
                        CheckAccount(mAccount, mPassword);
                    } else {
                        Global.ShowDialog(getResources().getString(R.string.dialog_title_error), getResources().getString(R.string.message_error_no_internet), LoginActivity.this);
                    }
                }
            }
        });

    }
    //Get username, password, server IP address from file
    private void GetFileSetting(){
        ArrayList<String> setting = new ArrayList<>();
        String data = null;
        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File file=new File(storageDir,filename);

            //FileInputStream fis = getActivity().getApplicationContext().openFileInput(xmlFile);

            FileInputStream fis = new  FileInputStream(file);

            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            data = new String(inputBuffer);
            isr.close();
            fis.close();
        }
        catch (FileNotFoundException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        }
        catch (XmlPullParserException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        }
        catch (XmlPullParserException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        try {
            xpp.setInput(new StringReader(data));
        }
        catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        }
        catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        while (eventType != XmlPullParser.END_DOCUMENT){
            if (eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            }
            else if (eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag "+xpp.getName());
            }
            else if (eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag "+xpp.getName());
            }
            else if(eventType == XmlPullParser.TEXT) {
                setting.add(xpp.getText());
            }
            try {
                eventType = xpp.next();
            }
            catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String username = setting.get(0);
        mEtUserName.setText(username);
        String password = setting.get(1);
        mEtPassWord.setText(password);
        String serverIp = setting.get(2);
        mEtServerIP.setText(serverIp);
    }

    //Save username, password, server IP address to file
    private boolean SaveFileSetting(String userName, String passWord, String serverIP){
        boolean result=true;
        final String xmlFile = "account";
        try {

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File file=new File(storageDir,filename);

            FileOutputStream fileos = new  FileOutputStream(file);
            //FileOutputStream fileos= getActivity().getApplicationContext().openFileOutput(xmlFile, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "setting");
            xmlSerializer.startTag(null, "username");
            xmlSerializer.text(userName);
            xmlSerializer.endTag(null, "username");
            xmlSerializer.startTag(null,"password");
            xmlSerializer.text(passWord);
            xmlSerializer.endTag(null, "password");
            xmlSerializer.startTag(null,"serverIP");
            xmlSerializer.text(serverIP);
            xmlSerializer.endTag(null, "serverIP");
            xmlSerializer.endTag(null, "setting");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result=false;
        }
        return  result;

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
            if (ValidateIP(mEtServerIP.getText().toString())==false) {
                builder1.setMessage(getResources().getString(R.string.message_wrong_server_ip_format));
                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }
        }
        return true;
    }

    //Check valid ip address format
    private boolean ValidateIP(String ip){
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ip.matches(PATTERN);
    }

    //Define control
    private void InitControl(){
        mBtLogin = findViewById(R.id.button_login);
        mCbRemember = findViewById(R.id.checkBox_login_remember);
        mCbShowPassword = findViewById(R.id.checkBox_login_password);
        mEtUserName = findViewById(R.id.editText_login_username);
        mEtPassWord = findViewById(R.id.editText_login_password);
        mEtServerIP = findViewById(R.id.editText_login_server);
    }

    //Check valid account
    private void CheckAccount(String account, String passWord){
        final ProgressDialog LoginDialog = new ProgressDialog(LoginActivity.this);
        String Url = Comm.API_ADDRESS + "User?language=en";
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
                    Log.d("SmartHome", "Login User ID: "+ userId);
                    //Valid account
                    if (!userId.matches("")) {
                        mApartmentId = response.getString("ApartmentID");
                        Log.d("SmartHome", "Apartment ID: " + mApartmentId);

                        mUserId = userId;

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
                Log.e("SmartHome",error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
