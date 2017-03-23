/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer;


import android.content.Context;
import android.content.SharedPreferences;

public class DataProvider {

    private Context context;
    SharedPreferences data;

    private static final String SHARED_PREFERENCE_NAME = "SmartPortTelemetrer";

    public static final String KEY_BT_MAC = "mac-address";
    public static final String KEY_BT_NAME = "device-name";

    public DataProvider(Context context) {
        this.context = context;
        data = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = data.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return data.getString(key, null);
    }

}
