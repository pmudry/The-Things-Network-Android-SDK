package org.ttn.android.sdk.v1.client;

import android.util.Log;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.json.JSONException;
import org.json.JSONObject;
import org.ttn.android.sdk.v1.api.MqttPacketConverter;
import org.ttn.android.sdk.v1.domain.Packet;

import java.net.URISyntaxException;

/*
 * Copyright 2016 Fabio Tiriticco / Fabway
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by fabiotiriticco on 3 June 2016.
 */
public class TTNMqttClient {
    private static final int MQTT_HOST_PORT = 1883;

    Topic mTopic;
    MQTT mMqtt = new MQTT();
    CallbackConnection mConnection;
    MqttPacketConverter mMqttPacketConverter = new MqttPacketConverter();

    public TTNMqttClient(String broker, String appEUI, String accessKey, String devEUI) {
        try {
            mMqtt.setHost(broker, MQTT_HOST_PORT);
            mMqtt.setUserName(appEUI);
            mMqtt.setPassword(accessKey);
            mTopic = new Topic(appEUI + "/devices/" + devEUI + "/down", QoS.AT_LEAST_ONCE);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void listen(final MqttApiListener listener) {

        // establish connection
        mConnection = mMqtt.callbackConnection();
        mConnection.listener(new Listener() {
            @Override
            public void onConnected() {
                Log.d("t", "onConnected");
                listener.onConnected();
            }

            @Override
            public void onDisconnected() {
                Log.d("t", "onDisconnected");
                listener.onDisconnected();
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {

                try {
                    JSONObject jsonObj = new JSONObject(body.ascii().toString());
                    Packet packet = (Packet) mMqttPacketConverter.fromJson(jsonObj);
                    listener.onPacket(packet);
                } catch (JSONException e) {
                    listener.onError(e);
                }

                ack.run();
            }

            @Override
            public void onFailure(Throwable value) {
                Log.d("t", "onFailure");
                listener.onError(value);
            }
        });

        // connect
        mConnection.connect(new Callback<Void>() {
            @Override
            public void onFailure(Throwable value) {
                listener.onError(value);
            }

            @Override
            public void onSuccess(Void value) {

                // subscribe
                mConnection.subscribe(new Topic[]{mTopic}, new Callback<byte[]>() {
                    public void onSuccess(byte[] qoses) {
                        // The result of the subscribe request.
                    }

                    public void onFailure(Throwable value) {
                        listener.onError(value);
                    }
                });
            }

        });

    }

    public void disconnect() {
        if (mConnection != null) {
            mConnection.disconnect(new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                }

                @Override
                public void onFailure(Throwable value) {

                }
            });
        }
    }
}
