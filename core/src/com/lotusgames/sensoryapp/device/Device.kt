package com.lotusgames.sensoryapp.device

import java.lang.Exception
import java.net.Socket
import java.util.concurrent.TimeoutException
import java.util.zip.CRC32


class Device (var deviceConnection: DeviceConnection) {
    private val TIME_CAROUSEL_US = 10000
    private val BEG_CHAR: Byte = 0xB4.toByte()

    private val CMD_SET_IMPULSE: Byte = 0x01
    private val CMD_CLEAR: Byte = 0x02
    private val CMD_INIT: Byte = 0x03
    private val CMD_CFG_SEG: Byte = 0x04
    private val CMD_SELF_TEST: Byte = 0x05

    private var segments: MutableList<Segment> = mutableListOf()

    init {
        segments.add(Segment("finger",      4, intArrayOf(0, 1, 2, 3, 4, 5, 6, 7), 4, intArrayOf(12, 13, 14, 15),500))
    }

    fun initialize() {
        for (seg in segments) {
            set_seg(seg)
        }

        sendCommand(cmd_init(), msg = "Init")
    }

    fun self_test() {
        sendCommand(cmd_self_test(), msg="Self Test")
    }

    fun get_mask(arr: IntArray): Int {
        var mask = 0
        for (i in 0..15) {
            if (i in arr) {
                mask = mask or (1 shl i)
            }
        }
        return mask
    }

    fun set_seg(segment: Segment) {
        val id = segments.indexOf(segment)
        val mask1 = get_mask(segment.mask1)
        val mask2 = get_mask(segment.mask2)
        sendCommand(cmd_set_seg(id, segment.adr1, mask1, segment.adr2, mask2, segment.timeSlot), msg= "set segment ${segment.name}")
    }

    fun set_impulse(segment: Int, period: Int, tau: Int) {
        sendCommand(cmd_set_impulse(segment, period, tau), msg="impulse on " + segments[segment].name + ", tau=" + tau)
    }

    fun clear(segment: Int) {
        sendCommand(cmd_clear(segment), msg="clear impulse on " + segments[segment].name)
    }

    fun cmd_set_seg(segment: Int, adr1: Int, mask1:Int, adr2: Int, mask2:Int, time_window_us:Int): ByteArray {
        var cmd = byteArrayOf(CMD_CFG_SEG)
        cmd += UInt16toByteArray(mask1)
        cmd += UInt16toByteArray(mask2)
        cmd += UInt16toByteArray(time_window_us)
        cmd += byteArrayOf(segment.toByte())
        cmd += byteArrayOf(adr1.toByte())
        cmd += byteArrayOf(adr2.toByte())
        return cmd
    }

    fun cmd_set_impulse(segment: Int, per: Int, duration: Int): ByteArray {
        var cmd = byteArrayOf(CMD_SET_IMPULSE)
        var period = per
        if (period < 1) {
            period = 1
        }
        cmd += UInt16toByteArray(segment)
        cmd += UInt16toByteArray(duration)
        cmd += UInt16toByteArray(period)
        return cmd
    }

    fun cmd_clear(segment: Int): ByteArray {
        var cmd = byteArrayOf(CMD_CLEAR)
        cmd += byteArrayOf(segment.toByte())
        return cmd
    }

    fun cmd_self_test(): ByteArray {
        var cmd = byteArrayOf(CMD_SELF_TEST)
        return cmd
    }

    fun cmd_init(): ByteArray {
        var cmd = byteArrayOf(CMD_INIT)
        return cmd
    }

    private fun sendCommand(command: ByteArray, log: Boolean=true, msg: String="") {
        if (deviceConnection.useProtocol()) {
            sendCommandWrapped(command, log, msg)
        } else {
            sendCommandSimple(command, log, msg)
        }
    }

