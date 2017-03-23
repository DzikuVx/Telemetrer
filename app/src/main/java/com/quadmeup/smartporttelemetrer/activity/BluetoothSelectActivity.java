/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.quadmeup.smartporttelemetrer.BluetoothManager;
import com.quadmeup.smartporttelemetrer.DeviceListAdapter;
import com.quadmeup.smartporttelemetrer.R;

import java.util.ArrayList;

public class BluetoothSelectActivity extends AppCompatActivity {

    private ListView deviceList;
    private DeviceListAdapter listAdapter;
    BluetoothManager bluetoothManager;

    static int REQUEST_ENABLE_BT = 7652;

    private void fillDeviceList() {
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        ArrayList<BluetoothDevice> list = new ArrayList<>();
        list.addAll(bluetoothAdapter.getBondedDevices());

        listAdapter.setData(list);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillDeviceList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bluetooth_select);

        bluetoothManager = BluetoothManager.getInstance();

        deviceList = (ListView) findViewById(R.id.device_list);
        listAdapter = new DeviceListAdapter(this);
        deviceList.setAdapter(listAdapter);

        if (!bluetoothManager.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

        fillDeviceList();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            fillDeviceList();
        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.bt_device_has_to_be_enable), Toast.LENGTH_LONG).show();
            NavUtils.navigateUpFromSameTask(this);
        }

    }

}
