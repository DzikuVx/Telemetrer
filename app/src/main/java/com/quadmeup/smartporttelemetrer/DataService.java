package com.quadmeup.smartporttelemetrer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.quadmeup.smartporttelemetrer.smartport.SmartPortProtocol;
import com.quadmeup.smartporttelemetrer.smartport.SmartPortReceiver;

import java.util.ArrayList;

public class DataService extends Service {

    static final private String TAG = "SPBT";

    private UAV uav;
    private SmartPortReceiver smartportReceiver;
    private BluetoothService bluetoothService;
    private BluetoothManager bluetoothManager;

    public DataService() {

        Log.i(TAG, "DataService started");

        uav = new UAV();
        smartportReceiver = new SmartPortReceiver(uav);

        bluetoothManager = BluetoothManager.getInstance();

        ArrayList<ProtocolDecoder> decoders = new ArrayList<>();
        decoders.add(new SmartPortProtocol(smartportReceiver));

        bluetoothService = BluetoothService.getInstance();
        bluetoothService.setBluetoothAdapter(bluetoothManager.getAdapter());
        bluetoothService.setDecoders(decoders);
    }

    // Binder given to clients
    private final IBinder mBinder = new DataServiceBinder();

    @Override
    public void onDestroy() {
        bluetoothService.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "DataService binded");

        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class DataServiceBinder extends Binder {
        public DataService getService() {
            return DataService.this;
        }
    }

    public ConnectionState getConnectionState() {
        return bluetoothService.getConnectionState();
    }

    public void connect(String macAddress) {
        bluetoothService.connect(macAddress);
    }

    public void disconnect() {
        bluetoothService.disconnect();
    }

    public UAV getUav() {
        return uav;
    }

}
