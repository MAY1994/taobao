package com.taobao.cache.listener;

import com.taobao.cache.activemq.ActivemqMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class InitListener implements MessageListener {

    @Autowired
    ActivemqMessageProcessor activemqMessageProcessor;

    /**
     * 监听器自动执行该方法
     * 自动提交offset
     * 执行业务代码
     */
    @Override
    public void onMessage(Message message) {
        System.out.println("监听到消息");
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("接收线程：" + Thread.currentThread().getName()+" 接收到的消息是："+message);
                //重起一个线程用来执行该message对应的缓存修改等服务
                activemqMessageProcessor.setMessage(textMessage);
                new Thread(activemqMessageProcessor).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("~~~~~~~~~~~~~kafkaConsumer消费结束~~~~~~~~~~~~~");
    }
}
