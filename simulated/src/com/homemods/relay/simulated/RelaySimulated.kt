@file:JvmName("RelaySimulated")

package com.homemods.relay.simulated

import com.homemods.relay.Relay
import com.homemods.relay.pin.InputPin
import com.homemods.relay.pin.OutputPin
import com.homemods.relay.pin.PinEdge
import com.homemods.relay.pin.PinState
import com.homemods.relay.simulated.pin.SimulatedInputPin
import com.homemods.relay.simulated.pin.SimulatedPinFactory
import javax.swing.JFrame
import javax.swing.JTable
import javax.swing.WindowConstants
import javax.swing.table.AbstractTableModel

/**
 * @author sergeys
 */

fun main(args: Array<String>) {
    val jFrame = JFrame("Relay Simulation")
    
    val simulatedPinFactory = SimulatedPinFactory()
    
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
    jFrame.add(jTable)
    jFrame.pack()
    jFrame.isVisible = true
    
    Relay(simulatedPinFactory).run()
    
    System.exit(0)
}