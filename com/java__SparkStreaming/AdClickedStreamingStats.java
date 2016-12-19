package com.java__SparkStreaming;

import com.google.common.base.Optional;
import groovy.lang.Tuple;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.kitesdk.shaded.com.google.common.base.*;
import scala.Int;
import scala.Tuple2;

import java.sql.*;
import java.util.*;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2016/7/1.
 * ���ݸ�ʽ��timestamp,ip,userID,adID,province,city
 */
public class AdClickedStreamingStats {

    public static void mian(String[] args) {

        SparkConf conf = new SparkConf().setMaster("local[5]").setAppName("AdClickedStreamingStats");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(10));

        Map<String,String> kafkaParameters = new HashMap<String,String>();
        kafkaParameters.put("metadata.broker.list", "master:9092,slave1:9092,slave2:9092");
        Set<String> topics = new HashSet<String>();
        topics.add("SparkStreamingDirected");
        JavaPairInputDStream<String,String> adClickedStreaming = KafkaUtils.createDirectStream(jsc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                kafkaParameters,
                topics);
        /**
         *����˼·
         * ͳ�ƹ��˳���Ч��� adClickedStreaming ->mapToPair->reduceByKey->filter->foreachRDD���洢���ⲿ�洢ϵͳ�У�
         * ���������ˣ�һ����ͬһ���û����ͬһ��ad����50�ξ����������
         *             ����ÿ�������ʱ��������Ҫ�ж��Ƿ��Ǻ����� adClickedStreaming.transformToPair����ѯ���������ݱ�
         *             blacklisttable Ȼ����й��ˣ�
         *             Ȼ�������к����������ɣ�һ����ͬһ���û������adclicked��ͬһ��ad����50�ξ��������������Ȼ��
         *             д�����ݿ�blacklisttable�Թ��´�����������������жϹ��˳�������
         */
        //�������ݸ�ʽ��timestamp��ip��userID��adID��province��

