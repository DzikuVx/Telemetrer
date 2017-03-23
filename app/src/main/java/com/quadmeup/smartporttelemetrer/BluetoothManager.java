/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer;

import android.bluetooth.BluetoothAdapter;

public class BluetoothManager {

    private static BluetoothManager instance;

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothManager() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothManager getInstance() {
        if (instance == null) {
            instance = new BluetoothManager();
        }

        return instance;
    }

    public boolean isAvailable() {
        return !(bluetoothAdapter == null);
    }

    public boolean isEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public BluetoothAdapter getAdapter() {
        return bluetoothAdapter;
    }

}
