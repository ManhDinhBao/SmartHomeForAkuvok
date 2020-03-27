package com.example.smarthome;

import java.util.ArrayList;

public class TCPMessageObject {
    private String Code;
    private int Length;
    private ArrayList<String> Params;

    public TCPMessageObject(String code, ArrayList<String> params) {
        Code = code;
        Params = params;
        int messLen = 0;
        for (String content: params) {
            messLen+=(content.length()+1);
        }
        Length = messLen+(Code.length()+1)+4;
    }

    public TCPMessageObject() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public ArrayList<String> getParams() {
        return Params;
    }

    public void setParams(ArrayList<String> params) {
        Params = params;
    }

    public String toString(){
        String result="";
        String strLength= Integer.toString(Length);
        if (strLength.length()<4){
            int diff = 4-strLength.length();
            for (int i=0;i<diff;i++){
                strLength = "0"+strLength;
            }
        }
        result = Code+";"+strLength;
        if (Params.size() > 0)
        {
            for (String s:Params) {
                result+=(";"+s);
            }
        }
        return result;
    }

    public String toAscii(){
        String s = toString();
        char[] chars = s.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }

    public String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }
        System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }

}
