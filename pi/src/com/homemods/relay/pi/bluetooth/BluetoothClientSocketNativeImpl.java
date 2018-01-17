package com.homemods.relay.pi.bluetooth;

import org.jetbrains.annotations.NotNull;

/**
 * @author sergeys
 */
public class BluetoothClientSocketNativeImpl extends BluetoothSocketNativeImpl implements BluetoothClientSocket.Impl {
    
    @Override
    public int connect(int socket,
                       @NotNull
                               String address, int port) {
        return connectN(socket, address, (short) (port & 0xFFFF));
    }
    
    private native int connectN(int socket, String address, short port);
}
