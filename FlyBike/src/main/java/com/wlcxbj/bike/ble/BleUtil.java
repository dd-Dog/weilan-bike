package com.wlcxbj.bike.ble;

/**
 * Created by bain on 17-2-6.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.wlcxbj.bike.ble.encrypt.AES;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by shaolin on 6/17/16.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleUtil {

    private static final String TAG = "BleUtil";
    private static final long SCAN_PERIOD = 1000 * 5;
    private static final String SERVICE_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
    //0000fff1-0000-1000-8000-00805f9b34fb
    public static final String characterUUID1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID3 = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID4 = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID5 = "0000fff5-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID6 = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID7 = "0000fff7-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID8 = "0000fff8-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID9 = "0000fff9-0000-1000-8000-00805f9b34fb";
    public static final String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";

    public static byte[] workModel = {0x02, 0x01};

    private Activity mContext;
    private static BleUtil mInstance;

    //作为中央来使用和处理数据；
    private BluetoothGatt mGatt;

    private BTUtilCallBackAdapter btUtilCallBackAdapter;
    private BluetoothDevice mCurDevice;
    private BluetoothAdapter mBtAdapter;
    private List<BleDeviceBean> listDevice;

    private BluetoothGattService service;
    private BluetoothGattCharacteristic character1;
    private BluetoothGattCharacteristic character2;
    private BluetoothGattCharacteristic character3;
    private BluetoothGattCharacteristic character4;
    private BluetoothGattCharacteristic character5;
    public BluetoothGattCharacteristic character6;
    private BluetoothGattCharacteristic character7;
    private BluetoothGattCharacteristic character8;
    public BluetoothGattCharacteristic character9;

    public BluetoothGattCharacteristic character_key_data;
    private BluetoothManager bluetoothManager;
    private BluetoothDevice remoteDevice;
    private boolean connectByMac = false;
    private BluetoothDevice connectedDevice;

    public static synchronized BleUtil getInstance() {
        if (mInstance == null) {
            mInstance = new BleUtil();
        }
        return mInstance;
    }


    public void setContext(Activity context) {
        mContext = context;
        init();
    }

    private void init() {
        listDevice = new ArrayList<>();
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showToast("BLE不支持此设备!");
            ((Activity) mContext).finish();
        }
        bluetoothManager = (BluetoothManager) mContext.getSystemService(Context
                .BLUETOOTH_SERVICE);
        //注：这里通过getSystemService获取BluetoothManager，
        //再通过BluetoothManager获取BluetoothAdapter。BluetoothManager在Android4.3以上支持(API level 18)。
        if (bluetoothManager != null) {
            mBtAdapter = bluetoothManager.getAdapter();
        }
        if (mBtAdapter == null || !mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }
    }

    public boolean isBlueToothEanbled() {
        if (mBtAdapter == null) return false;
        return mBtAdapter.isEnabled();
    }

    //扫描设备的回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     final byte[] scanRecord) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            if (Texdevice.getName())
                            BleDeviceBean bleDeviceBean = new BleDeviceBean(device, rssi,
                                    scanRecord);
                            if (!listDevice.contains(bleDeviceBean)) {
                                //不重复添加
                                listDevice.add(bleDeviceBean);
                                btUtilCallBackAdapter.onLeScanDevices(listDevice);
//                                Log.e(TAG, "device:" + bleDeviceBean.getDevice().getAddress()
// .toString());
                            }
                        }
                    });
                }
            };

    //扫描设备
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScan();
                    Log.e(TAG, "run: stop");
                }
            }, SCAN_PERIOD);
            startScan();
            Log.e(TAG, "start");
        } else {
            stopScan();
            Log.e(TAG, "stop");
        }
    }

    //开始扫描BLE设备
    private void startScan() {
        mBtAdapter.startLeScan(mLeScanCallback);
        btUtilCallBackAdapter.onLeScanStart();
    }

    //停止扫描BLE设备
    private void stopScan() {
        mBtAdapter.stopLeScan(mLeScanCallback);
        btUtilCallBackAdapter.onLeScanStop();
    }

    //返回中央的状态和周边提供的数据
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            Log.e(TAG, "onConnectionStateChange,status=" + status + ",newState=" + newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.e(TAG, "STATE_CONNECTED");
                    btUtilCallBackAdapter.onConnected(connectByMac ? remoteDevice : mCurDevice);
//                    Log.e(TAG, "STATE_CONNECTED, connectedDevice=" + connectedDevice.getAddress
// ());
                    if (connectedDevice != null) {
                        Log.e(TAG, "connectedDevice！＝null,connectedAddress=" + connectedDevice.getAddress()
                        + "gatt.GetDevice().address=" + gatt.getDevice().getAddress());
                        if (!TextUtils.equals(gatt.getDevice().getAddress(), connectedDevice
                                .getAddress())) {
                            Log.e(TAG, "STATE_CONNECTED，开始搜索服务");
                            gatt.discoverServices(); //搜索连接设备所支持的service
                        }else {
                            Log.e(TAG, "STATE_CONNECTED，是重复连接回调，不执行扫描服务");
                        }
                    } else {
                        Log.e(TAG, "connectedDevice==null,STATE_CONNECTED，开始搜索服务");
                        gatt.discoverServices(); //搜索连接设备所支持的service
                        connectedDevice = gatt.getDevice();
                    }
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    btUtilCallBackAdapter.onDisConnected(connectByMac ? remoteDevice : mCurDevice);
//                    disConnGatt();
                    connectedDevice = null;
                    Log.e(TAG, "STATE_DISCONNECTED");
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    btUtilCallBackAdapter.onConnecting(connectByMac ? remoteDevice : mCurDevice);
                    Log.e(TAG, "STATE_CONNECTING");
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    btUtilCallBackAdapter.onDisConnecting(connectByMac ? remoteDevice : mCurDevice);
                    Log.e(TAG, "STATE_DISCONNECTING");
                    if (gatt != null)
                        gatt.close();//关闭gatt客户端
                    break;
            }
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService mService = mGatt.getService(UUID.fromString(SERVICE_UUID));
                List<BluetoothGattCharacteristic> characterList = mService.getCharacteristics();
                for (int j = 0; j < characterList.size(); j++) {
                    String uuid = characterList.get(j).getUuid().toString();
                    Log.e(TAG, "---CharacterName:" + uuid);
                    if (TextUtils.equals(uuid, characterUUID1)) {
                        character1 = characterList.get(j);
                    } else if (TextUtils.equals(uuid, characterUUID2)) {
                        character2 = characterList.get(j);
                    } else if (TextUtils.equals(uuid, characterUUID3)) {
                        character3 = characterList.get(j);
                    } else if (TextUtils.equals(uuid, characterUUID4)) {
                        character4 = characterList.get(j);
                    } else if (TextUtils.equals(uuid, characterUUID5)) {
                        character5 = characterList.get(j);
                    } else if (TextUtils.equals(uuid, characterUUID6)) {
                        character6 = characterList.get(j);
                    } else if (TextUtils.equals(uuid, characterUUID7)) {
                        character7 = characterList.get(j);
                        setNotification();
                    } else if (TextUtils.equals(uuid, characterUUID8)) {
                        character8 = characterList.get(j);
                    } else if (TextUtils.equals(uuid, characterUUID9)) {
                        character9 = characterList.get(j);
                    } else if (TextUtils.equals(uuid, UUID_KEY_DATA)) {
                        character_key_data = characterList.get(j);
                    }
                }
                btUtilCallBackAdapter.onServiceDiscover(gatt);
            }

            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            Log.e(TAG, "onCharacteristicRead");
            super.onCharacteristicRead(gatt, characteristic, status);
            btUtilCallBackAdapter.onCharRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            Log.e(TAG, "onCharacteristicWrite");
            super.onCharacteristicWrite(gatt, characteristic, status);
            btUtilCallBackAdapter.onCharWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic) {
            Log.e(TAG, "onCharacteristicChanged");
//            这里是可以监听到设备自身或者手机改变设备的一些数据修改h通知
//            receiveData(characteristic);
            super.onCharacteristicChanged(gatt, characteristic);
            if (btUtilCallBackAdapter != null) {
                btUtilCallBackAdapter.onCharateristicChanged(gatt, characteristic);
            }
        }

    };

    //获取设备指定的特征中的特性,其中对其进行监听, setCharacteristicNotification与上面的回调onCharacteristicChanged进行一一搭配
    private void setNotification() {
        mGatt.setCharacteristicNotification(character7, true);

        String descriptorUUID = "00002902-0000-1000-8000-00805f9b34fb";
        BluetoothGattDescriptor descriptor = character7.getDescriptor(UUID.fromString
                (descriptorUUID));
        if (descriptor == null) {
            Log.e(TAG, "descriptor == null");
            return;
        }
        Log.e(TAG, "descriptor != null");
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mGatt.writeDescriptor(descriptor);
    }

    //接收数据,对其进行处理
    private void receiveData(BluetoothGattCharacteristic ch) {
        byte[] bytes = ch.getValue();
        int cmd = bytes[0];
        int agree = bytes[1];
        switch (cmd) {
            case 1:
                btUtilCallBackAdapter.onStrength(agree);
                Log.e(TAG, "手机通知BLE设备强度:" + agree);
                break;
            case 2:
                btUtilCallBackAdapter.onModel(agree);
                Log.e(TAG, "工作模式:" + agree);
                break;
            case 3:
                btUtilCallBackAdapter.onStrength(agree);
                Log.e(TAG, "设备自身通知改变强度:" + agree);
                break;
        }
    }

    /**
     * 通过mac地址直接连接，如果使用该方法，不用先调用扫描
     *
     * @param mac
     */
    private static final String MAC_FORMAT =
            "[0-9A-F]{2}:[0-9A-F]{2}:[0-9A-F]{2}:[0-9A-F]{2}:[0-9A-F]{2}:[0-9A-F]{2}";

    public void connectByMac(String mac) {
        if (mBtAdapter == null) return;
        if (!mac.matches(MAC_FORMAT)) {
            ToastUtil.showUIThread(mContext, "蓝牙地址格式错误！");
            return;
        }
        remoteDevice = mBtAdapter.getRemoteDevice(mac);
        mGatt = remoteDevice.connectGatt(mContext, true, mGattCallback);
        connectByMac = true;
    }

    //连接设备
    public void connectLeDevice(int devicePos) {
        mBtAdapter.stopLeScan(mLeScanCallback);
        mCurDevice = listDevice.get(devicePos).getDevice();
        checkConnGatt();
        connectByMac = false;
    }

    //连接设备
    public void connectLeDevice(BluetoothDevice device) {
//        mBtAdapter.stopLeScan(mLeScanCallback);
        mCurDevice = device;
        checkConnGatt();
        connectByMac = false;
    }

    //发送进入工作模式请求
    public void sendWorkModel() {
        if (character1 != null) {
            character1.setValue(workModel);
            mGatt.writeCharacteristic(character1);
        }
    }

    //发送强度
    public void sendStrength(int strength) {
        byte[] strengthModel = {0x01, (byte) strength};
        if (character1 != null) {
            character1.setValue(strengthModel);
            mGatt.writeCharacteristic(character1);
        }
    }

    //    //检查设备是否连接了
