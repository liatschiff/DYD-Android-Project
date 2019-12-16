package com.example.yodenproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class DeliveryPersonCallingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String number;

            String state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                if(number.equals("0526678008"))
                Toast.makeText(context, context.getResources().getString(R.string.delivery_call), Toast.LENGTH_SHORT).show();
            }

    }
}
