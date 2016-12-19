package com.common;

/**
 * Created by Administrator on 2016/4/8.
 */


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * ��̳�����Զ����ɴ��룬���ݸ�ʽ���£�
 * date:���ڣ���ʽΪyyyy-MM-dd
 * timestamp��ʱ���
 * userID���û�ID
 * pageID��ҳ��ID
 * channel�����ID
 * action�������ע��
 */
public class SparkStreamingDataManuallyProducerforKafka extends Thread{


    //��̳���
    static String[] channelNames = new String[] {
            "spark","scala","kafka","Flink","hadoop","Storm",
            "Hive","Impala","Hbase","ML"
    };
    static  String[] actionNames = new String[]{"View","Register"};

    private String topic;//���͸�Kafak�����ݵ����
    private Producer<Integer,String> producerForKaka;

    private static  String dataToday;
    private static Random random;

    public SparkStreamingDataManuallyProducerforKafka(String topic){
        dataToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.topic = topic;
        random = new Random();
        Properties conf = new Properties();
        conf.put("metadata.broker.list", "master:9092,slave1:9092,slave2:9092");
        conf.put("serializer.class","kafka.serializer.StringEncoder");
        producerForKaka = new Producer<Integer,String>(new ProducerConfig(conf));
    }


    @Override
    public void run() {
        int counter = 0;
        while(true) {
            counter++;
            String userLog = userLogs();
            System.out.println("product:" + userLog);
            producerForKaka.send(new KeyedMessage<Integer, String>(topic,userLog));

            if(500 == counter) {
                counter = 0;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        new SparkStreamingDataManuallyProducerforKafka("UserLogs").start();
    }

    private static String userLogs() {
        StringBuffer userLogBuffer = new StringBuffer("");
            long timestamp = new Date().getTime();
            long userID = 0L;
            long pageID = 0L;

            //������ɵ��û�ID
            userID = random.nextInt((int) 2000);
            //������ɵ�ҳ��ID
            pageID = random.nextInt((int) 2000);
            //�������Chan
            String channel = channelNames[random.nextInt(10)];
            //�������action��Ϊ
            String action = actionNames[random.nextInt(2)];
            userLogBuffer.append(dataToday)
                    .append("\t")
                    .append(timestamp)
                    .append("\t")
                    .append(userID)
                    .append("\t")
                    .append(pageID)
                    .append("\t")
                    .append(channel)
                    .append("\t")
                    .append(action);
        return userLogBuffer.toString();
    }

}
