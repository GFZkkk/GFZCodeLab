package com.gfz.message

import java.io.IOException
import java.net.*

class SendSocket() {

    var host = "127.0.0.1"

    fun pushWithUDPSocket(str: String) {
        val socket: DatagramSocket
        try {
            //创建DatagramSocket对象并指定一个端口号，注意，如果客户端需要接收服务器的返回数据,
            //还需要使用这个端口号来receive，所以一定要记住
            socket = DatagramSocket(MessageManger.port)
            //使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址
            val serverAddress = InetAddress.getByName(host)
            val data = str.toByteArray() //把字符串str字符串转换为字节数组
            //创建一个DatagramPacket对象，用于发送数据。
            //参数一：要发送的数据  参数二：数据的长度  参数三：服务端的网络地址  参数四：服务器端端口号
            val packet = DatagramPacket(data, data.size, serverAddress, 10025)
            socket.send(packet) //把数据发送到服务端。
        } catch (e: SocketException) {
            e.printStackTrace()
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}