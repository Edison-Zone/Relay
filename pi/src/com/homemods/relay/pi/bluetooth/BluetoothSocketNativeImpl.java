package com.homemods.relay.pi.bluetooth;

import org.jetbrains.annotations.NotNull;

/**
 * @author sergeys
 */
public class BluetoothSocketNativeImpl implements BluetoothSocket.Impl {
    
    @Override
    public int read(int socket,
                    @NotNull
                            byte[] bytes, int count) {
        return readN(socket, bytes, count);
    }
    
    private native int readN(int s, byte[] bytes, int count);
    
    @Override
    public int write(int socket,
                     @NotNull
                             byte[] bytes, int count) {
        return writeN(socket, bytes, count);
    }
    
    private native int writeN(int s, byte[] bytes, int count);
    
    @Override
    public void close(int socket) {
        closeN(socket);
    }
    
    private native void closeN(int s);
}
