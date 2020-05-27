package com.lefatechs.smarthome.View.Fragment.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lefatechs.smarthome.Controller.RoomAdapter;
import com.lefatechs.smarthome.Controller.SceneAdapter;
import com.lefatechs.smarthome.Custom.Global;
import com.lefatechs.smarthome.View.Activity.LoginActivity;
import com.lefatechs.smarthome.View.Activity.MainActivity;
import com.lefatechs.smarthome.Model.Apartment;
import com.lefatechs.smarthome.Model.Device.Device;
import com.lefatechs.smarthome.R;
import com.lefatechs.smarthome.Model.TCPMessageObject;
import com.lefatechs.smarthome.Utils.Comm;
import com.lefatechs.smarthome.Utils.ItemOffsetDecoration;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment implements SceneAdapter.OnItemClickListener {
    private RoomAdapter mRoomAdapter;
    public static ViewPager sVpRooms;
    private TabLayout mTlRoom;
    private ImageView mImageView;
    private RecyclerView mRvScene;
    private ArrayList<Device> mScenes;
    private SceneAdapter mSceneAdapter;
    private String mApartmentId;
    private ProgressDialog dialogProgress;
    private TextView mTvDate, mTvWeatherInfo, mTvTemp, mTvUser;
    public final static String PREF_BACKGROUND = "background";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        InitControl(view);

        GetValue();
        GetApartmentData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Set background
        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String backgroundValue = sharedConfig.getString(PREF_BACKGROUND, "");
        switch (backgroundValue) {
            case "2":
                Picasso.get().load(R.drawable.home_background2).fit().into(mImageView);
                break;
            case "3":
                Picasso.get().load(R.drawable.home_background3).fit().into(mImageView);
                break;
            case "4":
                Picasso.get().load(R.drawable.home_background4).fit().into(mImageView);
                break;
            default:
                Picasso.get().load(R.drawable.home_background).fit().into(mImageView);
                break;
        }

        //Get weather information
        GetWeather();

        //Set date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMMM dd, yyyy");
        String currentDate = format.format(calendar.getTime());
        mTvDate.setText(currentDate);

        //Set hi user
        String userName = getString(R.string.label_hi);
        mTvUser.setText(userName+ " " + Global.sUser.getName());
    }

    //When user click scene
    @Override
    public void onItemClick(int position) {
        try {
            //Send scene point value to server
            Device device = mScenes.get(position);
            String powerTopic = device.GetPowerPublicTopic();
            ArrayList<String> messParams = new ArrayList<>();
            messParams.add(powerTopic);
            if (powerTopic != null) {
                //device.setPower(true);
                messParams.add("1");
                TCPMessageObject message = new TCPMessageObject(Comm.MESSAGE_CODE_SEND_POINT, messParams);
                MainActivity.mTcpClient.SendMessage(message.toString());
                Log.d("SmartHome","Send scene value: "+message.toString());
            }
        } catch (Exception ex) {
            Log.e("SmartHome", "Send scene to sever error: " + ex.getMessage());
        }
    }

    //Define controls
    private void InitControl(View view) {
        sVpRooms = view.findViewById(R.id.viewpager_fragmentHome_Rooms);
        mTlRoom = view.findViewById(R.id.tabLayout_fragmentHome_Rooms);
        mImageView = view.findViewById(R.id.imageView_FragmentHome_Background);
        mRvScene = view.findViewById(R.id.recycleView_fragmentHome_Scenes);
        mTvDate = view.findViewById(R.id.textView_fragmentHome_Date);
        mTvTemp = view.findViewById(R.id.textView_fragmentHome_Temp);
        mTvWeatherInfo = view.findViewById(R.id.textView_fragmentHome_Weather);
        mTvUser = view.findViewById(R.id.textView_fragmentHome_User);
    }

    //Get value from main activity
    private void GetValue() {
        mApartmentId = getArguments().getString("MAIN_APARTMENT_ID");
    }

    //Get apartment data from server
    private void GetApartmentData() {
        if (mApartmentId != null && !mApartmentId.matches("")) {
            //Show waiting dialog
            dialogProgress = new ProgressDialog(getActivity());
            dialogProgress.setMessage(getActivity().getResources().getString(R.string.message_load_data));
            dialogProgress.setCancelable(false);
            dialogProgress.show();

            SharedPreferences preferences = getActivity().getSharedPreferences("LanguagePref", Context.MODE_PRIVATE);
            String languageCode = preferences.getString("languageCode", "en");

            String Url = Comm.API_ADDRESS + "Apartment/" + mApartmentId+"?language="+languageCode;
            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String jsonData = response;
                    try {
                        Global.sApartment = new Gson().fromJson(jsonData, Apartment.class);
                        if (Global.sApartment!=null){
                            Global.ReSetupDevice();
                        }
                        dialogProgress.dismiss();
                        Log.d("SmartHome", "Load apartment data success");
                    } catch (Exception ex) {
                        Log.e("SmartHome", "Convert apartment data from json to object error");
                        dialogProgress.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getBaseContext());
                        builder.setTitle(getActivity().getResources().getString(R.string.dialog_title_error));
                        builder.setMessage(getActivity().getResources().getString(R.string.message_error_convert_data));
                        builder.setCancelable(false);
                        builder.setNegativeButton(getActivity().getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Disconnect with TCP server
                                MainActivity.mTcpClient.DisConnect();
                                MainActivity.mTcpClient = null;

                                //Go to login screen
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }


                    //Set up room list
                    mRoomAdapter = new RoomAdapter(getChildFragmentManager(), Global.sApartment.getRooms());
                    sVpRooms.setAdapter(mRoomAdapter);
                    mTlRoom.setupWithViewPager(sVpRooms, true);

                    //Set up scenes
                    if(Global.sApartment.getScenes()!=null){
                        mScenes = Global.sApartment.getScenes();
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(RecyclerView.HORIZONTAL);

                    mSceneAdapter = new SceneAdapter(getActivity(), mScenes);
                    mSceneAdapter.setOnItemClickListener(HomeFragment.this);

                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.scene_space);
                    mRvScene.setHasFixedSize(true);
                    mRvScene.setLayoutManager(layoutManager);
                    mRvScene.setAdapter(mSceneAdapter);
                    mRvScene.addItemDecoration(itemDecoration);

                    //Get current point status from sever
                    RequestCurrentPointStatus(Global.sApartment.getId());

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("SmartHome", "Can't get apartment info");
                    dialogProgress.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getBaseContext());
                    builder.setTitle(getActivity().getResources().getString(R.string.dialog_title_error));
                    builder.setMessage(getActivity().getResources().getString(R.string.message_service_error));
                    builder.setCancelable(false);
                    builder.setNegativeButton(getActivity().getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Disconnect with TCP server
                            MainActivity.mTcpClient.DisConnect();
                            MainActivity.mTcpClient = null;

                            //Go to login screen
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            Volley.newRequestQueue(getActivity().getBaseContext()).add(stringRequest);

        }
    }

    //Get weather value
    private void GetWeather(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = Comm.WEATHER_API + Comm.WEATHER_API_CITY_HANOI + Comm.WEATHER_API_CELIUS + Comm.WEATHER_API_LANGUAGE + Comm.WEATHER_API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String day = jsonObject.getString("dt");

                    //Location
                    String name = jsonObject.getString("name");

                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("description");
                    String icon = jsonObjectWeather.getString("icon");

                    mTvWeatherInfo.setText(Global.UpperCaseAllFirst(status));

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String tempC = jsonObjectMain.getString("temp");
                    String humidity = jsonObjectMain.getString("humidity");

                    Double nhietdoC = Double.valueOf(tempC);
                    String nTempC = String.valueOf(nhietdoC.intValue());
                    mTvTemp.setText(nTempC + "°C");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
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
