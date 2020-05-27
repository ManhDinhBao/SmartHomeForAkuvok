package com.lefatechs.smarthome.View.Fragment.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lefatechs.smarthome.Controller.DeviceAdapter;
import com.lefatechs.smarthome.Custom.Global;
import com.lefatechs.smarthome.View.Activity.MainActivity;
import com.lefatechs.smarthome.Model.Device.Device;
import com.lefatechs.smarthome.Model.Point.Point;
import com.lefatechs.smarthome.Model.Room.Room;
import com.lefatechs.smarthome.R;
import com.lefatechs.smarthome.Model.TCPMessageObject;
import com.lefatechs.smarthome.Utils.Comm;
import com.lefatechs.smarthome.Utils.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomFragment extends Fragment implements DeviceAdapter.OnItemClickListener, DeviceAdapter.OnStopTrackingTouch, DeviceAdapter.OnTouchListener {

    private Room mRoom;
    private String mRoomId;
    private RecyclerView mRvDevice;
    public static DeviceAdapter mDeviceAdapter;

    public static RoomFragment newInstance(String roomId) {
        RoomFragment myFragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString("ROOM_ID", roomId);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_room, container, false);

        InitControl(view);
        GetRoomData();

        return view;
    }

    //When user click material card device
    @Override
    public void onItemClick(int position) {
        //Set default value for device power
        int onValue = 1, offValue = 0;
        Device device = mRoom.getDevices().get(position);

        //Light dim -> ON=100, OFF=0
        if(device.getDType().getID().matches(Comm.DEVICE_TYPE_LIGHT)){
            onValue = 100;
            ArrayList<String> messParams = new ArrayList<>();

            String powerTopic = "";
            //Get publish topic
            String jsonPower = null;
            for (Point point : device.getPoints()) {
                if (point.getAlias().matches(Comm.POINT_ALIAS_DIM)) {
                    powerTopic = point.getPublishAddress();
                }
            }

            int value = device.isPower() ? offValue : onValue;

            SendMessageToServer(device, powerTopic, Integer.toString(value));
            return;
        }

        //If device type <> curtain, send device point value to server
        if (!device.getDType().getID().matches(Comm.DEVICE_TYPE_CURTAIN)) {
            ArrayList<String> messParams = new ArrayList<>();

            //Get curtain mode properties and publish topic
            String jsonPower = null;
            String powerTopic = "";
            for (Point point : device.getPoints()) {
                if (point.getAlias().matches(Comm.POINT_ALIAS_POWER)) {
                    jsonPower = point.getCharacter().getMap();
                    powerTopic = point.getPublishAddress();
                }
            }

            if (jsonPower != null) {
                try {
                    JSONObject objDevicePower = new JSONObject(jsonPower);
                    onValue = Integer.parseInt(objDevicePower.getString("ON"));
                    offValue = Integer.parseInt(objDevicePower.getString("OFF"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    offValue = 0;
                    onValue = 1;
                }
            } else {
                Log.e("SmartHome", "Can't get setting power of device " + device.getName());
                offValue = 0;
                onValue = 1;
            }

            int value = device.isPower() ? offValue : onValue;

            SendMessageToServer(device, powerTopic, Integer.toString(value));
        }
    }

    //When user click button inside curtain device
    //buttonPosition: Stop = 0, Open = 1, Close = 2
    @Override
    public void onItemClick(int position, int buttonPosition) {
        //Set default value for curtain mode
        int stopMode = 0, openMode = 1, closeMode = 2;

        Device device = mRoom.getDevices().get(position);

        //Get curtain mode properties and publish topic
        String jsonCurtainMode = null;
        String curtainPublishTopic = "";
        for (Point point : device.getPoints()) {
            if (point.getAlias().matches(Comm.POINT_ALIAS_CURTAIN)) {
                jsonCurtainMode = point.getCharacter().getMap();
                curtainPublishTopic = point.getPublishAddress();
            }
        }

        //If curtain mode no error, send data to server(if any setting error in DB, send default value)
        if (jsonCurtainMode != null) {
            try {
                JSONObject objCurtainMode = new JSONObject(jsonCurtainMode);
                JSONArray jsonArray = objCurtainMode.getJSONArray("VALUE");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (obj.has("STOP") && !obj.isNull("STOP")){
                        stopMode =  Integer.parseInt(obj.getString("STOP"));
                    }
                    else if (obj.has("OPEN") && !obj.isNull("OPEN")){
                        openMode = Integer.parseInt(obj.getString("OPEN"));
                    }
                    else if (obj.has("CLOSE") && !obj.isNull("CLOSE")){
                        closeMode = Integer.parseInt(obj.getString("CLOSE"));
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                stopMode = 0;
                openMode = 1;
                closeMode = 2;
            }



        } else {
            Log.e("SmartHome", "Can't get setting mode of device " + device.getName());
            stopMode = 0;
            openMode = 1;
            closeMode = 2;
        }

        int value = 3;
        switch (buttonPosition) {
            case 0:
                value = stopMode;
                break;
            case 1:
                value = openMode;
                break;
            case 2:
                value = closeMode;
                break;
        }

        SendMessageToServer(device, curtainPublishTopic, Integer.toString(value));
    }

    //When user touch in material card, disable recycle view scroll
    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            mRvDevice.requestDisallowInterceptTouchEvent(true);
        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            mRvDevice.requestDisallowInterceptTouchEvent(false);
        }
    }

    //When user stop tracking seek bar
    @Override
    public void onStopTrackingTouch(int position, int progress) {
        //Only AC and light dim have seek bar to change properties
        Device device = mRoom.getDevices().get(position);
        //If device = light dim, send dim to server
        if (device.getDType().getID().matches(Comm.DEVICE_TYPE_LIGHT)) {
            for (Point point : device.getPoints()) {
                if (point.getAlias().matches(Comm.POINT_ALIAS_DIM)) {
                    String dimTopic = point.getPublishAddress();
                    SendMessageToServer(device, dimTopic == null ? "":dimTopic, Integer.toString(progress));
                }
            }
        }

        //If device = AC, send temperature to server
        if (device.getDType().getID().matches(Comm.DEVICE_TYPE_AC)) {
            for (Point point : device.getPoints()) {
                if (point.getAlias().matches(Comm.POINT_ALIAS_TEMPERATURE)) {
                    String tempTopic = point.getPublishAddress();
                    SendMessageToServer(device, tempTopic == null ? "":tempTopic, Integer.toString(progress));
                }
            }
        }
    }

    //Define controls
    private void InitControl(View view) {
        mRvDevice = view.findViewById(R.id.recyclerView_fragmentRoom_Device);
    }

    //Get room data from Global
    private void GetRoomData() {
        mRoomId = getArguments().getString("ROOM_ID");
        Log.d("SmartHome", "ROOM = " + mRoomId);
        try {
            mRoom = Global.GetRoomById(mRoomId);

            if (mRoom != null && mRoom.getDevices().size() > 0) {

                mDeviceAdapter = new DeviceAdapter(getActivity().getBaseContext(), mRoom.getDevices());
                mDeviceAdapter.setOnItemClickListener(RoomFragment.this);
                mDeviceAdapter.onStopTrackingTouch(RoomFragment.this);
                mDeviceAdapter.onTouch(RoomFragment.this);

                //Space between two device
                ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.device_space);

                //Set up recycle view devices
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);

                mRvDevice.setHasFixedSize(true);
                mRvDevice.setLayoutManager(layoutManager);
                mRvDevice.setAdapter(mDeviceAdapter);
                mRvDevice.addItemDecoration(itemDecoration);
            } else {
                Log.e("SmartHome", "Can't find room " + mRoomId + " in apartment");
            }
        }catch (Exception ex){
            Log.e("SmartHome", mRoomId +  "null");
        }

    }

    //Send message to server via TCP
    private void SendMessageToServer(Device device, String topic, String value) {
        try {
            ArrayList<String> messParams = new ArrayList<>();
            messParams.add(topic);
            messParams.add(value);

            TCPMessageObject message = new TCPMessageObject(Comm.MESSAGE_CODE_SEND_POINT, messParams);
            MainActivity.mTcpClient.SendMessage(message.toString());
            mDeviceAdapter.notifyDataSetChanged();
            Log.d("SmartHome", "Send message from device " + device.getName() + " done.");
            Log.d("CODE", message.toString());
        } catch (Exception ex) {
            Log.e("SmartHome", "Send value message to sever error: " + ex.getMessage());
        }
    }

}
