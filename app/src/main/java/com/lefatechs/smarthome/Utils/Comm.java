package com.lefatechs.smarthome.Utils;

public class Comm {
    public static  String API_ADDRESS                     = "https://192.168.19.43/SmartHomeAPI/api/";
    public static final String WEATHER_API                = "http://api.openweathermap.org/data/2.5/weather";
    public static final String WEATHER_API_CITY_HANOI     = "?id=1581129";
    public static final String WEATHER_API_CELIUS         = "&units=metric";
    public static       String WEATHER_API_LANGUAGE       = "&lang=en";
    public static final String WEATHER_API_KEY            = "&appid=e948490e32c40cfe0efdf041dff94736";
    public static  String SERVER_IP                       = "192.168.19.51";
    public static final int SERVER_PORT                   = 16707;

    public static final String MESSAGE_CODE_LOGIN           = "1201";
    public static final String MESSAGE_CODE_LOGIN_RSP       = "1202";
    public static final String MESSAGE_CODE_SEND_POINT      = "1203";
    public static final String MESSAGE_CODE_POINT_STATUS    = "1204";
    public static final String MESSAGE_CODE_CURRENT_POINT_STATUS    = "1205";

    public static final String POINT_TYPE_BOOL              = "PTY0000001";
    public static final String POINT_TYPE_NUMBER            = "PTY0000002";
    public static final String POINT_TYPE_TEXT              = "PTY0000003";

    public static final String DEVICE_TYPE_LIGHT                = "DTY0000001";
    public static final String DEVICE_TYPE_AC                   = "DTY0000002";
    public static final String DEVICE_TYPE_EW                   = "DTY0000003";
    public static final String DEVICE_TYPE_BUTTON               = "DTY0000004";
    public static final String DEVICE_TYPE_TV                   = "DTY0000006";
    public static final String DEVICE_TYPE_DOOR                 = "DTY0000007";
    public static final String DEVICE_TYPE_FAN                  = "DTY0000008";
    public static final String DEVICE_TYPE_WASHING_MACHINE      = "DTY0000009";
    public static final String DEVICE_TYPE_CURTAIN              = "DTY0000010";
    public static final String DEVICE_TYPE_BUTTON_MODE          = "DTY0000011";
    public static final String DEVICE_TYPE_LIGHT_NODIM          = "DTY0000012";
    public static final String DEVICE_TYPE_HOOD                 = "DTY0000013";
    public static final String DEVICE_TYPE_SCENE                = "DTY0000014";

    public static final String POINT_ALIAS_TEMPERATURE             = "SetTemp";
    public static final String POINT_ALIAS_POWER                   = "Power";
    public static final String POINT_ALIAS_MODE                    = "Mode";
    public static final String POINT_ALIAS_DIM                     = "Dim";
    public static final String POINT_ALIAS_FAN_SPE                 = "FanSpeed";
    public static final String POINT_ALIAS_CURTAIN                 = "CurtainCmd";

}
