package io.moquette.simple_test;


import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class MQTTServer {
    public static void main(String[] args) {
        MQTT mqtt = new MQTT();
        BlockingConnection connection = null;
        try {
            //设置服务端的ip
            mqtt.setHost("tcp://localhost:1883");

//            mqtt.setHost("tcp://192.168.8.10:1883");
//            mqtt.setUserName("test");
//            mqtt.setPassword("test");

            //连接前清空会话信息
            mqtt.setCleanSession(true);
            //设置重新连接的次数
            mqtt.setReconnectAttemptsMax(6);
            //设置重连的间隔时间
            mqtt.setReconnectDelay(2000);
            //设置心跳时间 低耗网络，但是又需要及时获取数据，心跳30s
            mqtt.setKeepAlive((short)30);
            //设置缓冲的大小  最大2M
            mqtt.setSendBufferSize(2 * 1024 * 1024);

            //创建阻塞式连接
            connection = mqtt.blockingConnection();

            connection.connect();
            try {
                int count = 0;
                while (true) {
                    count++;
                    // 指定topic
                    String topic = "test/test1";
                    // 设置消息内容
                    String message = "hello " + count;
                    connection.publish(topic, message.getBytes(), QoS.EXACTLY_ONCE, false);
                    System.out.println("MQTTServer receive message:  Topic: " + topic + "  Content :" + message);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
