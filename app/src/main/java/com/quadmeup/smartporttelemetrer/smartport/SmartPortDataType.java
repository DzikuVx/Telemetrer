/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.quadmeup.smartporttelemetrer.smartport;

import java.util.HashMap;
import java.util.Map;

public class SmartPortDataType {

    static private Map<Integer, DataTypeObject> dataTypeMap;

    static {
        dataTypeMap = new HashMap<>();

        dataTypeMap.put(0x0830, new DataTypeObject(0x0830, "GroundSpeed", "knots/1000"));
        dataTypeMap.put(0x0210, new DataTypeObject(0x0210, "VFAS", "V/100"));
        dataTypeMap.put(0x0200, new DataTypeObject(0x0200, "Current", "A/100"));
        dataTypeMap.put(0x050F, new DataTypeObject(0x050F, "RPM", "RPM"));
        dataTypeMap.put(0x0100, new DataTypeObject(0x0100, "Altitude", "m/100"));
        dataTypeMap.put(0x0600, new DataTypeObject(0x0600, "Fuel", "mAh"));
        dataTypeMap.put(0xF102, new DataTypeObject(0xF102, "ADC1", ""));
        dataTypeMap.put(0xF103, new DataTypeObject(0xF103, "ADC2", ""));
        dataTypeMap.put(0x0800, new DataTypeObject(0x0800, "LatLong", ""));
        dataTypeMap.put(0x0600, new DataTypeObject(0x0600, "CapUsed", ""));
        dataTypeMap.put(0x0110, new DataTypeObject(0x0110, "Vario", "m/100"));
        dataTypeMap.put(0x0300, new DataTypeObject(0x0300, "Cell1", "V/100"));
        dataTypeMap.put(0x0301, new DataTypeObject(0x0301, "Cell2", "V/100"));
        dataTypeMap.put(0x0302, new DataTypeObject(0x0302, "Cell3", "V/100"));
        dataTypeMap.put(0x0303, new DataTypeObject(0x0303, "Cell4", "V/100"));
        dataTypeMap.put(0x0304, new DataTypeObject(0x0304, "Cell5", "V/100"));
        dataTypeMap.put(0x0305, new DataTypeObject(0x0305, "Cell6", "V/100"));
        dataTypeMap.put(0x0306, new DataTypeObject(0x0306, "Cell7", "V/100"));
        dataTypeMap.put(0x0307, new DataTypeObject(0x0307, "Cell8", "V/100"));
        dataTypeMap.put(0x0308, new DataTypeObject(0x0308, "Cell9", "V/100"));
        dataTypeMap.put(0x0309, new DataTypeObject(0x0309, "Cell10", "V/100"));
        dataTypeMap.put(0x030A, new DataTypeObject(0x030A, "Cell11", "V/100"));
        dataTypeMap.put(0x030B, new DataTypeObject(0x030B, "Cell12", "V/100"));
        dataTypeMap.put(0x030C, new DataTypeObject(0x030C, "Cell13", "V/100"));
        dataTypeMap.put(0x030D, new DataTypeObject(0x030D, "Cell14", "V/100"));
        dataTypeMap.put(0x030E, new DataTypeObject(0x030E, "Cell15", "V/100"));
        dataTypeMap.put(0x030F, new DataTypeObject(0x030F, "Cell16", "V/100"));
        dataTypeMap.put(0x0840, new DataTypeObject(0x0840, "Heading", "deg/100"));
        dataTypeMap.put(0x0700, new DataTypeObject(0x0700, "AccX", "g/100"));
        dataTypeMap.put(0x0710, new DataTypeObject(0x0710, "AccY", "g/100"));
        dataTypeMap.put(0x0720, new DataTypeObject(0x0720, "AccZ", "g/100"));
        dataTypeMap.put(0x0400, new DataTypeObject(0x0400, "T1", ""));
        dataTypeMap.put(0x0410, new DataTypeObject(0x0410, "T2", ""));
        dataTypeMap.put(0x0420, new DataTypeObject(0x0420, "HomeDistance", "m"));
        dataTypeMap.put(0x0820, new DataTypeObject(0x0820, "GPS Altitude", "m/100"));
        dataTypeMap.put(0x0A00, new DataTypeObject(0x0A00, "AirSpeed", "knots/1000"));
        dataTypeMap.put(0x0900, new DataTypeObject(0x0900, "A3", ""));
        dataTypeMap.put(0x0910, new DataTypeObject(0x0910, "A4", ""));
    }

    public static Map<Integer, DataTypeObject> getDataTypeMap() {
        return dataTypeMap;
    }
}
