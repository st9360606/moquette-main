package io.moquette;

import org.eclipse.paho.client.mqttv3.*;

import javax.net.ssl.SSLSocketFactory;
import java.util.Scanner;

public class MQTTPublisher {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883"; // MQTT broker地址
        String clientId = "JavaSamplePublisher"; // 客户端ID
        String username = ""; // MQTT Broker的用户名
        String password = ""; // MQTT Broker的密码


        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // 设置重发时间间隔(每隔多少秒发送一个保持连接的控制包)
            connOpts.setKeepAliveInterval(5); // 重新發送時間間隔為 5 秒

            // 设置用户名和密码
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            // 设置 TLS/SSL 安全连接 (需有認證的網址)
//             connOpts.setSocketFactory(SSLSocketFactory.getDefault());

            System.out.println("连接到 MQTT broker: " + broker);
            client.connect(connOpts);
            System.out.println("连接成功");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("请输入要发布的主题（输入 q 退出）：");
                String topic = scanner.nextLine(); // 用户输入的主题

                if ("q".equals(topic)) {
                    break;
                }

                System.out.print("请输入要发布的消息内容：");
                String content = scanner.nextLine(); // 用户输入的消息内容

                int qos = 2;

                System.out.println("发布消息: " + content);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos); // 设置消息的 QoS 等级
                client.publish(topic, message);
            }

            client.disconnect();
            System.out.println("断开连接");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

