/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quadmeup.smartporttelemetrer.BluetoothManager;
import com.quadmeup.smartporttelemetrer.ConnectionState;
import com.quadmeup.smartporttelemetrer.DataProvider;
import com.quadmeup.smartporttelemetrer.DataService;
import com.quadmeup.smartporttelemetrer.R;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectActivity extends AppCompatActivity {

    static final private String TAG = "SPBT";

    private BluetoothManager bluetoothManager;

    static int REQUEST_ENABLE_BT = 7652;

    private DataService mService;
    boolean mBound = false;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataService.DataServiceBinder binder = (DataService.DataServiceBinder) service;
            mService = binder.getService();
            Log.i(TAG, "Service connected");
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    TextView deviceName;
    TextView stateLabel;
    TextView vfas;
    Button buttonDisconnect;
    Button buttonConnect;

    private class PopulateUI extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String result) {

            DataProvider dataProvider = new DataProvider(getApplicationContext());

            String value = dataProvider.getString(DataProvider.KEY_BT_NAME);

            if (value != null) {
                deviceName.setText(value);
            }

            if (mService != null) {

                ConnectionState connectionState = mService.getConnectionState();

                stateLabel.setText(connectionState.toString());
                vfas.setText(new DecimalFormat("#.##").format((double)mService.getUav().getBatteryVoltage()));

                if (connectionState.equals(ConnectionState.DISCONNECTED) || connectionState.equals(ConnectionState.CONNECTION_FAILED)) {
                    buttonConnect.setVisibility(View.VISIBLE);
                    buttonDisconnect.setVisibility(View.INVISIBLE);
                } else {
                    buttonConnect.setVisibility(View.INVISIBLE);
                    buttonDisconnect.setVisibility(View.VISIBLE);
                }

            }

            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    private void updateUI() {
        new PopulateUI().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        bluetoothManager = BluetoothManager.getInstance();
        if (!bluetoothManager.isAvailable()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_bluetooth), Toast.LENGTH_LONG).show();
            return;
        }

        deviceName = (TextView) findViewById(R.id.device_name);
        stateLabel = (TextView) findViewById(R.id.state_label);
        vfas = (TextView) findViewById(R.id.vfas);
        buttonDisconnect = (Button) findViewById(R.id.button_disconnect);
        buttonConnect = (Button) findViewById(R.id.button_connect);

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonConnect.setVisibility(View.VISIBLE);
                buttonDisconnect.setVisibility(View.INVISIBLE);

                mService.disconnect();
            }
        });

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bluetoothManager.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    return;
                }

                buttonConnect.setVisibility(View.INVISIBLE);
                buttonDisconnect.setVisibility(View.VISIBLE);

                DataProvider dataProvider = new DataProvider(getApplicationContext());
                mService.connect(dataProvider.getString(DataProvider.KEY_BT_MAC));
            }
        });

        Button buttonSelectBt = (Button) findViewById(R.id.button_select_bt);
        buttonSelectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BluetoothSelectActivity.class);
                startActivity(intent);
            }
        });

        updateUI();

        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                updateUI();
            }
        };
        timer.schedule(doAsynchronousTask, 0, 200); //execute in every 50000 ms
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, DataService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode,
                           int resultCode,
                           Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {

        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {

        }

    }
}
