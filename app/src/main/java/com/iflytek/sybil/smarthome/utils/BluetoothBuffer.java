package com.iflytek.sybil.smarthome.utils;

import java.nio.ByteBuffer;

/**
 * Created by guozhou on 2017/10/18.
 */

public class BluetoothBuffer {

    private int _rawBufferSize;
    private byte[] _rawBuffer = new byte[16];

    /**
     * appendBuffer（byte[] buffer）追加buffer
     * @param buffer
     */
    public  void appendBuffer(byte[] buffer) {
        if (null == buffer || 0 == buffer.length) return;
        int size = buffer.length + this._rawBufferSize;
        if (size <= this._rawBuffer.length) {
            System.arraycopy(buffer, 0, this._rawBuffer, this._rawBufferSize, buffer.length);
            this._rawBufferSize += buffer.length;
        }
//        else {
//            int newSize = this._rawBuffer.length;
//            while (newSize <= size) {
//                newSize *= 1.5;
//            }
//            byte[] newRawBuffer = new byte[newSize];
//            System.arraycopy(this._rawBuffer, 0, newRawBuffer, 0, this._rawBufferSize);
//            this._rawBuffer = newRawBuffer;
//            System.arraycopy(buffer, 0, this._rawBuffer, this._rawBufferSize, buffer.length);
//            this._rawBufferSize += buffer.length;
//        }
    }

    public byte[] get_rawBuffer() {
        return _rawBuffer;
    }

    public void set_rawBuffer(byte[] _rawBuffer) {
        this._rawBuffer = _rawBuffer;
    }

    /**
     * getFrontBuffer（int size）取前size长度的buffer
     * @param size
     * @return
     */
    public  byte[] getFrontBuffer(int size) {
        if (0 >= size || size > this._rawBufferSize) return null;
        byte[] buffer = new byte[size];
        System.arraycopy(this._rawBuffer, 0, buffer, 0, size);
        return buffer;
    }

    //releaseFrontBuffer（int size）释放前size长度的buffer
    public  void releaseFrontBuffer(int size) {
        if (0 >= size || size > this._rawBufferSize) return;
        System.arraycopy(this._rawBuffer, size, this._rawBuffer, 0, this._rawBufferSize - size);
        this._rawBufferSize -= size;
    }


}
