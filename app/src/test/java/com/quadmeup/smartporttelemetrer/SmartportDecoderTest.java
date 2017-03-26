package com.quadmeup.smartporttelemetrer;

import com.quadmeup.smartporttelemetrer.smartport.SmartPortProtocol;
import com.quadmeup.smartporttelemetrer.smartport.SmartPortReceiver;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SmartportDecoderTest {

    public SmartportDecoderTest() {

    }

    @Test
    public void shouldDecodeByteStream() {

        UAV uav = new UAV();

        SmartPortReceiver smartportReceiver = new SmartPortReceiver(uav);

        SmartPortProtocol decoder = new SmartPortProtocol(smartportReceiver);
        ArrayList<Integer> testData = new ArrayList<>(Arrays.asList(
                0x7e, 0x1b, 0x10, 0x10, 0x02, 0x68, 0x06, 0x00, 0x00, 0x6f, 0x7e, 0xd0,
                0x7e, 0x1b, 0x10, 0x40, 0x08, 0x04, 0x88, 0x00, 0x00, 0x1b, 0x7e, 0x71,
                0x7e, 0x1b, 0x10, 0x10, 0x07, 0xff, 0xff, 0xff, 0xff, 0xd8, 0x7e, 0xac
        ));

        for (int i = 0; i < testData.size(); i++) {
            decoder.put(testData.get(i));
        }

        assertEquals(16.4f, uav.getBatteryVoltage(), 0.001f);
        assertEquals(348, uav.getHeading());
        assertEquals(-0.01f, uav.getAccY(), 0.001f);

    }

}
