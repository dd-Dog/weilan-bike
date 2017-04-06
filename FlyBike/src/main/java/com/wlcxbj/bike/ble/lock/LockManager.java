package com.wlcxbj.bike.ble.lock;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.wlcxbj.bike.ble.BleClientManager;
import com.wlcxbj.bike.ble.encrypt.AES;
import com.wlcxbj.bike.ble.packet.cmd.CmdFactory;
import com.wlcxbj.bike.ble.packet.cmd.Command;
import com.wlcxbj.bike.ble.packet.cmd.UnlockCmd;
import com.wlcxbj.bike.ble.packet.part.CmdBody;
import com.wlcxbj.bike.ble.packet.part.CmdData;
import com.wlcxbj.bike.ble.packet.util.Util;
import com.wlcxbj.bike.global.BLEError;

import java.util.ArrayList;
import java.util.UUID;


import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Created by Administrator on 2017/3/28.
 */

public class LockManager {
    private static final int MSG_READ_LOCK_STATE = 10;
    private static final long DELAYED_READ = 1000;
    private BluetoothClient mClient;
    public static final String characterUUID1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID3 = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID4 = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID5 = "0000fff5-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID6 = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID7 = "0000fff7-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID8 = "0000fff8-0000-1000-8000-00805f9b34fb";
    public static final String characterUUID9 = "0000fff9-0000-1000-8000-00805f9b34fb";
    private static final String SERVICE_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String TAG = "LockManager";
    private ArrayList<byte[]> receiveBytes = new ArrayList<>();
    private ArrayList<SearchResult> beacons = new ArrayList<>();
    private LockCallbackHandler lockCallbackHandler;
    private LockHandler lockHandler;
    private Context mContext;
    private ScanLeDeviceThread mScanLeDeviceThread;
    private boolean enableScanner = true;
    private static long SCAN_FREQUENCY = 1000 * 10;

    public LockManager(Context context) {
        mContext = context;
        mClient = BleClientManager.getClient(context);
        initBle();
    }

    private void initBle() {
        searchLe();
        lockHandler = new LockHandler();
        if (mClient.isBluetoothOpened()) {
            startScanLe();
        }
    }

    public void scanLe() {
        if (mClient == null) return;
        searchLe();
    }

