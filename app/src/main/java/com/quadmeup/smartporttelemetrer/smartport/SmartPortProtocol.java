/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer.smartport;

import com.quadmeup.smartporttelemetrer.ProtocolDecoder;

public class SmartPortProtocol implements ProtocolDecoder {

    static final private String TAG = "SPBT";

    private enum ProtocolState {
        NONE,
        FRAME_START,
        SENSOR_ID,
        DATA_FRAME_START,
        DATA_TYPE,
        DATE_VALUE,
        DATA_CRC
    }

    private int byteIndex = 0;
    private ProtocolState state = ProtocolState.NONE;
    private int sensorId;
    private int[] dataType;
    private int[] dataValue;
    private long crc;

    SmartPortReceiver receiver;

    public SmartPortProtocol(SmartPortReceiver receiver) {
        this.receiver = receiver;
    }

    private void reset() {
        state = ProtocolState.NONE;
        sensorId = 0x00;
        dataType = null;
        dataValue = null;
        crc = 0x00;
        byteIndex = 0;
    }

    private void computeCrc(int data) {
        long tmpCrc = crc;

        tmpCrc += data;
        tmpCrc += tmpCrc >> 8;
        tmpCrc &= 0xFF;

        crc = tmpCrc;
    }

    @Override
    public void put(int data) {

        if (state == ProtocolState.NONE && data == 0x7e) {
            state = ProtocolState.FRAME_START;
            crc = 0;
        } else if (state == ProtocolState.FRAME_START) {
            sensorId = data;
            state = ProtocolState.SENSOR_ID;
        } else if (state == ProtocolState.SENSOR_ID) {
            if (data == 0x10) {
                state = ProtocolState.DATA_FRAME_START;
                byteIndex = 0;
                dataType = new int[2];
                computeCrc(data);
            } else {
                reset();
            }
        } else if (state == ProtocolState.DATA_FRAME_START) {
            //Process 2 data type bytes
            dataType[byteIndex] = data;

            computeCrc(data);

            byteIndex++;
            if (byteIndex == 2) {
                state = ProtocolState.DATA_TYPE;
                byteIndex = 0;
                dataValue = new int[4];
            }
        } else if (state == ProtocolState.DATA_TYPE) {
            //Process 4 data bytes
            dataValue[byteIndex] = data;

            computeCrc(data);

            byteIndex++;
            if (byteIndex == 4) {
                state = ProtocolState.DATE_VALUE;
                byteIndex = 0;
            }
        } else if (state == ProtocolState.DATE_VALUE) {

            if (0xff - crc == data) {
                /*
                 * CRC matches, we can send data
                 */
                receiver.put(sensorId, dataType, dataValue);
            }

            /*
             * Frame processing finished, we are ready to process next frame
             */
            state = ProtocolState.DATA_CRC;
        } else if (state == ProtocolState.DATA_CRC) {
            reset();
        } else {
            reset();
        }

    }

}
