package com.homemods.relay.pi.bluetooth;

import org.jetbrains.annotations.NotNull;

/**
 * @author sergeys
 */
public class BluetoothClientNativeImpl implements BluetoothClient.Impl {
    
    @NotNull
    @Override
    public BluetoothClientSocket createSocket() {
        return new BluetoothClientSocket(allocSocket(), new BluetoothClientSocketNativeImpl());
    }
    
    private native int allocSocket();
}
