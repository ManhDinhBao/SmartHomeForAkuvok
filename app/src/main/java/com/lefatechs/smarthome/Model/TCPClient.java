package com.lefatechs.smarthome.Model;

import android.system.ErrnoException;
import android.util.Log;

import com.lefatechs.smarthome.Utils.Comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

public class TCPClient {
    private String serverMessage;
    private boolean isConnected;

    private OnMessageReceived mMessageListener = null;
    private OnServerClosed mServerCloseListerner = null;
    private OnConnectionTimeOut onConnectionTimeOut = null;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;
    Socket socket;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener, OnServerClosed listerner1, OnConnectionTimeOut listerner2) {
        mMessageListener = listener;
        mServerCloseListerner = listerner1;
        onConnectionTimeOut = listerner2;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void SendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.print(message);
            out.flush();
            Log.d("SmartHome", "TCPClient - Send: " + message);
        }
    }

    public void DisConnect() {
        mRun = false;

        if (out != null) {
            out.flush();
            out.close();
        }

        mMessageListener = null;
        in = null;
        out = null;

        setConnected(false);
    }

    public void Connect() {
        mRun = true;
        try {
            InetAddress serverAddr = InetAddress.getByName(Comm.SERVER_IP);

            //create a socket to make the connection with the server
            socket = new Socket();
            socket.setTcpNoDelay(true);
            //socket.setKeepAlive(true);
            socket.connect(new InetSocketAddress(serverAddr, Comm.SERVER_PORT),5000);

            try {
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                int charsRead = 0;
                char[] buffer = new char[1024];
                //in this while the client listens for the messages sent by the server
                isConnected = true;
                while (mRun) {
                    charsRead = in.read(buffer);
                    //serverMessage = in.readLine();
                    serverMessage = new String(buffer).substring(0, charsRead);

                    if (serverMessage != null && mMessageListener != null) {
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }

            } catch (Exception ex) {
                Log.e("TCPClient", " Error" + ex.getMessage());
                mServerCloseListerner.serverClosed();
            } finally {
                mRun = false;
                isConnected = false;
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (SocketException se) {
            Log.e("TCPClient", "Connection time out: ", se);
            //onConnectionTimeOut.timeOut();
        } catch (IOException e){

        } catch (Exception e) {
            Log.e("TCPClient", "C: Error", e);

        } finally {
            mRun = false;
            isConnected = false;
        }
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on asyncTask doInBackground
    public interface OnMessageReceived {
        void messageReceived(String message);
    }

    public interface OnServerClosed {
        void serverClosed();
    }

    public interface OnConnectionTimeOut {
        void timeOut();
    }


    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
