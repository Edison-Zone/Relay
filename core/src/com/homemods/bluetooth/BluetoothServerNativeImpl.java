package com.homemods.bluetooth;

import org.jetbrains.annotations.NotNull;

/**
 * @author sergeys
 */
class BluetoothServerNativeImpl implements BluetoothServer.Impl {
    
    @NotNull
    @Override
    public BluetoothServerSocket createSocket(int port) {
        
        int s = allocSocket((short) (port & 0xFFFF));
        
        return new BluetoothServerSocket(s, new BluetoothServerSocketNativeImpl());
    }
    
    private native int allocSocket(short port);
}