        /**
         * ��ΪҪ�Ժ������������߹��ˣ�����������RDD�еģ����Ա�Ȼʹ��transform��
         * �������������Ǳ���ʹ��transformToPair��ԭ���Ƕ�ȡ������Kafka��������<String, Long>���͵�
         * ����һ��ԭ���ǹ��˺������Ҫ���н�һ���������Ա����Ƕ�������Kafka���ݵ�ԭʼ����DStream<String,Long>
         */
        JavaPairDStream<String,String> filteradClickedStreaming = adClickedStreaming.transformToPair(new Function<JavaPairRDD<String, String>, JavaPairRDD<String, String>>() {
            @Override
            public JavaPairRDD<String, String> call(JavaPairRDD<String, String> rdd) throws Exception {
                /**
                 * ˼·��
                 * 1�������ݿ��л�ȡ������ת����RDD�������µ�RDDʵ����װ����������
                 * 2��Ȼ��Ѵ����������RDD��ʵ����Batch Duration������RDD����leftOuterJoin������Ҳ����˵ʹ��Batch Duration
                 * ������RDD�ʹ����������RDD��ʵ������leftOuterJoin������������߶�������
                 * �Ļ����ͻ���true������Ļ�����false��
                 * ����Ҫ���������ǲ��������false
                 */
                JavaSparkContext jsc = new JavaSparkContext(rdd.context());
                /**
                 * �������ı���ֻ��userID���������Ҫ����join�����Ļ����ͱ�����key-value��
                 * ����������������Ҫ�������ݱ��е����ݲ���Key-Value���͵����ݼ��ϣ�
                 */
                List<String> blackListNames = new ArrayList<String>();//���ڱ�������ݿ��ѯ�����ĺ�����
                JDBCWrapper jdbcWrapper = JDBCWrapper.getJDBCInstance();
                jdbcWrapper.doQueryBatch("SELECT * FROM blacklisttable", null, new ExecuteCallBack() {
                    @Override
                    public void resultCallBack(ResultSet result) throws Exception {
                        while(result.next()){
                            blackListNames.add(result.getString(1));
                        }
                    }
                });
                List<Tuple2<String,Boolean>> blackListTuple = new ArrayList<Tuple2<String, Boolean>>();
                for(String name : blackListNames) {
                    blackListTuple.add(new Tuple2<String,Boolean>(name,true));//ӳ���name��true
                }
                //List<Tuple2<String,Boolean>>
                List blackListFromDB =blackListTuple;//���������ڲ�ѯ�ĺ��������ݱ��в�ӳ���String��Boolean��
                JavaPairRDD<String, Boolean> blackListRDD = jsc.parallelizePairs(blackListFromDB);

                /**
                 * ����join������ʱ��϶��ǻ���userID����join�ģ����Ա����rdd����mapToPair����ת����Ϊ���ϸ�ʽ��rdd
                 */
                JavaPairRDD<String, Tuple2<String, String>> rdd2Pair = rdd.mapToPair(new PairFunction<Tuple2<String, String>, String, Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, Tuple2<String, String>> call(Tuple2<String, String> t) throws Exception {
                        String userID = t._2().split("\t")[2];
                        return new Tuple2<String, Tuple2<String, String>>(userID, t);
                    }
                });
                /**
                 * var rdd1 = sc.makeRDD(Array(("A","1"),("B","2"),("C","3")),2)
                 var rdd2 = sc.makeRDD(Array(("A","a"),("C","c"),("D","d")),2)

                 scala> rdd1.leftOuterJoin(rdd2).collect
                 res11: Array[(String, (String, Option[String]))] = Array((B,(2,None)), (A,(1,Some(a))), (C,(3,Some(c))))
                 */
                JavaPairRDD<String, Tuple2<Tuple2<String, String>, Optional<Boolean>>> joined = rdd2Pair.leftOuterJoin(blackListRDD);

                JavaPairRDD<String, String> result = joined.filter(new Function<Tuple2<String, Tuple2<Tuple2<String, String>, Optional<Boolean>>>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<String, Tuple2<Tuple2<String, String>, Optional<Boolean>>> v1) throws Exception {
                        Optional<Boolean> optional = v1._2()._2();
                        if (optional.isPresent() && optional.get()) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }).mapToPair(new PairFunction<Tuple2<String, Tuple2<Tuple2<String, String>, Optional<Boolean>>>, String, String>() {
                    @Override
                    public Tuple2<String, String> call(Tuple2<String, Tuple2<Tuple2<String, String>, Optional<Boolean>>> t) throws Exception {
                        return t._2()._1();
                    }
                });
                return result;
            }
        });
        JavaPairDStream<String,Long> pairs = filteradClickedStreaming.mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> t) throws Exception {
                String[] splited = t._2().split("\t");
                String timestamp = splited[0];//YYYY-MM-DD
                String ip = splited[1];
                String userID = splited[2];
                String adID = splited[3];
                String province = splited[4];
                String city = splited[5];
                String clickedRecord = timestamp + "_" +
                        ip + "_" +
                        userID + "_" +
                        adID + "_" +
                        province + "_" +
                        city;
                return new Tuple2<String, Long>(clickedRecord, 1L);
            }
        });

        //����ÿ��Batch Duration�û��������
        JavaPairDStream<String,Long>  adClickedUsers= pairs.reduceByKey(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        });
        /**
         * �������Ч�ĵ��
         * �˴����ж�ÿ��Batch Duration���û�����Ĵ����������һ��,����˵�
         */
        JavaPairDStream<String,Long> filteredClickInBatch = adClickedUsers.filter(new Function<Tuple2<String, Long>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, Long> v1) throws Exception {
                if(v1._2() > 1) {
                    //����һ�º����������ݱ�
                    return false;
                } else {
                    return true;
                }
            }
        });

        /**
         * Ĭ������£�RDD�е����ݲ���MySQL����һ��һ���Ĳ���ģ�Ҳ����˵����ÿ��Partition��iterator�е�ÿһ����¼��ÿһ��
         * ��Ҫ����һ�����ݿ�����ӣ�������ʹ��foreachRDD��ʱ������Ķ�����RD��Ȼ������ʹ��rdd��foreachPartition����ʱ����
         * �Ķ�����RDD��������һ��һ���ļ�¼��Ҳ����˵ÿ�ζ�ȡ��������Partition����ȡ���ݵ�ʱ��Ч�ʷǳ��ߣ�Ȼ�����ǲ���
         * ExecuteBatch�ķ���������߸������ݣ���ʱҲ�����ݿ���Ӹ�Ч�����Ӻ͸��·�ʽ������һ�ζ�ȡһ��Partition�ı׶���
         * �п����ڴ�OOM�����Դ�ʱ����Ҫ�ǳ���ע�ڴ��ʹ�á�
         */
        filteredClickInBatch.foreachRDD(new Function<JavaPairRDD<String, Long>, Void>() {
            @Override
            public Void call(JavaPairRDD<String, Long> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> partition) throws Exception {
                        /**
                         * ����������ʹ�����ݿ����ӳصĸ�Ч��д���ݿ�ķ�ʽ������д�����ݿ�MySQL��
                         * ���ڴ���Ĳ�����һ��Iterator���͵ļ��ϣ�����Ϊ�˸��Ӹ�Ч�Ĳ���������Ҫ
                         * �����������ݿ�Ĳ�����������˵һ���Բ���1000��Record��ʹ��insertBatch����
                         * updateBatch���͵Ĳ�����������û���Ϣ����ֻ������timestamp,ip,user,adID,province,city
                         * ������һ�����⣺���ܳ���������¼��Key��һ���ģ���˾���Ҫ�����ۼӲ���
                         */

                        List<UserAdClicked> userAdClickedList = new ArrayList<UserAdClicked>();//����������ݿ������

                        while(partition.hasNext()) {
                            Tuple2<String,Long> record = partition.next();
                            String[] splited = record._1().split("\t");

                            UserAdClicked userClicked = new UserAdClicked();
                            userClicked.setTimestamp(splited[0]);
                            userClicked.setIp(splited[1]);
                            userClicked.setUserID(splited[2]);
                            userClicked.setAdID(splited[3]);
                            userClicked.setProvince(splited[4]);
                            userClicked.setCity(splited[5]);
                            userAdClickedList.add(userClicked);
                        }
                        List<UserAdClicked> inserting = new ArrayList<UserAdClicked>();//������Ҫ�����
                        List<UserAdClicked> updating = new ArrayList<UserAdClicked>();//������Ҫ���µ�

                        JDBCWrapper jdbcWrapper = JDBCWrapper.getJDBCInstance();
                        //adclicked ����ֶΣ�timestamp,ip,user,adID,province,city,clickedCount
                        for(UserAdClicked clicked : userAdClickedList) {
                            jdbcWrapper.doQueryBatch("SELECT count(1) FROM adclicked WHERE "
                                    + "timestamp = ? AND userID = ? AND adID = ? ", new Object[]{
                                    clicked.getTimestamp(), clicked.getUserID(), clicked.getAdID()}, new ExecuteCallBack() {
                                @Override
                                public void resultCallBack(ResultSet result) throws Exception {
                                    if(result.next()){
                                        long count = result.getLong(1);
                                        clicked.setClickedCount(count);
                                        updating.add(clicked);
                                    } else {
                                        inserting.add(clicked);
                                    }
                                }
                            });
                        }

                        //�������
                        ArrayList<Object[]> insertParametersList = new ArrayList<Object[]>();
                        for(UserAdClicked insertRecord: inserting){
                            insertParametersList.add(new Object[]{
                                    insertRecord.getTimestamp(),
                                    insertRecord.getIp(),
                                    insertRecord.getUserID(),
                                    insertRecord.getAdID(),
                                    insertRecord.getProvince(),
                                    insertRecord.getCity(),
                                    insertRecord.getClickedCount()
                            });
                        }
                        jdbcWrapper.doBatch("INSERT INTO adclicked VALUES(?,?,?,?,?,?,?",insertParametersList);

                        //���²���
                        ArrayList<Object[]> updateParametersList = new ArrayList<Object[]>();
                        for(UserAdClicked updateRecord: updating){
                            updateParametersList.add(new Object[]{
                                    updateRecord.getClickedCount(),
                                    updateRecord.getTimestamp(),
                                    updateRecord.getIp(),
                                    updateRecord.getUserID(),
                                    updateRecord.getAdID(),
                                    updateRecord.getProvince(),
                                    updateRecord.getCity()
                            });
                        }
                        jdbcWrapper.doBatch("UPDATE adclicked set clickedCount = ? " +
                                "  WHERE timestamp = ? AND ip = ? AND userID = ? AND adID = ? " +
                                " AND province = ? AND city = ? ",updateParametersList);

                    }
                });
                return null;
            }
        });

        //���ι��ˣ�ͨ���ж��û������ۼƵ�����Ĵ����Ƿ����ĳ����ֵ������50�����û�������ĳ�����Ĵ��������ݱ���
        JavaPairDStream<String,Long> blackListBasedOnHistory = filteredClickInBatch.filter(new Function<Tuple2<String, Long>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, Long> v1) throws Exception {
                String[] splited = v1._1().split("\t");

                String data = splited[0];
                String userID = splited[2];
                String adId = splited[3];

                /**
                 * ����������data��userID��adID������ȥ��ѯ�û�����������ݱ�
                 * ����ܵĵ�����������ʱ����ڵ�������ж��Ƿ����ں����������
                 */
                int clickedCountTotalToday = 81;
                if(clickedCountTotalToday > 50) {
                    return true;//������
                } else {
                    return false;
                }
            }
        });

        //��һ����blackListBasedOnHistoryд�뵽���������ݱ���
        //filteredClickInBatch.foreachRDD�������ڲ�ʹ�� rdd.foreachPartition�������п���partition֮�����ͬһ�û����û��ظ���
        //������Ҫ����RDDȥ��
        //�������˼ҵ���˼������Ϊ���ԣ���Ϊǰ�������reduceByKey������Ϊ��������ӣ�
        // ��Ϊǰ���Key����userID��adID��ͬ�����ģ����Կ��Գ���ͬһ�û������ͬ�Ĺ��
        /**
         * ����Ժ�����������RDD����ȥ�ز���
         * ��ô���䣨blackListBasedOnHistory������ȥ�ز�����������transformֱ�Ӷ�rdd���в�����ʹ��rdd.distinct��ԭ�����
         * DStreamû�и÷���������RDD���и÷�����
         */
        JavaDStream<String> blackListuserIDBasedOnHistory = blackListBasedOnHistory.map(new Function<Tuple2<String, Long>, String>() {
            @Override
            public String call(Tuple2<String, Long> v1) throws Exception {
                return v1._1().split("]t")[2];
            }
        });
        JavaDStream<String> blackListUniqueuserIDBasedOnHistory = blackListuserIDBasedOnHistory.transform(new Function<JavaRDD<String>, JavaRDD<String>>() {
            @Override
            public JavaRDD<String> call(JavaRDD<String> rdd) throws Exception {
                return rdd.distinct();
            }
        });
        //��һ��д����������ݱ��У�������
        blackListUniqueuserIDBasedOnHistory.foreachRDD(new Function<JavaRDD<String>, Void>() {
            @Override
            public Void call(JavaRDD<String> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<String>>() {
                    @Override
                    public void call(Iterator<String> t) throws Exception {
                        /**
                         * ����������ʹ�����ݿ����ӳصĸ�Ч��д���ݿ�ķ�ʽ������д�����ݿ�MySQL��
                         * ���ڴ���Ĳ�����һ��Iterator���͵ļ��ϣ�����Ϊ�˸��Ӹ�Ч�Ĳ���������Ҫ
                         * �����������ݿ�Ĳ�����������˵һ���Բ���1000��Record��ʹ��insertBatch����
                         * updateBatch���͵Ĳ�����������û���Ϣ����ֻ������userID\
                         */
                        List<Object[]> blackList = new ArrayList<Object[]>();

                        while(t.hasNext()) {
                            blackList.add(new Object[]{(Object)t.next()});
                        }
                        JDBCWrapper jdbcWrapper =  JDBCWrapper.getJDBCInstance();
                        jdbcWrapper.doBatch("INSERT INTO blacklisttable VALUES (?)",blackList);

                    }
                });
                return null;
            }
        });


        /**
         * ������ۼƴ�����̬���£����Batch֮�䣩��ÿ��updateStateByKey������ Batch Duration��ʱ��
         * ����Ļ����Ͻ��й���������ĸ��£�����֮������һ�㶼��־û����ⲿ�洢�豸�ϣ����������Ǵ洢��
         * MySQL���ݿ��У�
         */
        JavaPairDStream<String,Long> updateStateByKeyDStream = filteradClickedStreaming.mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> t) throws Exception {
                String[] splited = t._2().split("\t");
                String timestamp = splited[0];//YYYY-MM-DD
                String adID = splited[3];
                String province = splited[4];
                String city = splited[5];

                String clickedRecord = timestamp + "_" + adID + "_" + province + "_" + city;
                return new Tuple2<String, Long>(clickedRecord, 1L);
            }
        }).updateStateByKey(new Function2<List<Long>, Optional<Long>, Optional<Long>>() {
            @Override
            public Optional<Long> call(List<Long> v1, Optional<Long> v2) throws Exception {
                /**
                 * v1:������ǵ�ǰ��key�ڵ�ǰ��Batch Duration�г��ֵĴ����ļ��ϡ�����{1,1,1}
                 * v2:����ǰkey����ǰ��Batch Duration�л��������Ľ����
                 */
                Long clickedTotalHistory = 0L;
                if (v2.isPresent()) {
                    clickedTotalHistory = v2.get();
                }
                for (Long one : v1) {
                    clickedTotalHistory += one;
                }
                return Optional.of(clickedTotalHistory);
            }
        });
        updateStateByKeyDStream.foreachRDD(new Function<JavaPairRDD<String, Long>, Void>() {
            @Override
            public Void call(JavaPairRDD<String, Long> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> partition) throws Exception {
                        List<AdClicked> adClickedList = new ArrayList<AdClicked>();

                        while (partition.hasNext()) {
                            Tuple2<String, Long> record = partition.next();
                            String[] splited = record._1().split("\t");

                            AdClicked adClicked = new AdClicked();
                            adClicked.setTimestamp(splited[0]);
                            adClicked.setAdID(splited[1]);
                            adClicked.setProvince(splited[2]);
                            adClicked.setCity(splited[3]);
                            adClicked.setClickedCount(record._2());

                            adClickedList.add(adClicked);
                        }

                        List<AdClicked> inserting = new ArrayList<AdClicked>();//������Ҫ�����
                        List<AdClicked> updating = new ArrayList<AdClicked>();//������Ҫ���µ�

                        JDBCWrapper jdbcWrapper = JDBCWrapper.getJDBCInstance();
                        for (AdClicked clicked : adClickedList) {
                            jdbcWrapper.doQueryBatch("SELECT count(1) FROM adclickedcount WHERE "
                                    + "timestamp = ? AND userID = ? AND adID = ?,And province = ? AND city = ? ", new Object[]{
                                    clicked.getTimestamp(), clicked.getTimestamp(), clicked.getAdID(), clicked.getProvince(), clicked.getCity()}, new ExecuteCallBack() {
                                @Override
                                public void resultCallBack(ResultSet result) throws Exception {
                                    if (result.next()) {
                                        long count = result.getLong(1);
                                        clicked.setClickedCount(count);
                                        updating.add(clicked);
                                    } else {
                                        inserting.add(clicked);
                                    }
                                }
                            });
                        }

                        //�������
                        ArrayList<Object[]> insertParametersList = new ArrayList<Object[]>();
                        for (AdClicked insertRecord : inserting) {
                            insertParametersList.add(new Object[]{
                                    insertRecord.getTimestamp(),
                                    insertRecord.getAdID(),
                                    insertRecord.getProvince(),
                                    insertRecord.getCity(),
                                    insertRecord.getClickedCount()
                            });
                        }
                        jdbcWrapper.doBatch("INSERT INTO adclickedcount VALUES(?,?,?,?,?", insertParametersList);

                        //���²���
                        ArrayList<Object[]> updateParametersList = new ArrayList<Object[]>();
                        for (AdClicked updateRecord : updating) {
                            updateParametersList.add(new Object[]{
                                    updateRecord.getClickedCount(),
                                    updateRecord.getTimestamp(),
                                    updateRecord.getAdID(),
                                    updateRecord.getProvince(),
                                    updateRecord.getCity()
                            });
                        }
                        jdbcWrapper.doBatch("UPDATE adclickedcount set clickedCount = ? " +
                                "  WHERE timestamp = ? AND adID = ? AND province = ? AND city = ?", updateParametersList);

                    }
                });
                return null;
            }
        });

        /**
         * �Թ��������TopN�ļ��㣬�����ÿ��ÿ��ʡ�ݵ�Top5�����Ĺ��
         * ��ΪҪ��RDD���в�������������ʹ����transform����
         * ��󱣴浽���ݿ���(��ɾ�������)
         */
        updateStateByKeyDStream.transform(new Function<JavaPairRDD<String, Long>, JavaRDD<Row>>() {
            @Override
            public JavaRDD<Row> call(JavaPairRDD<String, Long> rdd) throws Exception {
                JavaRDD<Row> rowRDD = rdd.mapToPair(new PairFunction<Tuple2<String,Long>, String, Long>() {
                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, Long> t) throws Exception {
                        String[] splited = t._1().split("_");
                        String timestamp = splited[0];
                        String adID = splited[1];
                        String province = splited[2];

                        String clickedRecord = timestamp + "_" + adID + "_" + province;
                        return new Tuple2<String, Long>(clickedRecord,t._2());
                    }
                }).reduceByKey(new Function2<Long, Long, Long>() {
                    @Override
                    public Long call(Long v1, Long v2) throws Exception {
                        return v1 + v2;
                    }
                }).map(new Function<Tuple2<String,Long>, Row>() {
                    @Override
                    public Row call(Tuple2<String, Long> v1) throws Exception {
                        String[] splited = v1._1().split("_");
                        String timestamp = splited[0];
                        String adID = splited[1];
                        String province = splited[2];
                        return RowFactory.create(timestamp,adID,province,v1._2());
                    }
                });

                StructType structType = DataTypes.createStructType(Arrays.asList(
                        DataTypes.createStructField("timestamp", DataTypes.StringType, true),
                        DataTypes.createStructField("adID", DataTypes.StringType, true),
                        DataTypes.createStructField("province", DataTypes.StringType, true),
                        DataTypes.createStructField("clickedCount", DataTypes.StringType, true)
                ));
                HiveContext hiveContext = new HiveContext(rdd.context());
                DataFrame df = hiveContext.createDataFrame(rowRDD,structType);
                df.registerTempTable("topNTableSource");
                DataFrame result = hiveContext.sql("SELECT timestamp,adID,province,clickedCount FROM" +
                        "(SELECT timestamp,adID,province,clickedCount,ROW_NUMBER OVER(PARTITION BY " +
                        " province ORDER BY clickedCount DESC) rank FROM topNTableSource) subquery " +
                        "WHERE rank <= 5 " );


                return result.toJavaRDD();
            }
        }).foreach(new Function<JavaRDD<Row>, Void>() {
            @Override
            public Void call(JavaRDD<Row> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Row>>() {
                    @Override
                    public void call(Iterator<Row> t) throws Exception {
                        List<AdProvinceTopN> adProvinceTopN = new ArrayList<AdProvinceTopN>();
                        while (t.hasNext()) {
                            Row row = t.next();
                            AdProvinceTopN item = new AdProvinceTopN();
                            item.setTimestamp(row.getString(0));
                            item.setAdID(row.getString(1));
                            item.setProvince(row.getString(2));
                            item.setClickedCount(row.getString(3));
                            adProvinceTopN.add(item);
                        }

                        JDBCWrapper jdbcWrapper = JDBCWrapper.getJDBCInstance();

                        //ȥ�ز��� ԭ�������deleteɾ��������where���������Ǹ���provinceɾ���ģ�topn����һ���ط���5�����
                        Set<String> set = new HashSet<String>();
                        for (AdProvinceTopN item : adProvinceTopN) {
                            set.add(item.getTimestamp() + "_" + item.getProvince());
                        }

                        ArrayList<Object[]> deleteParametersList = new ArrayList<Object[]>();
                        for (String deleteRecord : set) {
                            String[] splited = deleteRecord.split("_");
                            deleteParametersList.add(new Object[]{
                                    splited[0], splited[1]
                            });
                        }
                        jdbcWrapper.doBatch("DELETE FROM adprovincetopn WHERE timestamp = ? AND province = ?", deleteParametersList);

                        //�������
                        //adprovincetopn ������᣺timestamp,adIN,province,clickedCount
                        ArrayList<Object[]> insertParametersList = new ArrayList<Object[]>();
                        for (AdProvinceTopN updateRecord : adProvinceTopN) {
                            insertParametersList.add(new Object[]{
                                    updateRecord.getTimestamp(),
                                    updateRecord.getAdID(),
                                    updateRecord.getProvince(),
                                    updateRecord.getProvince()
                            });
                        }
                        jdbcWrapper.doBatch("INSERT INTO adprovincetopn VALUES(?,?,?,?)", insertParametersList);

                    }
                });
                return null;
            }
        });

        /**
         * ����30���ӹ��ĵ������
         * Ȼ������ݷŵ�DB�У�Ȼ��ͨ����������������ͼ
         * filteradClickedStreaming������kafka�����ȡ�����ݣ�keyû�����壬value����Ҫ������
         */
        filteradClickedStreaming.mapToPair(new PairFunction<Tuple2<String,String>, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> t) throws Exception {
                String[] splited = t._2().split("\t");
                String adID = splited[3];
                String time = splited[0];//Todo:������Ҫ�ع�����ʵ��ʱ����ͷ��ӵ�ת����ȡ���˴���Ҫ��ȡ���ù��ķ��ӵ�λ
                return new Tuple2<String, Long>(time + "_" + adID,1L);
            }
        }).reduceByKeyAndWindow(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        }, new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 - v2;
            }
        }, Durations.minutes(30), Durations.minutes(5)).foreachRDD(new Function<JavaPairRDD<String, Long>, Void>() {
            @Override
            public Void call(JavaPairRDD<String, Long> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> partition) throws Exception {
                        List<AdTrendStat> adTrend = new ArrayList<AdTrendStat>();
                        while(partition.hasNext()){
                            Tuple2<String,Long> record = partition.next();
                            String[] splited = record._1().split("_");
                            String time = splited[0];
                            String adID = splited[1];
                            Long ClickedCount = record._2();

                            /**
                             * �ڲ������ݵ����ݿ��ʱ�������Ҫ��Щ�ֶΣ�time��adID��ClickedCount
                             * ������ͨ��J2EE�����������ƻ�ͼ��ʱ��϶�����Ҫ�ꡢ�¡��ա�ʱ�������ά�ȵģ�����
                             * ������������Ҫ�ꡢ�¡��ա�ʱ������Щʱ��ά�ȣ�
                             */

                            AdTrendStat adtrendStat = new AdTrendStat();
                            adtrendStat.setAdID(adID);
                            adtrendStat.setClickedCount(ClickedCount);
                            adtrendStat.set_data(time);//Todo:��ȡ������
                            adtrendStat.set_hour(time);//Todo:��ȡСʱ
                            adtrendStat.set_minute(time);//TOdo��ȡ����

                            adTrend.add(adtrendStat);
                        }

                        List<AdTrendStat> inserting = new ArrayList<AdTrendStat>();//������Ҫ�����
                        List<AdTrendStat> updating = new ArrayList<AdTrendStat>();//������Ҫ���µ�

                        JDBCWrapper jdbcWrapper = JDBCWrapper.getJDBCInstance();
                        for (AdTrendStat clicked : adTrend) {
                            AdTrendCountHistory adTrendCountHistory = new AdTrendCountHistory();
                            jdbcWrapper.doQueryBatch("SELECT count(1) FROM adclickedtrend WHERE "
                                    + "date = ? AND hour = ? AND minute = ?,And adID = ?  ", new Object[]{
                                    clicked.get_data(), clicked.get_hour(), clicked.get_minute(), clicked.getAdID()}, new ExecuteCallBack() {
                                @Override
                                public void resultCallBack(ResultSet result) throws Exception {
                                    if (result.next()) {
                                        long count = result.getLong(1);
                                        adTrendCountHistory.setClickedCountHistory(count);
                                        updating.add(clicked);
                                    } else {
                                        inserting.add(clicked);
                                    }
                                }
                            });
                        }

                        //�������
                        ArrayList<Object[]> insertParametersList = new ArrayList<Object[]>();
                        for (AdTrendStat insertRecord : inserting) {
                            insertParametersList.add(new Object[]{
                                    insertRecord.get_data(),
                                    insertRecord.get_hour(),
                                    insertRecord.get_minute(),
                                    insertRecord.getClickedCount(),
                            });
                        }
                        //adclickedtrend�ֶΣ�data,minute,adID,clickedCount
                        jdbcWrapper.doBatch("INSERT INTO adclickedtrend VALUES(?,?,?,?", insertParametersList);

                        //���²���:��Ϊ�Է���Ϊ��λ�ģ�������������10���ӣ������п�����1��������ͬ�Ĺ��������
                        ArrayList<Object[]> updateParametersList = new ArrayList<Object[]>();
                        for (AdTrendStat updateRecord : updating) {
                            updateParametersList.add(new Object[]{
                                    updateRecord.getClickedCount(),
                                    updateRecord.get_data(),
                                    updateRecord.get_hour(),
                                    updateRecord.get_minute(),
                                    updateRecord.getAdID()

                            });
                        }
                        jdbcWrapper.doBatch("UPDATE adclickedtrend set clickedCount = ? " +
                                "  WHERE date = ? AND hour = ? AND minute = ?,And adID = ?", updateParametersList);
                    }
                });
                return null;
            }
        });
        jsc.start();
        jsc.awaitTermination();
        jsc.close();
    }
}
class JDBCWrapper {

