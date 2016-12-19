package com.common;

/**
 * Created by Administrator on 2016/4/8.
 */


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class DataManually {


    //��̳���
    static String[] channelNames = new String[] {
            "spark","scala","kafka","Flink","hadoop","Storm",
            "Hive","Impala","Hbase","ML"
    };
    static  String[] actionNames = new String[]{"View","Register"};
    static String yesterdayFormated;

    public static void main(String[] args) {
        /**
         * ���ɵ���������
         */

        long numberItems = 5000;
        String path = ".";
        if(args.length > 0) {
            numberItems = Integer.valueOf(args[0]);
            path = args[1];
        }
        System.out.println("user log number is :" + numberItems);

        /**
         * �����ʱ�������
         */
        yesterdayFormated = yesterday();
        
        userLogs(numberItems,path);
        
    }

    private static void userLogs(long numberItems, String path) {
        StringBuffer userLogBuffer = new StringBuffer();
        Random random = new Random();
        for(int i = 0; i<numberItems;i++) {
            long timestamp = new Date().getTime();
            long userID = 0L;
            long pageID = 0L;

            //������ɵ��û�ID
            userID = random.nextInt((int) numberItems);
            //������ɵ�ҳ��ID
            pageID = random.nextInt((int) numberItems);
            //�������Chan
            String channel = channelNames[random.nextInt(10)];
            //�������action��Ϊ
            String action = actionNames[random.nextInt(2)];
            userLogBuffer.append(yesterdayFormated)
                    .append("\t")
                    .append(timestamp)
                    .append("\t")
                    .append(userID)
                    .append("\t")
                    .append(pageID)
                    .append("\t")
                    .append(channel)
                    .append("\t")
                    .append(action)
                    .append("\n");
        }
        //System.out.print(userLogBuffer.toString());
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path+"userLog.log")) );
            printWriter.write(userLogBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }

    }

    public static String yesterday() {
        SimpleDateFormat data = new SimpleDateFormat("yyyy-mm-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);

        Date yesterday = cal.getTime();
        return data.format(yesterday);
    }


}
