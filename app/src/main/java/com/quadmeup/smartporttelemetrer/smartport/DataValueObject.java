/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.quadmeup.smartporttelemetrer.smartport;


public class DataValueObject {

    private DataTypeObject dataType;
    private long rawValue;

    //TODO add data type

    public DataValueObject(DataTypeObject dataType, long rawValue) {
        this.dataType = dataType;
        this.rawValue = rawValue;
    }

    public DataTypeObject getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeObject dataType) {
        this.dataType = dataType;
    }

    public long getRawValue() {
        return rawValue;
    }

    public void setRawValue(long rawValue) {
        this.rawValue = rawValue;
    }
}
