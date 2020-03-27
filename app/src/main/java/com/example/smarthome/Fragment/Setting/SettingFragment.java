package com.example.smarthome.Fragment.Setting;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import com.example.smarthome.R;
import java.util.Locale;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingFragment extends PreferenceFragmentCompat {
    public final static String PREF_LANGUAGE="language";
    public final static String PREF_BACKGROUND = "background";
    public final static String PREF_ACCOUNT = "accountInfo";
    public final static String PREF_SOFTWARE = "softInfo";
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
                    //Locale locale=new Locale(currLanguageCode);
                    //LocaleHelper.setLocale(getActivity().getBaseContext(), currLanguageCode);
                    CLanguage(currLanguageCode);
                }
                if (s.matches(PREF_BACKGROUND)){
                    ListPreference wallpaperPref = findPreference(s);
                    CharSequence currBackgroundText = wallpaperPref.getEntry();
                    wallpaperPref.setSummary(currBackgroundText);
                }
            }
        };

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

    private void CLanguage(String lang){
        Locale myLocale = new Locale(lang);
        Resources res = getActivity().getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        //Configuration conf = res.getConfiguration();
        Configuration conf = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(myLocale);
        } else {
            conf.locale = myLocale;
        }
        getActivity().getBaseContext().getResources().updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //region Change bottom menu language
        mBnvMain = getActivity().findViewById(R.id.bottomNavigationView_main_menu);
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
        languagePref.setEntries(getResources().getStringArray(R.array.arr_language_name));
        CharSequence currLanguageText = languagePref.getEntry();
        //Summary
        languagePref.setSummary(currLanguageText);
        //Dialog title
        languagePref.setDialogTitle(getResources().getString(R.string.label_choose_language));
        //Dialog button
        languagePref.setNegativeButtonText(getResources().getString(R.string.button_cancel));
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