//    private void checkConnGatt() {
//        if (mGatt == null) {
//            //首次连接
//            Log.e(TAG, "mGatt == null,建立新连接");
//            mGatt = mCurDevice.connectGatt(mContext, false, mGattCallback);
//            btUtilCallBackAdapter.onConnecting(mCurDevice);
//        } else {
//            Log.e(TAG, "mGatt ！= null,重新连接");
//            //再次连接
//            mGatt.connect();
//        }
//    }
//检查设备是否连接了
    private void checkConnGatt() {
        if (mGatt == null) {
            //首次连接
            Log.e(TAG, "mGatt == null,建立新连接");
            mGatt = mCurDevice.connectGatt(mContext, false, mGattCallback);
            btUtilCallBackAdapter.onConnecting(mCurDevice);
        } else {
            Log.e(TAG, "mGatt != null,建立新连接");
            mGatt.disconnect();
            mGatt.close();
            mGatt = mCurDevice.connectGatt(mContext, false, mGattCallback);
        }
    }

    //  断开设备连接
    public void disConnGatt() {
        if (mGatt != null) {
            connectedDevice = null;
            mGatt.disconnect();
            mGatt.close();//Close this Bluetooth GATT client.
            //只断开连接，不把对象置空，以方便下次连接，不必要再初始化
            mGatt = null;
            Log.e(TAG, "主动断开连接");
        }
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 向BLE终端写数据
     *
     * @param value
     * @return
     */
    public boolean writeChar(byte[] value) {
        boolean b = false;
        Log.e(TAG, "发送数据包：" + AES.byte2hex(value));
        if (character8 != null && mGatt != null) {
            Log.e(TAG, "character8=" + character8.getUuid());
            int writeType = character8.getWriteType();
            character8.setValue(value);
            b = mGatt.writeCharacteristic(character8);
            Log.e(TAG, "写操作初始化" + (b ? "成功" : "失败") + ",writeType=" + writeType);
            return b;
        }
        return b;
    }

    /**
     * 从BLE终端读取车锁状态
     */
    public boolean readLockState() {
        boolean b = false;
        if (character9 != null) {
            Log.e(TAG, "gattCharacteristic=" + character9);
            LogUtil.e(TAG, "mGatt=" + mGatt + ", mBtAdapter=" + mBtAdapter);
            if (mGatt == null || mBtAdapter == null) {
                Log.e(TAG, "BluetoothAdapter not initialized!");
                return b;
            }
            b = mGatt.readCharacteristic(character9);
            Log.e(TAG, "读操作初始化" + (b ? "成功" : "失败"));
        }
        return b;
    }

    /**
     * 从BLE终端读取开锁次数
     */
    public boolean readUnlockCount() {
        boolean b = false;
        if (character5 != null) {
            Log.e(TAG, "gattCharacteristic=" + character5);
            if (mGatt == null || mBtAdapter == null) {
                Log.e(TAG, "BluetoothAdapter not initialized!");
                return b;
            }
            b = mGatt.readCharacteristic(character5);
            Log.e(TAG, "读操作初始化" + (b ? "成功" : "失败"));
        }
        return b;
    }

    public void setBTUtilListener(BTUtilCallBackAdapter btUtilCallBackAdapter) {
        this.btUtilCallBackAdapter = btUtilCallBackAdapter;
    }


    public int getConnectState() {
        int connectionState = bluetoothManager.getConnectionState(mCurDevice, BluetoothProfile
                .GATT_SERVER);
        return connectionState;
    }

    public interface BTUtilListener {
        void onLeScanStart(); // 扫描开始

        void onLeScanStop();  // 扫描停止

        void onLeScanDevices(List<BleDeviceBean> listDevice); //扫描得到的设备

        void onConnected(BluetoothDevice mCurDevice); //设备的连接

        void onDisConnected(BluetoothDevice mCurDevice); //设备断开连接

        void onConnecting(BluetoothDevice mCurDevice); //设备连接中

        void onDisConnecting(BluetoothDevice mCurDevice); //设备连接失败

        void onStrength(int strength); //给设备设置强度

        void onModel(int model); //设备模式

        void onServiceDiscover(BluetoothGatt gatt);

        void onCharRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

        void onCharWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int
                status);

        void onCharateristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic);
    }
}