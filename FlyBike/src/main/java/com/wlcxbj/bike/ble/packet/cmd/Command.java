package com.wlcxbj.bike.ble.packet.cmd;

import android.util.Log;

import com.wlcxbj.bike.ble.encrypt.AES;
import com.wlcxbj.bike.ble.encrypt.XOR;
import com.wlcxbj.bike.ble.packet.part.CmdBody;
import com.wlcxbj.bike.ble.packet.part.CmdData;
import com.wlcxbj.bike.ble.packet.part.CmdHead;
import com.wlcxbj.bike.ble.packet.part.CmdTail;
import com.wlcxbj.bike.ble.packet.util.Util;

import java.util.Arrays;


/**
 * Created by Administrator on 2017/3/4.
 */
public abstract class Command {
    protected CmdHead cmdHead;
    protected CmdBody cmdBody;
    protected CmdData cmdData;
    protected CmdTail cmdTail;
    private static final String TAG = "Command";

    public Command(byte[] cmd) {
        setCmdData(cmd);
        setCmdHead();
        setCmdBody();
        setCmdTail();
        setCheckSum();
    }

    public Command() {
    }

    /**
     * 用户命令类型
     */
    public static final byte CMD_TYPE_USER = 0x01;
    /**
     * 生产命令类型，超级管理命令
     */
    public static final byte CMD_TYPE_ADMIN = (byte) 0xFF;
    /**
     * 用户类型——普通用户
     */
    public static final byte USER_TYPE_NORMAL = 0x01;
    /**
     * 用户类型－管理员
     */
    public static final byte USER_TYPE_ADMIN = (byte) 0xFF;

    /**
     * 加密类型－SQRT98
     */
    public static final byte ENRYPT_TYPE_SQRT98 = 0x01;
    /**
     * 加密类型－AES
     */
    public static final byte ENRYPT_TYPE_AES = 0x02;
    /**
     * 加密类型－RSA
     */
    public static final byte ENRYPT_TYPE_XOR = 0x03;

    /**
     * 命令传输类型－从设备到主设备
     */
    public static final byte TRANS_TYPE_UP = 0x01;
    /**
     * 命令传输类型－主设备到从设备
     */
    public static final byte TRANS_TYPE_DOWN = 0x02;
    /**
     * 寻车命令
     */
    public static final byte CMD_ID_LOCATE = 0X01;

    /**
     * 开锁命令ID
     */
    public static final byte CMD_ID_UNLOCK = 0X02;
    /**
     * 查询车锁状态命令ID
     */
    protected static final byte CMD_ID_CHECK_STATE = 0X03;

    /**
     * 车锁状态——上锁
     */
    public static final int LOCK = 0X01;
    /**
     * 车锁状态——开锁
     */
    public static final int UNLOCK = 0X02;

    protected void setCmdHead() {
        Log.e(TAG, "设置CmdHead");
        byte[] randomData = Util.createRandomData();
        byte[] cmd_data_len = new byte[2];
        //后继数据域的长度
        int dataLen = cmdData.getBytes().length;
        cmd_data_len[0] = (byte) (dataLen % 256);
        cmd_data_len[1] = (byte) (dataLen / 256);
        Log.e(TAG, "CmdHead中数据域长度字段：" + Arrays.toString(cmd_data_len));
        this.cmdHead = new CmdHead(ENRYPT_TYPE_AES, cmd_data_len, randomData, new byte[]{0, 0});
    }

    protected abstract void setCmdBody();

    protected void setCmdData(byte[] cmd){
        this.cmdData = new CmdData(cmd);
        Log.e(TAG, "设置CmdData，cmdData＝" + Arrays.toString(cmdData.getData()));
    }

    protected void setCmdTail(){
        this.cmdTail = new CmdTail();
    }

    protected void setCheckSum(){
        if (cmdTail == null) {
            return;
        }
        cmdTail.setCheck_sum(getCheckSum());
    }

    /**
     * 获取累加校验和
     *
     * @return
     */
    public byte[] getCheckSum() {
        if (cmdHead == null || cmdBody == null || cmdData == null || cmdTail == null) {
            return null;
        }
        byte[] headBytes = cmdHead.getBytes();
        byte[] bodyBytes = cmdBody.getBytes();
        byte[] dataBytes = cmdData.getBytes();
        byte[] tailBytes = cmdTail.getCmd_tail();
        int sum = 0;
        for (int i = 0; i < headBytes.length; i++) {
            sum += Util.getPositiveValue(headBytes[i]);
        }
        Log.e("checksum", "head:" + AES.byte2hex(headBytes));
        for (int i = 0; i < bodyBytes.length; i++) {
            sum += Util.getPositiveValue(bodyBytes[i]);
        }
        Log.e("checksum", "body:" + AES.byte2hex(bodyBytes));
        for (int i = 0; i < dataBytes.length; i++) {
            sum += Util.getPositiveValue(dataBytes[i]);
        }
        Log.e("checksum", "data:" + AES.byte2hex(dataBytes));
        for (int i = 0; i < tailBytes.length; i++) {
            sum += Util.getPositiveValue(tailBytes[i]);
        }
        Log.e("checksum", "tailBytes:" + AES.byte2hex(tailBytes));
        Log.e(TAG, "计算校验和sum＝" + sum);
        //取低两个字节
        byte[] check_sum = new byte[2];
        check_sum[0] = (byte) (sum % 256);
        check_sum[1] = (byte) (sum / 256);
        return check_sum;
    }

