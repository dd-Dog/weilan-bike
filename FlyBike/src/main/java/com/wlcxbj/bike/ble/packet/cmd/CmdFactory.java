package com.wlcxbj.bike.ble.packet.cmd;

import android.util.Log;

import com.wlcxbj.bike.ble.encrypt.AES;
import com.wlcxbj.bike.ble.encrypt.SQRT98;
import com.wlcxbj.bike.ble.encrypt.XOR;
import com.wlcxbj.bike.ble.packet.part.CmdBody;
import com.wlcxbj.bike.ble.packet.part.CmdData;
import com.wlcxbj.bike.ble.packet.part.CmdHead;
import com.wlcxbj.bike.ble.packet.part.CmdTail;
import com.wlcxbj.bike.ble.packet.util.Util;
import com.wlcxbj.bike.global.BLEError;

import java.util.ArrayList;


/**
 * Created by bianjianbiao on 2017/3/4.
 */
public class CmdFactory {
    private static final String TAG = "CmdFactory";

    public static UnlockCmd createUnlockCmd(byte[] cmd) {
        return new UnlockCmd(cmd);
    }
    public static LocateCmd createLocateCmd(byte[] cmd) {
        return new LocateCmd(cmd);
    }

    public static UnlockCmd createUnlockCmd() {
        return new UnlockCmd();
    }

