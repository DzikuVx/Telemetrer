/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothService {

    private static BluetoothService instance;

    static final private String TAG = "SPBT";
    private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private ConnectionState connectionState = ConnectionState.DISCONNECTED;
    private BluetoothAdapter bluetoothAdapter;

    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private List<ProtocolDecoder> decoders;

    public static BluetoothService getInstance() {
        if (instance == null) {
            instance = new BluetoothService();
        }

        return instance;
    }

    private BluetoothService() {

    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void setDecoders(ArrayList<ProtocolDecoder> decoders) {
        this.decoders = decoders;
    }

    public void connect(String macAddress) {

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        connectionState = ConnectionState.DISCONNECTED;
        bluetoothAdapter.cancelDiscovery();

        device = bluetoothAdapter.getRemoteDevice(macAddress);

        connectThread = new ConnectThread(false);
        connectThread.start();

    }

    public void disconnect() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        connectionState = ConnectionState.DISCONNECTED;
    }

    public String getConnectionStateAsString() {
        return connectionState.toString();
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    private class ConnectThread extends Thread {
        private String mSocketType;

        ConnectThread(boolean secure) {
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);

            }
            socket = tmp;
            connectionState = ConnectionState.CONNECTING;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                socket.connect();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionState = ConnectionState.CONNECTION_FAILED;
                return;
            }

            connectionState = ConnectionState.SOCKET_CONNECTED;
            synchronized (BluetoothService.this) {
                connectThread = null;
            }

            Log.i(TAG, "Socket connected");

            connectedThread = new ConnectedThread(mSocketType);
            connectedThread.start();
        }

        void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        ConnectedThread(String socketType) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            connectionState = ConnectionState.CONNECTED;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (connectionState == ConnectionState.CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    //noinspection StatementWithEmptyBody
                    for (int i = 0; i < bytes; i++) {

                        /*
                         * send every byte as it comes into each registered
                         * protocol decoder
                         */
                        for (int j = 0; j < decoders.size(); j++) {
                            decoders.get(j).put((int)buffer[i] & 0xff);
                        }
                    }

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    if (connectedThread != null) {
                        connectedThread.cancel();
                    }
                    synchronized (BluetoothService.this) {
                        connectedThread = null;
                    }
                    connectionState = ConnectionState.DISCONNECTED;
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        @SuppressWarnings("unused")
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

}