    public byte[] encrypt() {
        /**
         * cmd_head中的random_data为明文，跟随整个cmd_head做和根号98异或加密；
         （b）cmd_body、cmd_data、cmd_tail的AES加解密：
         加密过程：
         ①	将random_data拼接在cmd_body、cmd_data、cmd_tail之前；
         ②	对拼接后的整体做AES加密；
         ③	对AES加密之后的内容，整体与random_data 做循环异或加密；
         解密过程：
         ①	从cmd_head中取出random_data；
         ②	对加过密的数据块，用random_data 循环异或解密；
         ③	再对数据块做AES解密；
         ④	取出所需要的cmd_body、cmd_data、cmd_tail。

         */
        byte[] encryptedHeadBytes = cmdHead.getEncryptedBytes(Command.ENRYPT_TYPE_SQRT98);
        byte[] plainBodyBYtes = cmdBody.getBytes();
        byte[] plainDataBytes = cmdData.getBytes();
        byte[] plainTailBytes = cmdTail.getBytes();
        byte[] plainBodyDataTailRandomBytes = new byte[cmdHead.getRandom_data().length +
                plainBodyBYtes.length + plainDataBytes.length +
                plainTailBytes.length];
        //放到同一个数组中
        System.arraycopy(cmdHead.getRandom_data(), 0,
                plainBodyDataTailRandomBytes, 0,
                cmdHead.getRandom_data().length);
        System.arraycopy(plainBodyBYtes, 0,
                plainBodyDataTailRandomBytes, cmdHead.getRandom_data().length,
                plainBodyBYtes.length);
        System.arraycopy(plainDataBytes, 0,
                plainBodyDataTailRandomBytes,
                cmdHead.getRandom_data().length + plainBodyBYtes.length,
                plainDataBytes.length);
        System.arraycopy(plainTailBytes, 0, plainBodyDataTailRandomBytes,
                cmdHead.getRandom_data().length + plainBodyBYtes.length + plainDataBytes.length,
                plainTailBytes.length);
        //AES加密后，再循环异或加密
        Log.e(TAG, "plainBodyDataTailRandomBytes=" + AES.byte2hex(plainBodyDataTailRandomBytes));
        byte[] aesEncryptedBodyDataTailBytes = AES.encrypt(plainBodyDataTailRandomBytes, AES
                .getKey());
        Log.e(TAG, "aesEncryptedBodyDataTailBytes=" + AES.byte2hex(aesEncryptedBodyDataTailBytes));
        byte[] xorAESEncryptedBodyDataTailBytes = XOR.encryptWithRandom
                (aesEncryptedBodyDataTailBytes, cmdHead.getRandom_data());
//        byte[] xorAESEncryptedBodyDataTailBytes = aesEncryptedBodyDataTailBytes;

        //SQRT98加密后的头部与AES加密后的其它进行拼接
        byte[] encryptedCommandBytes =
                new byte[encryptedHeadBytes.length + xorAESEncryptedBodyDataTailBytes.length];
        System.arraycopy(encryptedHeadBytes, 0,
                encryptedCommandBytes, 0, encryptedHeadBytes.length);
        System.arraycopy(xorAESEncryptedBodyDataTailBytes, 0,
                encryptedCommandBytes, encryptedHeadBytes.length,
                xorAESEncryptedBodyDataTailBytes.length);
        return encryptedCommandBytes;
    }

    /**
     * 解析数据后的数据完整性
     *
     * @return
     */
    public boolean checkInteger() {
        if (cmdTail == null) {
            return false;
        }
        byte[] check_sum_remote = cmdTail.getCheck_sum();
        byte[] check_sum_native = getCheckSum();
        Log.e(TAG, "check_sum_remote=" + AES.byte2hex(check_sum_remote));
        Log.e(TAG, "check_sum_native=" + AES.byte2hex(check_sum_native));
        if (check_sum_native[0] == check_sum_remote[0] && check_sum_native[1] ==
                check_sum_remote[1]) {
            return true;
        }
        return false;
    }

    /**
     * 获取字节数组
     * @return
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[CmdHead.LENGTH + CmdBody.LENGTH + cmdData.getBytes().length +
                CmdTail.LENGTH];
        System.arraycopy(cmdHead.getBytes(), 0, bytes, 0, CmdHead.LENGTH);
        System.arraycopy(cmdBody.getBytes(), 0, bytes, CmdHead.LENGTH, CmdBody.LENGTH);
        System.arraycopy(cmdData.getBytes(), 0, bytes, CmdHead.LENGTH + CmdBody.LENGTH, cmdData
                .getBytes().length);
        System.arraycopy(cmdTail.getBytes(), 0, bytes,
                CmdHead.LENGTH + CmdBody.LENGTH + cmdData.getBytes().length, CmdTail.LENGTH);
//        Log.e(TAG, "COMMAND BYTES=" + Arrays.toString(bytes));
        Log.e(TAG, "COMMAND BYTES=" + AES.byte2hex(bytes));
        return bytes;
    }

    public CmdHead getCmdHead() {
        return cmdHead;
    }

    public CmdBody getCmdBody() {
        return cmdBody;
    }

    public CmdData getCmdData() {
        return cmdData;
    }

    public CmdTail getCmdTail() {
        return cmdTail;
    }

    public void setCmdHead(CmdHead cmdHead) {
        this.cmdHead = cmdHead;
    }

    public void setCmdBody(CmdBody cmdBody) {
        this.cmdBody = cmdBody;
    }

    public void setCmdData(CmdData cmdData) {
        this.cmdData = cmdData;
    }

    public void setCmdTail(CmdTail cmdTail) {
        this.cmdTail = cmdTail;
    }
}
