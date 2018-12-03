package org.ttn.android.sample;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.view.View;

import org.ttn.android.sdk.v1.client.MqttApiListener;
import org.ttn.android.sdk.v1.client.TTNMqttClient;
import org.ttn.android.sdk.v1.domain.Packet;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    final String TAG = ApplicationTest.class.getSimpleName();


    public ApplicationTest() {
        super(Application.class);



    }
}