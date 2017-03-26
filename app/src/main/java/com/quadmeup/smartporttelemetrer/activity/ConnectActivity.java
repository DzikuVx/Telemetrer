/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quadmeup.smartporttelemetrer.BluetoothManager;
import com.quadmeup.smartporttelemetrer.BluetoothService;
import com.quadmeup.smartporttelemetrer.DataProvider;
import com.quadmeup.smartporttelemetrer.ProtocolDecoder;
import com.quadmeup.smartporttelemetrer.R;
import com.quadmeup.smartporttelemetrer.UAV;
import com.quadmeup.smartporttelemetrer.smartport.SmartPortProtocol;
import com.quadmeup.smartporttelemetrer.smartport.SmartPortReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectActivity extends AppCompatActivity {

    static final private String TAG = "SPBT";

    private BluetoothManager bluetoothManager;
    private BluetoothService bluetoothService;

    static int REQUEST_ENABLE_BT = 7652;

    TextView deviceName;
    TextView stateLabel;

    private class PopulateUI extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String result) {

            DataProvider dataProvider = new DataProvider(getApplicationContext());

            String value = dataProvider.getString(DataProvider.KEY_BT_NAME);

            if (value != null) {
                deviceName.setText(value);
            }

            stateLabel.setText(bluetoothService.getConnectionState());

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

        UAV uav = new UAV();

        SmartPortReceiver smartportReceiver = new SmartPortReceiver(uav);

        ArrayList<ProtocolDecoder> decoders = new ArrayList<>();
        decoders.add(new SmartPortProtocol(smartportReceiver));

        bluetoothService = BluetoothService.getInstance();
        bluetoothService.setBluetoothAdapter(bluetoothManager.getAdapter());
        bluetoothService.setDecoders(decoders);

        deviceName = (TextView) findViewById(R.id.device_name);
        stateLabel = (TextView) findViewById(R.id.state_label);

        final Button buttonDisconnect = (Button) findViewById(R.id.button_disconnect);
        final Button buttonConnect = (Button) findViewById(R.id.button_connect);

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonConnect.setVisibility(View.VISIBLE);
                buttonDisconnect.setVisibility(View.INVISIBLE);

                bluetoothService.disconnect();
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
                bluetoothService.connect(dataProvider.getString(DataProvider.KEY_BT_MAC));

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
