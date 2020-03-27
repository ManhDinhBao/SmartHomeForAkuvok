package com.example.smarthome;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.smarthome.Fragment.Home.HomeFragment;
import com.example.smarthome.Fragment.Home.RoomFragment;
import com.example.smarthome.Model.Apartment;
import com.example.smarthome.Model.Device.Device;
import com.example.smarthome.Model.Device.DeviceIcon;
import com.example.smarthome.Model.Device.DeviceType;
import com.example.smarthome.Model.Message.Message;
import com.example.smarthome.Model.Point.Point;
import com.example.smarthome.Model.Room.Room;
import com.example.smarthome.Utils.Comm;

import java.util.ArrayList;

public class Global {
    public static Apartment sApartment = null;
    public static ArrayList<Message> sListMessage = null;

    public static void ShowDialog(String title, String content, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(false);
        builder.setNegativeButton(context.getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return true;
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    public static boolean UpdatePointValueByTopic(String topic, String value) {
        for (Room room : sApartment.getListRooms()) {
            for (Device device : room.getDevices()) {
                for (Point point : device.getPoints()) {
                    if (point.getSubcribeAddress().matches(topic)) {
                        switch (point.getType().getId()) {
                            case Comm.POINT_TYPE_BOOL:
                                if (value.matches("0")) {
                                    point.setValue(0);
                                    device.setPower(false);
                                    Log.d("SmartHome", "Device change to " + device.getName() + "OFF");
                                } else {
                                    point.setValue(1);
                                    device.setPower(true);
                                    Log.d("SmartHome", "Device change to " + device.getName() + "ON");
                                }
                                device.setCurrentValue(value);
                                break;
                            case Comm.POINT_TYPE_NUMBER:
                                if (device.getType().getID().matches(Comm.DEVICE_TYPE_AC) && point.getAlias().matches(Comm.POINT_ALIAS_TEMPERATURE)) {
                                    device.setCurrentValue(value);
                                } else if (device.getType().getID().matches(Comm.DEVICE_TYPE_LIGHT) && point.getAlias().matches(Comm.POINT_ALIAS_DIM)) {
                                    device.setCurrentValue(value);
                                } else if (device.getType().getID().matches(Comm.DEVICE_TYPE_CURTAIN) && point.getAlias().matches(Comm.POINT_ALIAS_MODE)) {
                                    device.setCurrentValue(value);
                                }
                                point.setValue(Integer.parseInt(value));
                                break;
                            case Comm.POINT_TYPE_TEXT:
                                point.setValue(0);
                                break;
                        }
                        if (RoomFragment.mDeviceAdapter != null) {
                            RoomFragment.mDeviceAdapter.notifyDataSetChanged();
                            HomeFragment.sVpRooms.getAdapter().notifyDataSetChanged();
                            Log.d("SmartHome", "Updated device adapter");
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Room GetRoomById(String roomId) {
        for (Room room : sApartment.getListRooms()) {
            if (room.getId().matches(roomId)) {
                return room;
            }
        }
        return null;
    }

    public static void ReSetupDevice() {
        for (Room room : sApartment.getListRooms()) {
            if (room != null) {
                for (Device device : room.getDevices()) {
                    if (device != null) {
                        device.SetDeviceViewType();
                    }

                }
            }

        }
    }

}
