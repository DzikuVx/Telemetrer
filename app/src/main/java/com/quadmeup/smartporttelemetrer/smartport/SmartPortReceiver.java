/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer.smartport;

import com.quadmeup.smartporttelemetrer.UAV;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SmartPortReceiver {

    private UAV uav;

    public SmartPortReceiver(UAV uav) {
        this.uav = uav;
    }

    void put(int sensor, byte[] dataType, byte[] dataValue) {

        int type = (int) getUnsignedInt(dataType);
        long value;


        switch (type) {

            /*
             * FVAS sensor
             */
            case 0x0210:
                uav.setBatteryVoltage((float)getUnsignedInt(dataValue) / 100);

                break;

            /*
             * Heading
             */
            case 0x0840:
                uav.setBatteryVoltage((float)getUnsignedInt(dataValue) / 100);

                break;


        }

    }

    private static long getUnsignedInt(byte[] data)
    {
        long tmp = 0;
        for (int i = 0; i < data.length; i++) {
            tmp += data[i] << (8 * i);
        }
        return tmp;
    }

    private static long getSignedInt(byte[] data)
    {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getInt();
    }

}
