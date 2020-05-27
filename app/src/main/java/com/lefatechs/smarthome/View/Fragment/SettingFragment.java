package com.lefatechs.smarthome.View.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lefatechs.smarthome.Controller.RoomAdapter;
import com.lefatechs.smarthome.Controller.SceneAdapter;
import com.lefatechs.smarthome.Custom.Global;
import com.lefatechs.smarthome.Model.Apartment;
import com.lefatechs.smarthome.Model.TCPMessageObject;
import com.lefatechs.smarthome.R;

import java.util.ArrayList;
import java.util.Locale;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lefatechs.smarthome.Utils.Comm;
import com.lefatechs.smarthome.Utils.ItemOffsetDecoration;
import com.lefatechs.smarthome.View.Activity.LoginActivity;
import com.lefatechs.smarthome.View.Activity.MainActivity;
import com.lefatechs.smarthome.View.Fragment.Home.HomeFragment;

public class SettingFragment extends PreferenceFragmentCompat {
    public final static String PREF_LANGUAGE="language";
    public final static String PREF_BACKGROUND = "background";
    public final static String PREF_ACCOUNT = "accountInfo";
    public final static String PREF_SOFTWARE = "softInfo";
    final static String languagePreference = "LanguagePref";
    private BottomNavigationView mBnvMain;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.matches(PREF_LANGUAGE)){
                    ListPreference languagePref = findPreference(s);
                    String currLanguageCode = languagePref.getValue();
                    CharSequence currLanguageText = languagePref.getEntry();
                    languagePref.setSummary(currLanguageText);
                    ChangeLanguage(currLanguageCode);

                    //Change language weather api
                    Comm.WEATHER_API_LANGUAGE = "&lang=" + currLanguageCode;

                    //Request send current point
                    RequestCurrentPointStatus(Global.sApartment.getId());
                }
                else if (s.matches(PREF_BACKGROUND)){
                    ListPreference wallpaperPref = findPreference(s);
                    CharSequence currBackgroundText = wallpaperPref.getEntry();
                    wallpaperPref.setSummary(currBackgroundText);
                }

            }
        };

        Preference accountInfoPref = findPreference(PREF_ACCOUNT);
        accountInfoPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                displayUserInfoAlertDialog();
                return true;
            }
        });

        Preference softwareInfoPref = findPreference(PREF_SOFTWARE);
        softwareInfoPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                displaySoftwareInfoAlertDialog();
                return true;
            }
        });
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListPreference languagePref = findPreference(PREF_LANGUAGE);
        CharSequence currLanguageText = languagePref.getEntry();
        languagePref.setSummary(currLanguageText);

        ListPreference wallpaperPref = findPreference(PREF_BACKGROUND);
        CharSequence currWallpaperText = wallpaperPref.getEntry();
        wallpaperPref.setSummary(currWallpaperText);

        Preference accountPref = findPreference(PREF_ACCOUNT);
        accountPref.setTitle(getResources().getString(R.string.label_account_info));

        Preference softPref = findPreference(PREF_SOFTWARE);
        softPref.setTitle(getResources().getString(R.string.label_software_info));

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Setting","Resume");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Setting","Pause");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void ChangeLanguage(String lang){
        Locale myLocale = new Locale(lang);
        Activity activity = getActivity();
        if (isAdded() && activity!=null){
            Resources res = activity.getResources();
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

    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Activity activity = getActivity();

        if (isAdded() && activity!=null){
            //region Change bottom menu language
            mBnvMain = activity.findViewById(R.id.bottomNavigationView_main_menu);
            Menu menu = mBnvMain.getMenu();
            menu.findItem(R.id.item_main_home).setTitle(getResources().getString(R.string.label_home));
            menu.findItem(R.id.item_main_notify).setTitle(getResources().getString(R.string.label_notification));
            menu.findItem(R.id.item_main_energy).setTitle(getResources().getString(R.string.label_energy));
            menu.findItem(R.id.item_main_setting).setTitle(getResources().getString(R.string.label_setting));
            //endregion

            //Category pref
            PreferenceCategory prefCat=findPreference("settingCategory");
            prefCat.setTitle(getResources().getString(R.string.label_setting));


            //region Language pref
            //Language
            ListPreference languagePref = findPreference(PREF_LANGUAGE);
            //Title
            languagePref.setTitle(getResources().getString(R.string.label_language));
            languagePref.setEntries(activity.getResources().getStringArray(R.array.arr_language_name));
            CharSequence currLanguageText = languagePref.getEntry();
            //Summary
            languagePref.setSummary(currLanguageText);
            //Dialog title
            languagePref.setDialogTitle(getResources().getString(R.string.label_choose_language));
            //Dialog button
            languagePref.setNegativeButtonText(getResources().getString(R.string.button_cancel));

            SharedPreferences preferences = activity.getSharedPreferences(languagePreference, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.clear().apply();
            editor.putString("languageCode", languagePref.getValue());
            editor.commit();

            //Reload apartment data with language
            final ProgressDialog dialogProgress = new ProgressDialog(getActivity());
            dialogProgress.setMessage(activity.getResources().getString(R.string.message_load_data));
            dialogProgress.setCancelable(false);
            dialogProgress.show();

            String Url = Comm.API_ADDRESS + "Apartment/" + Global.sApartment.getId()+"?language="+languagePref.getValue();
            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String jsonData = response;
                    try {
                        Global.sApartment = new Gson().fromJson(jsonData, Apartment.class);

                        dialogProgress.dismiss();
                        Log.d("SmartHome", "Load apartment data success");
                    } catch (Exception ex) {
                        Log.e("SmartHome", "Convert apartment data from json to object error");
                        dialogProgress.dismiss();

                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("SmartHome", "Can't get apartment info");
                    dialogProgress.dismiss();

                }
            });

            Volley.newRequestQueue(activity.getBaseContext()).add(stringRequest);

            //endregion

            //region Wallpaper pref
            ListPreference wallpaperPref = findPreference(PREF_BACKGROUND);
            //Title
            wallpaperPref.setTitle(getResources().getString(R.string.label_wallpaper));
            wallpaperPref.setEntries(getResources().getStringArray(R.array.arr_background_name));
            CharSequence currWallpaperText = wallpaperPref.getEntry();
            //Summary
            wallpaperPref.setSummary(currWallpaperText);
            //Dialog title
            wallpaperPref.setDialogTitle(getResources().getString(R.string.label_choose_wallpaper));
            //Dialog button
            wallpaperPref.setNegativeButtonText(getResources().getString(R.string.button_cancel));
            //endregion

            //region Account info
            Preference accountPref = findPreference(PREF_ACCOUNT);
            accountPref.setTitle(getResources().getString(R.string.label_account_info));
            //endregion

            //region Software info
            Preference softPref = findPreference(PREF_SOFTWARE);
            softPref.setTitle(getResources().getString(R.string.label_software_info));
            //endregion
        }

    }

    public void displayUserInfoAlertDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_user_information, null);

        TextView tvUserFullName = alertLayout.findViewById(R.id.textView_userInformation_userFullName);
        TextView tvUserId = alertLayout.findViewById(R.id.textView_userInformation_id);
        TextView tvApartment = alertLayout.findViewById(R.id.textView_userInformation_apartment);
        TextView tvUserPhone = alertLayout.findViewById(R.id.textView_userInformation_phone);
        TextView tvUserAddress= alertLayout.findViewById(R.id.textView_userInformation_address);
        TextView tvDescription= alertLayout.findViewById(R.id.textView_userInformation_description);

        String id = "<font color=#A9A9A9>"+getResources().getString(R.string.label_id)+":</font> <font color=#000000>"+ Global.sUser.getId() +"</font>";
        String fullName = "<font color=#000000>"+ Global.sUser.getName() +"</font>";
        String apartment = "<font color=#A9A9A9>"+getResources().getString(R.string.label_apartment)+":</font> <font color=#000000>"+ Global.sApartment.getId() +"</font>";
        String userPhone = "<font color=#A9A9A9>"+getResources().getString(R.string.label_phone)+":</font> <font color=#000000>"+ Global.sUser.getPhoneNo() +"</font>";
        String userAddress = "<font color=#A9A9A9>"+getResources().getString(R.string.label_address)+":</font> <font color=#000000>"+ Global.sUser.getAddress() +"</font>";
        String description = "<font color=#A9A9A9>"+getResources().getString(R.string.label_description)+":</font> <font color=#000000>"+ Global.sUser.getDescription() +"</font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvUserId.setText(Html.fromHtml(id,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            tvUserFullName.setText(Html.fromHtml(fullName,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            tvApartment.setText(Html.fromHtml(apartment,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            tvUserPhone.setText(Html.fromHtml(userPhone,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            tvUserAddress.setText(Html.fromHtml(userAddress,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            tvDescription.setText(Html.fromHtml(description,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            tvUserId.setText(Html.fromHtml(id), TextView.BufferType.SPANNABLE);
            tvUserFullName.setText(Html.fromHtml(fullName), TextView.BufferType.SPANNABLE);
            tvApartment.setText(Html.fromHtml(apartment), TextView.BufferType.SPANNABLE);
            tvUserPhone.setText(Html.fromHtml(userPhone), TextView.BufferType.SPANNABLE);
            tvUserAddress.setText(Html.fromHtml(userAddress), TextView.BufferType.SPANNABLE);
            tvDescription.setText(Html.fromHtml(description), TextView.BufferType.SPANNABLE);
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        //alert.setTitle(getString(R.string.label_account_info));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        alert.setPositiveButton(getString(R.string.button_yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
    public void displaySoftwareInfoAlertDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_software_information, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("");
        alert.setView(alertLayout);
        alert.setCancelable(false);

        alert.setPositiveButton(getString(R.string.button_yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    //Send message to server via TCP
    private void RequestCurrentPointStatus(String apartmentId) {
        try {
            ArrayList<String> messParams = new ArrayList<>();
            messParams.add(apartmentId);

            TCPMessageObject message = new TCPMessageObject(Comm.MESSAGE_CODE_CURRENT_POINT_STATUS, messParams);
            MainActivity.mTcpClient.SendMessage(message.toString());

            Log.d("CODE", message.toString());
        } catch (Exception ex) {
            Log.e("SmartHome", "Send value message to sever error: " + ex.getMessage());
        }
    }

}
