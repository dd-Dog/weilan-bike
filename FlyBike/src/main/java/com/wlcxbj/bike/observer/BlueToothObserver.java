package com.wlcxbj.bike.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by bain on 17-2-9.
 */
public class BlueToothObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;
    public static final int MSG_BLUETOOTH_ON = 3;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public BlueToothObserver(Handler handler) {
        super(handler);
    }

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public BlueToothObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
        Log.e("TAG", "FlashlightObserver: " );
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.e("TAG", "FlashlightObserver onChange" );
        try {
            int bluetoothOn = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                bluetoothOn = Settings.System.getInt(mContext.getContentResolver(), Settings.Global.BLUETOOTH_ON);
            }
            mHandler.obtainMessage(MSG_BLUETOOTH_ON,bluetoothOn).sendToTarget();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}
