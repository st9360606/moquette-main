package io.moquette;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT_Client_2 {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883"; // MQTT broker地址
        String clientId = "Client2"; // 客户端ID
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true); // 启用自动重连

            // 设置重新连接的次数和间隔时间
            connOpts.setMaxReconnectDelay(2000); // 最大重连间隔
            connOpts.setAutomaticReconnect(true); // 允许自动重连
            connOpts.setConnectionTimeout(30); // 连接超时时间

            // 设置用户名和密码
//            connOpts.setUserName("your_username");
//            connOpts.setPassword("your_password".toCharArray());

            // 设置 TLS/SSL 安全连接
            // connOpts.setSocketFactory(SSLSocketFactory.getDefault());

            System.out.println("连接到 MQTT broker: " + broker);
            client.connect(connOpts);
            System.out.println("连接成功");

            // 设置服务器接收消息的回调函数
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    System.out.println("连接丢失...");
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("收到消息: " + new String(message.getPayload()));
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("消息已发送...");
                }
            });

            // 订阅主题
            String topic = "kurt";
            int qos = 2;
            client.subscribe(topic, qos);
            System.out.println("订阅主题: " + topic);

            // 等待消息到达
            Thread.sleep(60000);

//            client.disconnect();
//            System.out.println("断开连接");
        } catch (MqttException | InterruptedException me) {
            me.printStackTrace();
        }
    }
}
