package com.homemods.relay.pi.bluetooth;

import org.jetbrains.annotations.NotNull;

/**
 * @author sergeys
 */
public class BluetoothServerSocketNativeImpl implements BluetoothServerSocket.Impl {
    
    private String connectionName;
    
    @Override
    public void listen(int socket) {
        listenN(socket);
    }
    
    private native void listenN(int s);
    
    @NotNull
    @Override
    public BluetoothSocket acceptConnection(int socket) {
        int clientSocket = acceptConnectionN(socket);
        System.out.println("Accepted connection from " + connectionName);
        
        return new BluetoothSocket(clientSocket, new BluetoothSocketNativeImpl());
    }
    
    private native int acceptConnectionN(int s);
    
    @Override
    public void close(int socket) {
        closeN(socket);
    }
    
    private native void closeN(int s);
}
