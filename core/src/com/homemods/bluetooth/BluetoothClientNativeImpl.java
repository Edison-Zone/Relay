package com.homemods.bluetooth;

import org.jetbrains.annotations.NotNull;

/**
 * @author sergeys
 */
class BluetoothClientNativeImpl implements BluetoothClient.Impl {
    
    @NotNull
    @Override
    public BluetoothClientSocket createSocket() {
        return new BluetoothClientSocket(allocSocket(), new BluetoothClientSocketNativeImpl());
    }
    
    private native int allocSocket();
}
