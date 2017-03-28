package com.wlcxbj.bike.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.util.ClsUtils;
import com.wlcxbj.bike.util.PreferenceUtil;


/**
 * 两个应用有相同的receiver会产生冲突
 */
public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = "BluetoothReceiver";
    String pin = "1234";  //此处为你要连接的蓝牙设备的初始密钥，一般为1234或0000
    private static final String KEY_STRING = "SCL-CL00";
    private BluetoothDevice device;
    BluetoothDevice btDevice = null;  //创建一个蓝牙device对象

    public BluetoothReceiver() {

    }

    //广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction(); //得到action
        Log.e(TAG, "action1=" + action);
        // 从Intent中获取设备对象
        btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {  //发现设备
            Log.e(TAG, "发现设备:" + "[" + btDevice.getName() + "]" + ":" + btDevice.getAddress());

            if (btDevice.getName().contains(KEY_STRING))//HC-05设备如果有多个，第一个搜到的那个会被尝试。
            {
                if (btDevice.getBondState() == BluetoothDevice.BOND_NONE) {

                    Log.e(TAG, "ywq" + "attemp to bond:" + "[" + btDevice.getName() + "]");
                    try {
                        //通过工具类ClsUtils,调用createBond方法
                        boolean bond = ClsUtils.createBond(btDevice.getClass(), btDevice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e(TAG, "error" + "Is faild");
            }
        } else if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            //再次得到的action，会等于PAIRING_REQUEST
            Log.e(TAG, "action2=" + action);
            int pairing = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice
                    .BOND_NONE); //当前的配对的状态
            Log.e(TAG, "state=" + pairing);
            if (btDevice.getName().contains(KEY_STRING)) {
                try {
                    //1.确认配对
                    ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);
                    //2.终止有序广播
                    Log.e(TAG, "order..." + "isOrderedBroadcast:" + isOrderedBroadcast() + "," +
                            "isInitialStickyBroadcast:" + isInitialStickyBroadcast());
                    abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //3.调用setPin方法进行配对...
                    boolean ret = ClsUtils.setPin(btDevice.getClass(), btDevice, pin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
            //监听蓝牙开关状态
            Log.e("action3", action);
            int enable = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter
                    .STATE_OFF);
            if (enable == BluetoothAdapter.STATE_ON) {
                Log.e("action3", "蓝牙状态改变，已经开启");
                BluetoothAdapter.getDefaultAdapter().startDiscovery();
            }
        } else if (action.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
            //监听蓝牙配对状态
            Log.e("action4", "蓝牙配对状态改变");
            int bonded = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice
                    .BOND_NONE); //当前的配对的状态
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //配对的设备信息
            if (bonded == BluetoothDevice.BOND_BONDED) {
                Log.e("action4", "蓝牙配对成功,配对设备信息：" + device.getName() + ", " + device
                        .getAddress());
                //配对成功后就能发信息了
                PreferenceUtil.putBoolean(context, Constants.BLUETOOTH_PAIR_SUCCESS, true);
            } else {
                PreferenceUtil.putBoolean(context, Constants.BLUETOOTH_PAIR_SUCCESS, false);
                Toast.makeText(context, "与车锁连接失败，您需要手动输入密码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "提示信息" + "这个设备不是目标蓝牙设备");
        }
    }
}