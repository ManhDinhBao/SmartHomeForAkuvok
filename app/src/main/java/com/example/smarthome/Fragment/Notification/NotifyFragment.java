package com.example.smarthome.Fragment.Notification;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smarthome.Fragment.Home.HomeFragment;
import com.example.smarthome.Fragment.Home.RoomAdapter;
import com.example.smarthome.Fragment.Home.SceneAdapter;
import com.example.smarthome.Global;
import com.example.smarthome.LoginActivity;
import com.example.smarthome.MainActivity;
import com.example.smarthome.Model.Apartment;
import com.example.smarthome.Model.Message.Message;
import com.example.smarthome.R;
import com.example.smarthome.Utils.Comm;
import com.example.smarthome.Utils.ItemOffsetDecoration;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotifyFragment extends Fragment implements NotifyAdapter.OnItemClickListener {
    private TextView mTvNotificationHeader;
    private RecyclerView mRvListNotification;
    private ProgressDialog dialogProgress;
    private ViewPager mVpNotificationContent;
    private TabLayout mTlNotificationList;
    private String mUserId;
    private NotifyAdapter mNotifyAdapter;
    private ImageView mImageView;
    private NotifyImageAdapter mNotifyImageAdapter;
    public final static String PREF_BACKGROUND = "background";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        InitControl(view);
        GetValue();
        GetNotifyData();
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
        }

        //Set header text
        mTvNotificationHeader.setText(getActivity().getString(R.string.label_notification));
    }

    //When user click notification item to show detail
    @Override
    public void onItemClick(int position) {
        Message message = Global.sListMessage.get(position);
        ArrayList<String> lstAttachAddress = message.getAttachAddress();
        mNotifyImageAdapter =new NotifyImageAdapter(getFragmentManager(),lstAttachAddress);
        mVpNotificationContent.setAdapter(mNotifyImageAdapter);
        mTlNotificationList.setupWithViewPager(mVpNotificationContent,true);
    }

    //Define controls
    private void InitControl(View view){
        mTvNotificationHeader = view.findViewById(R.id.textView_NotificationFragment_Header);
        mRvListNotification = view.findViewById(R.id.recycleView_NotificationFragment_ListNotification);
        mVpNotificationContent = view.findViewById(R.id.viewPager_NotificationFragment_Content);
        mTlNotificationList = view.findViewById(R.id.tabLayout_NotificationFragment_ContentList);
        mImageView = view.findViewById(R.id.imageView_FragmentNotify_Background);
    }

    //Get value from main activity
    private void GetValue() {
        mUserId = getArguments().getString("MAIN_USER_ID");
    }

    //Get notification data from server
    private void GetNotifyData() {
        if (mUserId != null && !mUserId.matches("")) {
            //Show waiting dialog
            dialogProgress = new ProgressDialog(getActivity());
            dialogProgress.setMessage(getActivity().getResources().getString(R.string.message_load_data));
            dialogProgress.setCancelable(false);
            dialogProgress.show();

            String Url = Comm.API_ADDRESS + "Message/" + mUserId;
            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String jsonData = response;
                    try {
                        TypeToken<ArrayList<Message>> token = new TypeToken<ArrayList<Message>>() {};
                        Global.sListMessage = new Gson().fromJson(jsonData, token.getType());

                        dialogProgress.dismiss();
                        Log.d("SmartHome", "Load messages data success");
                    }
                    catch (Exception ex){
                        Log.e("SmartHome", "Convert message data from json to object error");
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
                                MainActivity.mTcpClient=null;

                                //Go to login screen
                                Intent intent =new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    //Set up notification list
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(RecyclerView.VERTICAL);

                    mNotifyAdapter = new NotifyAdapter(getActivity(), Global.sListMessage);
                    mNotifyAdapter.setOnItemClickListener(NotifyFragment.this);

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                            LinearLayoutManager.VERTICAL);

                    dividerItemDecoration.setDrawable(getContext().getDrawable(R.drawable.item_decoration));

                    mRvListNotification.setHasFixedSize(true);
                    mRvListNotification.setLayoutManager(layoutManager);
                    mRvListNotification.setAdapter(mNotifyAdapter);
                    mRvListNotification.addItemDecoration(dividerItemDecoration);

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
                            MainActivity.mTcpClient=null;

                            //Go to login screen
                            Intent intent =new Intent(getActivity(),LoginActivity.class);
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