    private static JDBCWrapper jdbcInstance = null;
    private static LinkedBlockingQueue<Connection> dbConnectionPool = new LinkedBlockingQueue<Connection>();

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  static JDBCWrapper getJDBCInstance() {
        if(jdbcInstance == null) {
            synchronized (JDBCWrapper.class) {
                if(jdbcInstance == null) {
                    jdbcInstance = new JDBCWrapper();
                }
            }
        }
        return jdbcInstance;
    }

    private JDBCWrapper() {
        for (int i = 0; i < 10; i++) {
            try {
                Connection conn = DriverManager.getConnection("jdbc://mysql://master:3306/sparkstreaming", "root", "root");
                dbConnectionPool.put(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized  Connection getConnection(){
        while (0 == dbConnectionPool.size()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return dbConnectionPool.poll();
    }

    public int[] doBatch(String sqlText,List<Object[]> paramsList) {
        Connection conn = getConnection();
        PreparedStatement preparedStatement = null;
        int[] result = null;
        try {
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(sqlText);
            for(Object[] parameters : paramsList) {
                for(int i = 0; i < parameters.length; i++) {
                    preparedStatement.setObject(i+1,parameters[i]);
                }
                preparedStatement.addBatch();
            }
            result = preparedStatement.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    dbConnectionPool.put(conn);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public void doQueryBatch(String sqlText,Object[] paramsList,ExecuteCallBack callBack) {
        Connection conn = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            preparedStatement = conn.prepareStatement(sqlText);
                for(int i = 0; i < paramsList.length; i++) {
                    preparedStatement.setObject(i+1,paramsList[i]);
                }
            result = preparedStatement.executeQuery();
            try {
                callBack.resultCallBack(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    dbConnectionPool.put(conn);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

//doBatchQuery�����Ļص������Ľӿ�
interface ExecuteCallBack {
    void resultCallBack(ResultSet result) throws Exception;
}

/**
 * javaBean:��adclicked����ֶν��з�װ
 */
class UserAdClicked{
    private String timestamp;
    private String ip;
    private String userID;
    private String adID;
    private String province;
    private String city;

    public Long getClickedCount() {
        return clickedCount;
    }

    public void setClickedCount(Long clickedCount) {
        this.clickedCount = clickedCount;
    }

    private Long clickedCount;




    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}

class AdClicked {
    private String timestamp;
    private String adID;
    private String province;
    private String city;
    private Long clickedCount;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getClickedCount() {
        return clickedCount;
    }

    public void setClickedCount(Long clickedCount) {
        this.clickedCount = clickedCount;
    }
}

class AdProvinceTopN {
    private String timestamp;
    private String adID;
    private String province;
    private String clickedCount;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getClickedCount() {
        return clickedCount;
    }

    public void setClickedCount(String clickedCount) {
        this.clickedCount = clickedCount;
    }
}

class AdTrendStat{
    private String _data;
    private String _hour;
    private String _minute;
    private String adID;
    private Long clickedCount;

    public String get_data() {
        return _data;
    }

    public void set_data(String _data) {
        this._data = _data;
    }

    public String get_hour() {
        return _hour;
    }

    public void set_hour(String _hour) {
        this._hour = _hour;
    }

    public String get_minute() {
        return _minute;
    }

    public void set_minute(String _minute) {
        this._minute = _minute;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public Long getClickedCount() {
        return clickedCount;
    }

    public void setClickedCount(Long clickedCount) {
        this.clickedCount = clickedCount;
    }
}

class AdTrendCountHistory {


    private Long clickedCountHistory;

    public Long getClickedCountHistory() {
        return clickedCountHistory;
    }

    public void setClickedCountHistory(Long clickedCountHistory) {
        this.clickedCountHistory = clickedCountHistory;
    }

}