    private fun sendCommandSimple(command: ByteArray, log: Boolean=true, msg: String="") {
        val cmd = command

        val timeout = 1000;
        try {
            deviceConnection.write(cmd)
            val response: ByteArray
            if (command[0] == CMD_SELF_TEST) {
                response = deviceConnection.read(7, timeout)
            } else {
                response = deviceConnection.read(4, timeout)
            }

            if (log) {
                val cmd_data = response
                var cmd_data_str: String?
                if (command[0] == CMD_SELF_TEST) {
                    cmd_data_str = "addr=" + cmd_data[4].toString() + "; bad pins:" + bytesToBitMask(cmd_data.copyOfRange(5, cmd_data.size))
                } else {
                    cmd_data_str = bytesToHex(cmd_data)
                }
                val cmd_log = msg + " --> " + response?.decodeToString(1, 3) + " " + cmd_data_str
                println(cmd_log)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to send command to device")
        }
    }

    private fun sendCommandWrapped(command: ByteArray, log: Boolean=true, msg: String="") {
        val cmd = wrapPacket(command)

        val response: ByteArray
        val timeout = 1000;
        try {
            deviceConnection.write(cmd)
            var beg: ByteArray;
            do {
                beg = deviceConnection.read(1, timeout)
            } while (beg[0] != BEG_CHAR)

            val length = deviceConnection.read(1, timeout)[0].toUByte()

            val resp = deviceConnection.read(length.toInt() - 1, timeout)
            response = unwrapPacket(resp)

            if (log) {
                val cmd_data = response.copyOfRange(4, response.size)
                var cmd_data_str: String?
                if (command[0] == CMD_SELF_TEST) {
                    cmd_data_str = "addr=" + cmd_data[0].toString() + bytesToBitMask(cmd_data.copyOfRange(1, cmd_data.size - 1))
                } else {
                    cmd_data_str = bytesToHex(cmd_data)
                }
                val cmd_log = msg + " --> " + response.decodeToString(1, 3) + " " + cmd_data_str + "\n"
                println(cmd_log)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to send command to device")
        }
    }

    private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 3)
        for (j in bytes.indices) {
            val v: Int = bytes[j].toUByte().toInt() and 0xFF
            hexChars[j * 3] = HEX_ARRAY[v ushr 4]
            hexChars[j * 3 + 1] = HEX_ARRAY[v and 0x0F]
            hexChars[j * 3 + 2] = ' '
        }
        return String(hexChars)
    }

    private fun wrapPacket(command: ByteArray): ByteArray {
        val crc32 = CRC32()
        crc32.reset()
        crc32.update(command)
        var cmd = byteArrayOf((command.size + 5).toByte()) + command
        cmd = byteArrayOf(BEG_CHAR) + cmd + UInt32toByteArray(crc32.value)
        return cmd
    }

    private fun unwrapPacket(packet: ByteArray): ByteArray {
        if (packet.size < 4)
            throw RuntimeException("Packet length should be >= 4");

        val crc_recv = ByteArrayToUInt32(packet.copyOfRange(packet.size-4, packet.size))
        val crc32 = CRC32()
        crc32.reset()
        val data = packet.copyOfRange(0, packet.size - 4)
        crc32.update(data)
        val crc_calc = crc32.value

        if (crc_recv != crc_calc)
            return packet

        return data
    }

    private fun ByteArrayToUInt32(bytes: ByteArray): Long {
        var value: ULong = 0U
        value = value or (bytes[0].toUByte().toULong() shl 0)
        value = value or (bytes[1].toUByte().toULong() shl 8)
        value = value or (bytes[2].toUByte().toULong() shl 16)
        value = value or (bytes[3].toUByte().toULong() shl 24)
        return value.toLong()
    }

    private fun UInt32toByteArray(value: Long): ByteArray {
        val bytes = ByteArray(4)
        bytes[0] = (value and 0xFFFF).toByte()
        bytes[1] = ((value ushr 8) and 0xFFFF).toByte()
        bytes[2] = ((value ushr 16) and 0xFFFF).toByte()
        bytes[3] = ((value ushr 24) and 0xFFFF).toByte()
        return bytes
    }

    fun UInt16toByteArray(value: Int): ByteArray {
        val bytes = ByteArray(2)
        bytes[0] = (value and 0xFFFF).toByte()
        bytes[1] = ((value ushr 8) and 0xFFFF).toByte()
        return bytes
    }

    private fun bytesToBitMask(bytes: ByteArray): String {
        val indices = mutableListOf<Int>();
        for (i in bytes.indices) {
            for (j in 0..7) {
                if (((bytes[i].toUByte().toInt() ushr j) and 0x1) != 0) {
                    indices.add(i * 8 + j);
                }
            }
        }
        var string = " | "
        for (ind in indices) {
            string += "$ind | "
        }
        return string
    }
}