package io.moquette.simple_test;

import org.fusesource.mqtt.client.*;

/**
 * MQTT moquette 的Client 用于消费指定topic的消息
 */
public class MQTTClient {


    public static void main(String[] args) {
        //创建MQTT对象
        MQTT mqtt = new MQTT();
        BlockingConnection connection = null;
        try {
            //设置mqtt broker的ip和端口
            mqtt.setHost("tcp://localhost:1883");

            // 设置用户名密码类型连接
//            mqtt.setHost("tcp://192.168.8.10:1883");
//            mqtt.setUserName("test");
//            mqtt.setPassword("test");
//            mqtt.setClientId("client_123");

            //连接前清空会话信息
            mqtt.setCleanSession(true);
            //设置重新连接的次数
            mqtt.setReconnectAttemptsMax(6);
            //设置重连的间隔时间 单位毫秒
            mqtt.setReconnectDelay(2000);
            //设置心跳时间  低耗网络，但是又需要及时获取数据，心跳30s
            mqtt.setKeepAlive((short)30);
            //设置缓冲的大小 最大2M
            mqtt.setSendBufferSize(2 * 1024 * 1024);

            //获取mqtt的连接对象BlockingConnection
            connection = mqtt.blockingConnection();
            //创建连接
            connection.connect();
            //创建相关的MQTT 的主题列表
            Topic[] topics = {new Topic("test/test1", QoS.AT_LEAST_ONCE)};
            //订阅相关的主题信息
            byte[] qoses = connection.subscribe(topics);
            //
            while (true) {
                System.out.println("waiting...");

                //接收订阅的消息内容
                Message message = connection.receive();

                System.out.println("received...");

                //获取订阅的消息内容
                byte[] payload = message.getPayload();

                //
                System.out.println("MQTTClient Message  Topic：" + message.getTopic() + " Content :" + new String(payload));
                //确认消息回执
                message.ack();
                Thread.sleep(2000);
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
