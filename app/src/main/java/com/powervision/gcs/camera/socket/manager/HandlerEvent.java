package com.powervision.gcs.camera.socket.manager;

import android.util.Log;

import org.apache.mina.core.buffer.IoBuffer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;


/**
 * 消息事件处理
 */
public class HandlerEvent {
    private static HandlerEvent handlerEvent;

    public static HandlerEvent getInstance() {
        if (handlerEvent == null) {
            handlerEvent = new HandlerEvent();
        }
        return handlerEvent;
    }

    public void handle(IoBuffer buf) throws IOException, InterruptedException, UnsupportedEncodingException, SQLException {
        Log.i("miaojx", "二进制数据" + buf.getHexDump());

        //解析包头
//        MinaMsgHead msgHead = new MinaMsgHead();
//        msgHead.bodyLen = buf.getInt();//包体长度
//        msgHead.event = buf.getShort();//事件号
//        if(buf.getHexDump(2).startsWith())

        Log.i("miaojx", "包头" + buf.getHexDump(2));
//        switch (msgHead.event){//根据事件号解析消息体内容
//            case Event.EV_S_C_TEST:
        byte[] by = new byte[506];
        buf.get(by, 0, by.length);
        if (by[0] == (byte) 0xaa && by[1] == 0x55) {
            Log.i("miaojx", "by[0]" + by[0]);
            byte[] bytes = {by[2], by[3]};
            Log.i("miaojx", "by[2]" + by[2] + "--by[3]" + by[3]);
            Log.i("miaojx", "----"+((short)by[2]<<8+(short)by[3]));

        }
//        String json = new String(by, "UTF-8").trim();
        //接下来根据业务处理....
//                break;
//        }
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }
    public static int bytesToInt3(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset]&0xFF)
                | ((ary[offset+1]<<8) & 0xFF00)
                | ((ary[offset+2]<<16)& 0xFF0000)
                | ((ary[offset+3]<<24) & 0xFF000000));
        return value;
    }
}
