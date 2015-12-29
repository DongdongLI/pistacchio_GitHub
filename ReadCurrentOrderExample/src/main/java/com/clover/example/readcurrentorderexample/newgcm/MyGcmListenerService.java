/**
 * Copyright 2015 Google Inc. All Rights Reserved.
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
 */

package com.clover.example.readcurrentorderexample.newgcm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.clover.example.readcurrentorderexample.MainActivity;
import com.clover.example.readcurrentorderexample.asynctask.FetchOrdersTask;
import com.clover.example.readcurrentorderexample.event.OrdersAvailableEvent;
import com.google.android.gms.gcm.GcmListenerService;

import de.greenrobot.event.EventBus;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "demo1";
    private Handler handler = new Handler(Looper.getMainLooper());
    private EventBus bus=EventBus.getDefault();

    // get the message and use it
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        consume(message);
    }


    // the toast message is shown for testing purpose, and after that FetchOrder Asynctask will be initialized
    public void consume(final String mes){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
                // notify the MainActivity as soon as the GCM message is received, and let it start the fetching task.
                bus.post(new OrdersAvailableEvent("It's ready"));
            }
        });

    }
}
