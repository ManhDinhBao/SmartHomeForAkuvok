package com.example.smarthome.Fragment.Home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smarthome.Global;
import com.example.smarthome.LoginActivity;
import com.example.smarthome.MainActivity;
import com.example.smarthome.Model.Apartment;
import com.example.smarthome.Model.Device.Device;
import com.example.smarthome.R;
import com.example.smarthome.TCPMessageObject;
import com.example.smarthome.Utils.Comm;
import com.example.smarthome.Utils.ItemOffsetDecoration;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
            case "1":
                Picasso.get().load(R.drawable.home_background).fit().into(mImageView);
                break;
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

            String Url = Comm.API_ADDRESS + "Apartment/" + mApartmentId;
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
                    mRoomAdapter = new RoomAdapter(getChildFragmentManager(), Global.sApartment.getListRooms());
                    sVpRooms.setAdapter(mRoomAdapter);
                    mTlRoom.setupWithViewPager(sVpRooms, true);

                    //Set up scenes
                    mScenes = Global.sApartment.getScenes();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(RecyclerView.HORIZONTAL);

                    mSceneAdapter = new SceneAdapter(getActivity(), mScenes);
                    mSceneAdapter.setOnItemClickListener(HomeFragment.this);

                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.scene_space);
                    mRvScene.setHasFixedSize(true);
                    mRvScene.setLayoutManager(layoutManager);
                    mRvScene.setAdapter(mSceneAdapter);
                    mRvScene.addItemDecoration(itemDecoration);

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

}
