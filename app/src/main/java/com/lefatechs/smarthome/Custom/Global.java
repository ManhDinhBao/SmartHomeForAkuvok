package com.lefatechs.smarthome.Custom;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.lefatechs.smarthome.Model.User.User;
import com.lefatechs.smarthome.R;
import com.lefatechs.smarthome.View.Fragment.Home.HomeFragment;
import com.lefatechs.smarthome.View.Fragment.Home.RoomFragment;
import com.lefatechs.smarthome.Model.Apartment;
import com.lefatechs.smarthome.Model.Device.Device;
import com.lefatechs.smarthome.Model.Message.Message;
import com.lefatechs.smarthome.Model.Point.Point;
import com.lefatechs.smarthome.Model.Room.Room;
import com.lefatechs.smarthome.Utils.Comm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Global {
    public static Apartment sApartment = null;
    public static User sUser = null;

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
        try {
            if (sApartment.getRooms() != null && sApartment.getRooms().size() > 0) {
                for (Room room : sApartment.getRooms()) {
                    if (room.getDevices() != null && room.getDevices().size() > 0) {
                        for (Device device : room.getDevices()) {
                            if (device.getPoints() != null && device.getPoints().size() > 0) {
                                for (Point point : device.getPoints()) {
                                    if (point != null && point.getSubcribeAddress().matches(topic)) {
                                        switch (point.getPType().getId()) {
                                            case Comm.POINT_TYPE_BOOL:
                                                if (value.matches("0")) {
                                                    point.setValue(0);
                                                    device.setPower(false);
                                                    Log.d("SmartHome", device.getName() + " ->OFF");
                                                } else {
                                                    point.setValue(1);
                                                    device.setPower(true);
                                                    Log.d("SmartHome", device.getName() + " ->ON");
                                                }
                                                if (!device.getDType().getID().matches(Comm.DEVICE_TYPE_AC) && !device.getDType().getID().matches(Comm.DEVICE_TYPE_LIGHT) && !device.getDType().getID().matches(Comm.DEVICE_TYPE_CURTAIN))
                                                    device.setCurrentValue(value);
                                                break;
                                            case Comm.POINT_TYPE_NUMBER:
                                                if (device.getDType().getID().matches(Comm.DEVICE_TYPE_AC) && point.getAlias().matches(Comm.POINT_ALIAS_TEMPERATURE)) {
                                                    device.setCurrentValue(value);
                                                } else if (device.getDType().getID().matches(Comm.DEVICE_TYPE_LIGHT) && point.getAlias().matches(Comm.POINT_ALIAS_DIM)) {
                                                    if (value.matches("0")) {
                                                        point.setValue(0);
                                                        device.setPower(false);
                                                        Log.d("SmartHome", device.getName() + "OFF");
                                                    } else {
                                                        point.setValue(100);
                                                        device.setPower(true);
                                                        Log.d("SmartHome", device.getName() + "ON");
                                                    }
                                                    device.setCurrentValue(value);
                                                } else if (device.getDType().getID().matches(Comm.DEVICE_TYPE_CURTAIN) && point.getAlias().matches(Comm.POINT_ALIAS_MODE)) {
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
                                        //return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            Log.d("Update point", "Update point false - Detail: " + ex.getMessage() + ex.getStackTrace()[0].getLineNumber());
            return false;
        }
    }

    public static Room GetRoomById(String roomId) {
        for (Room room : sApartment.getRooms()) {
            if (room.getId().matches(roomId)) {
                return room;
            }
        }
        return new Room();
    }

    public static void ReSetupDevice() {
        for (Room room : sApartment.getRooms()) {
            if (room != null) {
                for (Device device : room.getDevices()) {
                    if (device != null) {
                        device.SetDeviceViewType();
                    }

                }
            }

        }
    }

    public static ArrayList<Message> GetMessage() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Message m1 = new Message();
        m1.setId("MES0000001");
        m1.setTitle("Hướng dẫn khai báo y tế trên ứng dụng NCOV");
        m1.setDetail("Bằng cách khai báo y tế trên ứng dụng NCOVI, mỗi chúng ta đã đóng góp phần công sức vào công cuộc phòng và chống đại dịch cúm Corona");
        m1.setEmergency(true);
        try {
            m1.setSendDate(sdf.parse("12/03/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> attachments = new ArrayList<>();
        attachments.add("m1_a");
        attachments.add("m1_b");
        attachments.add("m1_c");
        attachments.add("m1_d");
        attachments.add("m1_e");
        m1.setAttachAddress(attachments);

        Message m2 = new Message();
        m2.setId("MES0000002");
        m2.setTitle("Triển khai thực hiện chủ trương khai báo y tế toàn dân");
        m2.setDetail("Thực hiện công văn số 232/STTTT-TTBCXB ngày 12/03/2020 và công văn số 241/232/STTTT-TTBCXB của sở thông tin truyền ");
        m2.setEmergency(true);
        try {
            m2.setSendDate(sdf.parse("15/03/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> attachments2 = new ArrayList<>();
        attachments2.add("m2_a");
        attachments2.add("m2_b");
        m2.setAttachAddress(attachments2);

        Message m3 = new Message();
        m3.setId("MES0000003");
        m3.setTitle("Triển khai ứng dụng khai báo y tế NCOVI");
        m3.setDetail("Thực hiện chỉ đạo của Thủ tướng Chính phủ về việc tăng cường phòng, chống dịch bệnh COVID-19.");
        m3.setEmergency(true);
        try {
            m3.setSendDate(sdf.parse("20/04/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> attachments3 = new ArrayList<>();
        attachments3.add("m3_a");
        attachments3.add("m3_b");
        attachments3.add("m3_c");
        m3.setAttachAddress(attachments3);

        Message m4 = new Message();
        m4.setId("MES0000004");
        m4.setTitle("Tiếp tục cho học sinh nghỉ học");
        m4.setDetail("Do diễn biến phức tạp của bệnh dịch. Trường THCS Nguyễn Lương Bằng thông báo đến các bậc PH cùng các em học sinh nhà trường như sau.");
        m4.setEmergency(true);
        try {
            m4.setSendDate(sdf.parse("30/04/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> attachments4 = new ArrayList<>();
        attachments4.add("m4_a");
        m4.setAttachAddress(attachments4);

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(m1);
        messages.add(m2);
        messages.add(m3);
        messages.add(m4);

        return messages;
    }

    public static String UpperCaseAllFirst(String value) {

        char[] array = value.toCharArray();
        // Uppercase first letter.
        array[0] = Character.toUpperCase(array[0]);

        // Uppercase all letters that follow a whitespace character.
        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }

        // Result.
        return new String(array);
    }
}
