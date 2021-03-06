/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.quadmeup.smartporttelemetrer;

public class UAV {

    float batteryVoltage;

    float accX;
    float accY;
    float accZ;

    long heading;
    long cellVoltage;
    long RSSI;
    long relativeAltitude;
    long absoluteAltitude;

    public float getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(float batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public long getCellVoltage() {
        return cellVoltage;
    }

    public void setCellVoltage(long cellVoltage) {
        this.cellVoltage = cellVoltage;
    }

    public long getRSSI() {
        return RSSI;
    }

    public void setRSSI(long RSSI) {
        this.RSSI = RSSI;
    }

    public long getRelativeAltitude() {
        return relativeAltitude;
    }

    public void setRelativeAltitude(long relativeAltitude) {
        this.relativeAltitude = relativeAltitude;
    }

    public long getAbsoluteAltitude() {
        return absoluteAltitude;
    }

    public void setAbsoluteAltitude(long absoluteAltitude) {
        this.absoluteAltitude = absoluteAltitude;
    }

    public long getHeading() {
        return heading;
    }

    public void setHeading(long heading) {
        this.heading = heading;
    }

    public float getAccX() {
        return accX;
    }

    public void setAccX(float accX) {
        this.accX = accX;
    }

    public float getAccY() {
        return accY;
    }

    public void setAccY(float accY) {
        this.accY = accY;
    }

    public float getAccZ() {
        return accZ;
    }

    public void setAccZ(float accZ) {
        this.accZ = accZ;
    }
}
