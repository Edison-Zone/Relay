@file:JvmName("RelaySimulated")

package com.homemods.relay.simulated

import com.homemods.relay.Relay
import com.homemods.relay.pin.InputPin
import com.homemods.relay.pin.OutputPin
import com.homemods.relay.pin.PinEdge
import com.homemods.relay.pin.PinState
import com.homemods.relay.simulated.connection.SimulatedClientConnection
import com.homemods.relay.simulated.connection.SimulatedConnectionFactory
import com.homemods.relay.simulated.pin.SimulatedInputPin
import com.homemods.relay.simulated.pin.SimulatedPinFactory
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JTextArea
import javax.swing.WindowConstants
import javax.swing.table.AbstractTableModel

/**
 * @author sergeys
 */

fun main(args: Array<String>) {
    val jFrame = JFrame("Relay Simulation")
    
    val simulatedPinFactory = SimulatedPinFactory()
    val simulatedBluetoothServer = SimulatedConnectionFactory()
    
    val tableModel = object : AbstractTableModel() {
        override fun getRowCount(): Int = 32
        
        override fun getColumnCount(): Int = 3
        
        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
            val row = rowIndex
            if (columnIndex == 0) {
                return row
            }
            val pin = simulatedPinFactory[row]
            if (columnIndex == 1) {
                return when (pin) {
                    null         -> "-"
                    is InputPin  -> "I"
                    is OutputPin -> "O"
                    else         -> "?"
                }
            } else {
                return when (pin) {
                    null                 -> false
                    is SimulatedInputPin -> pin.state
                    is OutputPin         -> pin.get() == PinState.ON
                    else                 -> false
                }
            }
        }
        
        override fun getColumnClass(columnIndex: Int): Class<*> {
            return if (columnIndex == 2) {
                java.lang.Boolean::class.java
            } else {
                java.lang.String::class.java
            }
        }
        
        override fun isCellEditable(rowIndex: Int,
                                    columnIndex: Int): Boolean = columnIndex == 2 && simulatedPinFactory[rowIndex] is SimulatedInputPin
        
        override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
            if (columnIndex == 2) {
                val pin = simulatedPinFactory[rowIndex]
                if (pin is SimulatedInputPin) {
                    pin.state = !pin.state
                    pin.listeners.forEach { listener ->
                        listener.invoke(pin.get(), if (pin.state) {
                            PinEdge.RISING
                        } else {
                            PinEdge.FALLING
                        })
                    }
                }
            }
        }
    }
    
    val jTable = JTable(tableModel)
    
    simulatedPinFactory.onPinUpdate = {
        tableModel.fireTableDataChanged()
    }
    
    jFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    
    val panel = JPanel()
    panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
    
    panel.add(jTable)
    
    val button = JButton("New Connection")
    button.addActionListener { action ->
        val bluetoothFrame = JFrame("Connection")
        val area = JTextArea(32, 1)
        area.isEditable = false
    
        val connection = object : SimulatedClientConnection() {
            override fun openInputStream(): InputStream = ByteArrayInputStream(ByteArray(0))
            
            override fun openOutputStream(): OutputStream = object : OutputStream() {
                override fun write(byte: Int) {
                    area.append("$byte\n")
                }
            }
            
            override fun close() {
                bluetoothFrame.isVisible = false
                bluetoothFrame.dispose()
            }
        }
        
        bluetoothFrame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        bluetoothFrame.add(JScrollPane(area))
        bluetoothFrame.pack()
        bluetoothFrame.isVisible = true
        
        simulatedBluetoothServer.discoveryCallback?.invoke(connection)
    }
    
    panel.add(button)
    
    jFrame.add(panel)
    jFrame.pack()
    jFrame.isVisible = true
    
    Relay(simulatedPinFactory, simulatedBluetoothServer).run()
    
    System.exit(0)
}