    private class LockHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_READ_LOCK_STATE:
                    SearchResult device = (SearchResult) msg.obj;
                    readLockState(device, msg.arg1);
                    break;
            }
        }
    }

    /**
     * 设置通知
     *
     * @param device
     */
    private void setNotify(final SearchResult device, final byte cmdId) {
        if (mClient == null) return;
        mClient.notify(device.getAddress(), UUID.fromString(SERVICE_UUID), UUID.fromString
                (characterUUID7), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                Log.e(TAG, "notify value=" + AES.byte2hex(value));
                if (receiveBytes != null) {
                    //避免收到重复数据包
                    if (receiveBytes.size() > 0) {
                        //判断是否是重复数据包，不重复则接收
                        if (!(value[1] == receiveBytes.get(receiveBytes.size() - 1)[1])) {
                            receiveBytes.add(value);
                            if (value[0] == receiveBytes.size()) {
                                Log.e(TAG, "读取完成");
                                handleCmdRes(device, cmdId);
                            }
                        }
                    } else {
                        receiveBytes.add(value);
                        Log.e(TAG, "接收数据,receiveByteArr.size=" + receiveBytes.size());
                        if (value[0] == receiveBytes.size()) {
                            Log.e(TAG, "读取完成");
                            handleCmdRes(device, cmdId);
                        }
                    }

                }
            }

            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {

                } else {
                    if (lockCallbackHandler != null) {
                        lockCallbackHandler.onFail(device, code, "设置通知时失败", cmdId);
                        if (mClient != null) {
                            mClient.disconnect(device.getAddress());
                        }
                    }
                }
            }
        });
    }

    /**
     * 关闭蓝牙
     *
     * @return
     */
    public boolean closeBluetooth() {
        boolean b = false;
        if (mClient != null) {
            b = mClient.closeBluetooth();
        }
        return b;
    }

    /**
     * 打开蓝牙
     *
     * @return
     */
    public boolean openBluetooth() {
        boolean b = false;
        if (mClient != null) {
            b = mClient.openBluetooth();
        }
        return b;
    }

    /**
     * 蓝牙是否是打开状态
     *
     * @return
     */
    public boolean isBluetoothOpened() {
        boolean b = false;
        if (mClient != null) {
            b = mClient.isBluetoothOpened();
        }
        return b;
    }

    /**
     * 是否支持BLE
     *
     * @return
     */
    public boolean isBleSupported() {
        if (mClient != null) {
            return mClient.isBleSupported();
        } else {
            return false;
        }
    }

    /**
     * 处理回复
     */
    public void handleCmdRes(SearchResult device, int cmdId) {
        //接收完成
        Command cmd = CmdFactory.parseByteToCmd(receiveBytes);
        receiveBytes.clear();
        CmdData cmdData = cmd.getCmdData();
        CmdBody cmdBody = cmd.getCmdBody();
        final byte code = cmdData.getData()[0];
        Log.e(TAG, "CMDDATA=" + code + ",cmd_id=" + cmdBody.getCmd_id());
        switch (cmdId) {
            case Command.CMD_ID_LOCATE:
                break;
            case Command.CMD_ID_UNLOCK:
                if (code != 0) {
                    if (lockCallbackHandler != null) {
                        lockCallbackHandler.onFail(device, code, "接收回复，开锁失败", (byte) cmdId);
                        if (mClient != null) {
                            mClient.disconnect(device.getAddress());
                        }
                    }
                } else {
                    if (lockHandler != null) {
                        Message message = lockHandler.obtainMessage();
                        message.obj = device;
                        message.arg1 = cmdId;
                        message.what = MSG_READ_LOCK_STATE;
                        lockHandler.sendMessageDelayed(message, DELAYED_READ);
                    }
                }
                break;
            case Command.CMD_ID_SET_SERVER:
                if (lockCallbackHandler != null) {
                    lockCallbackHandler.onSuccess(device, (byte) cmdId);
                    if (mClient != null) {
                        mClient.disconnect(device.getAddress());
                    }
                }
                break;
        }
    }

    /**
     * 读锁状态
     *
     * @param device
     */
    private void readLockState(final SearchResult device, final int cmdId) {
        if (mClient == null) return;
        mClient.read(device.getAddress(), UUID.fromString(SERVICE_UUID), UUID.fromString
                (characterUUID9), new BleReadResponse() {
            @Override
            public void onResponse(int code, byte[] data) {
                byte state = data[0];
                if (state == BLEError.STATE_UNLOCK) {
                    if (lockCallbackHandler != null) {
                        lockCallbackHandler.onSuccess(device, (byte) cmdId);
                        if (mClient != null) {
                            mClient.disconnect(device.getAddress());
                        }
                    }
                } else {
                    if (lockCallbackHandler != null) {
                        lockCallbackHandler.onFail(device, state, "读锁状态：未开锁", (byte) cmdId);
                        if (mClient != null) {
                            mClient.disconnect(device.getAddress());
                        }
                    }
                }
            }
        });
    }

    /**
     * 扫描设备
     */
    private void searchLe() {
        //扫描前清除一次
//        beacons.clear();
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 1)   // 先扫BLE设备3次，每次3s
//                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .build();
        if (mClient == null) return;
        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Beacon beacon = new Beacon(device.scanRecord);
                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon
                        .toString()));
                if (TextUtils.equals(device.getName(), "iLock")) {
                    for (int i=0; i<beacons.size(); i++) {
                        if (TextUtils.equals(beacons.get(i).getAddress(), device.getAddress())) {
                            continue;
                        }
                    }
                    beacons.add(device);
                }
            }

            @Override
            public void onSearchStopped() {

            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }

    /**
     * 连接状态回调
     */
    private BleConnectStatusListener mBleConnectStatusListener = new
            BleConnectStatusListener() {

                @Override
                public void onConnectStatusChanged(String mac, int status) {
                    Log.e(TAG, "连接状态，status=" + status);
                    if (status == STATUS_CONNECTED) {

                    } else if (status == STATUS_DISCONNECTED) {

                    }
                }
            };


    /**
     * 开锁方法
     */
    public void unlock(String mac, byte[] psw) {
        if (psw == null) return;
        UnlockCmd unlockCmd = CmdFactory.createUnlockCmd(psw);
        ArrayList<byte[]> unlockBytes = Util.splitToByteArr(unlockCmd.encrypt());
        boolean find = false;
        for (SearchResult device : beacons) {
            if (TextUtils.equals(device.getAddress(), mac)) {
                connectBleDevice(device, unlockBytes, Command.CMD_ID_UNLOCK);
                find = true;
                break;
            }
        }
        if (!find) {
            if (lockCallbackHandler != null) {
                lockCallbackHandler.onFail(null, -1, "没有搜索到蓝牙设备", Command.CMD_ID_UNLOCK);
            }
        }
    }


    private void connectBleDevice(final SearchResult device, final ArrayList<byte[]> unlockBytes,
                                  final byte cmdId) {
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(10 * 1000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(10 * 1000)  // 发现服务超时20s
                .build();

        mClient.connect(device.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                Log.e(TAG, "CODE=" + code);
                if (code == REQUEST_SUCCESS) {
                    //连接成功
                    Log.e(TAG, "连接成功");
                    setNotify(device, cmdId);
                    sendCmd(device, 0, unlockBytes);
                } else {
                    if (lockCallbackHandler != null) {
                        lockCallbackHandler.onFail(device, code, "连接失败", cmdId);
                        if (mClient != null) {
                            mClient.disconnect(device.getAddress());
                        }
                    }
                }
            }
        });
        mClient.registerConnectStatusListener(device.getAddress(), mBleConnectStatusListener);
    }

    /**
     * 发送指令
     *
     * @param device
     * @param unlockBytes
     */

    private void sendCmd(final SearchResult device, final int index, final ArrayList<byte[]>
            unlockBytes) {
        mClient.write(device.getAddress(), UUID.fromString(SERVICE_UUID), UUID.fromString
                (characterUUID8), unlockBytes.get(index), new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {
                    Log.e(TAG, "写成功, index=" + index);
                    if (index + 1 >= unlockBytes.size()) {
                        return;
                    } else {
                        sendCmd(device, index + 1, unlockBytes);
                    }
                }
            }
        });
    }

    /**
     * 停止扫描LE
     */
    public void stopScanLe() {
        enableScanner = false;
    }

    /**
     * 开始扫描LE
     */
    public void startScanLe() {
        enableScanner = true;
        if (mScanLeDeviceThread == null) {
            mScanLeDeviceThread = new ScanLeDeviceThread();
        }
        mScanLeDeviceThread.start();
    }

    private class ScanLeDeviceThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (enableScanner) {
                scanLe();
                try {
                    Thread.sleep(SCAN_FREQUENCY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void setLockCallbackHandler(LockCallbackHandler lockCallbackHandler) {
        this.lockCallbackHandler = lockCallbackHandler;
    }

    public void destroy() {
        stopScanLe();
        mClient.stopSearch();
    }
}
