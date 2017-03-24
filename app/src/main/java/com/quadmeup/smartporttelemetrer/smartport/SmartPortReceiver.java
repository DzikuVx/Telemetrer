/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer.smartport;

import com.quadmeup.smartporttelemetrer.UAV;

public class SmartPortReceiver {

    private UAV uav;

    public SmartPortReceiver(UAV uav) {
        this.uav = uav;
    }

    void put(int sensor, int[] dataType, int[] dataValue) {

        int type = (int) getInt(dataType);


        switch (type) {

            /*
             * FVAS sensor
             */
            case 0x0210:
                uav.setBatteryVoltage((float) getInt(dataValue) / 100);
                break;

            /*
             * Heading
             */
            case 0x0840:
                uav.setHeading(getInt(dataValue) / 100);
                break;

            /*
             * ACC_X
             */
            case 0x0700:
                uav.setAccX((float) getInt(dataValue) / 100);
                break;

            /*
             * ACC_Y
             */
            case 0x0710:
                uav.setAccY((float) getInt(dataValue) / 100);
                break;

            /*
             * ACC_Z
             */
            case 0x0720:
                uav.setAccZ((float) getInt(dataValue) / 100);
                break;

        }

    }

    private long getInt(int[] data)
    {
        long tmp = 0;
        for (int i = 0; i < data.length; i++) {
            tmp += data[i] << (8 * i);
        }
        return tmp;
    }

}