    /**
     * 从报文中解析出指令
     *
     * @param byteArrList
     * @return 拼接数据包后加密的command数组
     */
    public static Command parseByteToCmd(ArrayList<byte[]> byteArrList) {
        if (byteArrList == null || byteArrList.size() == 0) {
            BLEError.error(BLEError.PACKAGE_NULL);
            return null;
        }
        if (byteArrList.size() < 2) {
            BLEError.error(BLEError.PKG_LOST);
            return null;
        }
        Log.e(TAG, "接收到的数据包个数：" + byteArrList.size());
        //1.解析头部，拿到总共有多少个字节长度
        CmdHead cmdHead = parseCmdHead(byteArrList);
        int data_len = getDataLen(cmdHead);
        if (data_len < 0) {
            BLEError.error(BLEError.DATA_LENGTH_ERROR);
            return null;
        }
        //计算body+data+tail整体长度，以些来推算加密后的长度
        int cmd_len = CmdBody.LENGTH + data_len + CmdTail.LENGTH;
        byte[] encCmdBytes = new byte[cmd_len + CmdHead.LENGTH];
        if (!sortPkg(byteArrList)) {
            Log.e(TAG, "排序出错");
            return null;
        }
        try {
            for (int i = 0; i < byteArrList.size(); i++) {
                Log.e(TAG, "i=" + i + "size=" + byteArrList.size());
                if (i == (byteArrList.size() - 1)) {
                    Util.arrayCopy(byteArrList.get(i), 2, encCmdBytes, i * 18,
                            cmd_len + CmdHead.LENGTH - i * 18);
                } else {
                    Util.arrayCopy(byteArrList.get(i), 2, encCmdBytes, i * 18, 18);
                }
                Log.e(TAG, "receive bytes,i=" + i + ", bytes=" + AES.byte2hex(encCmdBytes));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            BLEError.error(BLEError.ERROR_WHEN_PARSE_PKG);
            return null;
        }

        Log.e(TAG, "all of the receive bytes=" + AES.byte2hex(encCmdBytes));

        //2.解密random_data, body,data,tail,前8个字节为随机数
        //2.1反异或
        byte[] xorEncryptedBodyDataTailBytes = new byte[encCmdBytes.length - CmdHead.LENGTH];
        System.arraycopy(encCmdBytes, CmdHead.LENGTH,
                xorEncryptedBodyDataTailBytes, 0,
                xorEncryptedBodyDataTailBytes.length);
        Log.e(TAG, "解密之前的数据（除head外）＝" + AES.byte2hex(xorEncryptedBodyDataTailBytes));
        byte[] decryptedBodyDataTailBytes = XOR.decryptedWithRandom
                (xorEncryptedBodyDataTailBytes, cmdHead.getRandom_data());
        Log.e(TAG, "XOR解密之后的数据（除head外）＝" + AES.byte2hex(decryptedBodyDataTailBytes));
        //2.解析body
        byte[] decryptedBodyBytes = new byte[CmdBody.LENGTH];
        System.arraycopy(decryptedBodyDataTailBytes, 0, decryptedBodyBytes, 0, CmdBody.LENGTH);
        Log.e(TAG, "encryptedBodyBytes.LENGTH=" + decryptedBodyBytes.length);
        CmdBody cmdBodyDec = new CmdBody(decryptedBodyBytes);
        boolean check = checkCmdCheck(cmdBodyDec);
        if (!check) {
            BLEError.error(BLEError.ERROR_CMD_CHECK_FAILUR);
            return null;
        }

        //3.解析data
        byte[] cmd_data_len = cmdHead.getCmd_data_len();//获取数据域长度
        int datalen_real = cmd_data_len[0] + cmd_data_len[1] * 256;
        byte[] decryptedDataBytes = new byte[datalen_real];
        //从头长度，body长度是加密过的16
        System.arraycopy(decryptedBodyDataTailBytes, CmdBody.LENGTH,
                decryptedDataBytes, 0, datalen_real);
        CmdData cmdDataDec = new CmdData(decryptedDataBytes);

        //4.解析tail
        byte[] decryptedTailBytes = new byte[CmdTail.LENGTH];
        System.arraycopy(decryptedBodyDataTailBytes, CmdBody.LENGTH + datalen_real,
                decryptedTailBytes, 0, CmdTail.LENGTH);
        boolean checkTail = checkTail(decryptedTailBytes);
        if (!checkTail) {
            BLEError.error(BLEError.ERROR_CMD_TAIL_FAILURE);
            return null;
        }
        CmdTail cmdTailDec = new CmdTail(decryptedTailBytes);
        //5.验证数据完整性
        UnlockCmd unlockCmd = new UnlockCmd();
        unlockCmd.setCmdHead(cmdHead);
        unlockCmd.setCmdBody(cmdBodyDec);
        unlockCmd.setCmdData(cmdDataDec);
        unlockCmd.setCmdTail(cmdTailDec);
        boolean b = unlockCmd.checkInteger();
        Log.e(TAG, "解密数据是否完整？" + (b ? "是" : "否"));
        if (!b) {
            BLEError.error(BLEError.CHECK_SUM_ERROR);
            return null;
        }
        return unlockCmd;
    }

    /**
     * 校验尾部
     */
    private static boolean checkTail(byte[] tailBytes) {
        int len = tailBytes.length;
        Log.e(TAG, "tail:" + tailBytes[len-1]);
        if (Util.getPositiveValue(tailBytes[len-1]) == 0xEF && Util.getPositiveValue(tailBytes[len-2]) == 0x86) {
            return true;
        }
        return false;
    }

    /**
     * 校验标志位
     */
    private static boolean checkCmdCheck(CmdBody cmdBodyDec) {
        byte[] cmd_check = cmdBodyDec.getCmd_check();
        if (cmd_check[0] == 0x12 && cmd_check[1] == 0x34) {
            return true;
        }
        return false;
    }

    /**
     * 根据包的序号进行排序,冒泡
     *
     * @param byteArrList
     */
    private static boolean sortPkg(ArrayList<byte[]> byteArrList) {
        for (int i = 0; i < byteArrList.size() - 1; i++) {
            byte[] bytes_i = byteArrList.get(i);
            for (int j = i + 1; j < byteArrList.size(); j++) {
                byte[] bytes_j = byteArrList.get(j);
                if (bytes_i[1] > bytes_j[1]) {
                    byte[] temp = bytes_i;
                    byteArrList.add(i, bytes_j);
                    byteArrList.add(j, bytes_i);
                }
            }
        }
        for (int i = 0; i < byteArrList.size(); i++) {
            if (!(byteArrList.get(i)[1] == i)) {
                Log.e(TAG, "------------------排序出错");
                BLEError.error(BLEError.PKG_SORT_ERROR);
                return false;
            }
        }
        return true;
    }

    /**
     * 计算数据域长度
     *
     * @param cmdHead
     * @return
     */
    private static int getDataLen(CmdHead cmdHead) {
        if (cmdHead == null) {
            return -1;
        }
        byte[] cmd_data_len = cmdHead.getCmd_data_len();
        //计算data长度
        int i = cmd_data_len[0] & 0xff;
        int j = cmd_data_len[1] & 0xff;
        int dateLen = i + j * 256;
        Log.e(TAG, "数据域长度＝" + dateLen);
        return dateLen;
    }

    /**
     * 解析收到数据包的头部
     *
     * @param byteArrList
     * @return
     */
    private static CmdHead parseCmdHead(ArrayList<byte[]> byteArrList) {
        byte[] firstPkg = null;
        //找第一个数据包
        for (int i = 0; i < byteArrList.size(); i++) {
            firstPkg = byteArrList.get(i);
            if (firstPkg[1] == 0) {
                break;
            } else {
                firstPkg = null;
            }

        }
        if (firstPkg == null) {
            BLEError.error(BLEError.PACKAGE_HEAD_NOT_FOUND);
            return null;
        }
        byte[] encHeadBytes = new byte[CmdHead.LENGTH];
        System.arraycopy(firstPkg, 2, encHeadBytes, 0, CmdHead.LENGTH);
        Log.e(TAG, "解析收到的头部密文＝" + AES.byte2hex(encHeadBytes));
        byte[] decHeadBytes = SQRT98.decrypt(encHeadBytes);
        Log.e(TAG, "解析收到的头部明文＝" + AES.byte2hex(decHeadBytes));
        CmdHead cmdHead = new CmdHead(decHeadBytes);
        //校验头部信息
        if (!(cmdHead.getEncrypt_type() == Command.ENRYPT_TYPE_AES ||
                cmdHead.getEncrypt_type() == Command.ENRYPT_TYPE_XOR ||
                cmdHead.getEncrypt_type() == Command.ENRYPT_TYPE_SQRT98)) {
            BLEError.error(BLEError.ENCRYPT_TYPE_ERROR);
            return null;
        }
        return cmdHead;
    }
}
