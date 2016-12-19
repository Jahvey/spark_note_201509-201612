package com.java__SparkStreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Administrator on 2016/4/17.
 */
public class wordCountOnLine {

    public static void main(String[] args) {
        /**
         * ����SparkConf��
         * 1������2���̣߳���ΪSpark Streaming Ӧ�ó��������е�ʱ��������һ���������ֳ����ϵ�ѭ���������ݣ�����
         * ������һ���߳��û�������ܵ����ݣ�����Ļ��޷����߳����ڴ������ݣ�����ʱ������ƣ��ڴ�ʹ��̶��᲻���ظ���
         * 2�����ڼ�Ⱥ���ԣ�ÿ��Executorһ��϶���ֹһ���̣߳����ڴ���spark streaming��Ӧ�ó���ÿ��executorһ�����
         * ����coree�ȽϺ��ʣ����ݹ�ȥ�ľ��飬5�����ҵ�core����ѵģ�
         */
        //SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("wordCountOnLine");
        /**
         * �ڶ�����
         * 1.����SparkStreamingContext�������SparkStreamingӦ�ó������й��ܵ���ʼ��ͳ�����ȵĺ���
         * SparkStreamingContext�Ĺ������Ի���SparkConf������Ҳ�ɻ��ڳ־û���SparkStreamingContext��������
         * �ָ�����
         * 2.��һ��spark streamingӦ�ó����п��Դ������ɸ�SparkStreamingContext����ʹ����һ��SparkStreamingContext
         * ֮ǰ��Ҫ��ǰ���������е�SparkStreamingContext����رյ����ɴˣ����ǻ��һ���ش��������SparkStreaming Ҳֻ��
         * spark core�ϵ�һ��Ӧ�ó���ֻ����Spark Streaming �����Ҫ���еĻ���Ҫspark����ʦдҵ���߼����롣
         */
        //JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        final String checkpointDirectory = "hdfs://master:9000/sparkStreaming/checkPoint_Data";
        JavaStreamingContextFactory factory = new JavaStreamingContextFactory() {
            @Override
            public JavaStreamingContext create() {
                return createContext(checkpointDirectory);
            }
        };
        /**
         * ���Դ�ʧ���лظ�Driver����������Ҫָ��Driver�������������Cluster���������ύӦ�ó���
         * ��ʱ��ָ��--supervise��
         */
        JavaStreamingContext jssc = JavaStreamingContext.getOrCreate(checkpointDirectory, factory);

        /**
         * ������������SparkStreaming����������Դinput Streaming
         *  ���������ʱ�����û�����ݵĻ������ϵ��������������ɵ�����Դ���˷ѡ�
         */
        JavaReceiverInputDStream lines = jssc.socketTextStream("master", 9999);
        /**
         * �������������RDD���һ������DStream���б�̣�����ԭ����DStream��RDD������ģ�壨����˵�ࣩ
         * ��spark Streaming��������ǰ����ʵ���ǰ�ÿ��DStream�Ĳ��������Ϊ��RDD�Ĳ�����
         */
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String,String>() {
            @Override
            public Iterable<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" "));
            }

        });
        JavaPairDStream<String,Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String, Integer>(word, 1);
            }
        });
        JavaPairDStream<String,Integer> wordsCount = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1+v2;
            }
        });
        /**
         * �˴���print������ֱ�Ӵ���job��ִ�У���Ϊ���ڵ�һ�ж�����sparkSteaming��ܵĿ���֮�µģ�����spark streaming��
         * �����Ƿ񴥷������Ķ�job�ǻ���durationʱ���������ġ�
         * Spark StreamingӦ�ó���Ҫ��ִ�о����Job����DStream�ͱ�����output Stream����
         * output Strram�кܶ����͵ĺ�������������print��saveASTextFile��������Ϊ��Ҫ��һ��������
         * foreachRDD����ΪSpark Streaming����Ľ��һ�㶼�����Redis��DB�������棬foreachRDD
         * ��Ҫ�������������Щ���ܵģ����ҿ��������ָ���������ݵ��׷������
         */
        wordsCount.print();
        /**
         * spark Streamingִ������Ҳ����Dirver��ʼ���У�Dirver������ʱ����λ��һ��
         * �µ��߳��еģ����ڲ�����Ϣѭ���壬���ڽ���Ӧ�ó���ʡ����executor�е���Ϣ
         */
        jssc.start();
        jssc.awaitTermination();
        jssc.close();

    }

    private static JavaStreamingContext createContext(String checkpointDirectory) {

        // If you do not see this printed, that means the StreamingContext has been loaded
        // from the new checkpoint
        System.out.println("Creating new context");
        SparkConf sparkConf = new SparkConf().setAppName("wordCountOnLine");
        // Create the context with a 1 second batch size
        JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, Durations.seconds(30));
        ssc.checkpoint(checkpointDirectory);
        return ssc;
    }
}
