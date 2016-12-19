package com.java.SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class RDD2DataFrameByReflection {
    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("RDD2DataFrameByFlection").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        JavaRDD<String> lines = sc.textFile("E:/softwares/spark-1.6.0/examples/src/main/resources/person.txt");
        JavaRDD<Person> persons = lines.map(new Function<String, Person>() {
            @Override
            public Person call(String line) throws Exception {
                String[] splited = line.split("\t");
                Person p = new Person();
                p.setId(Integer.valueOf(splited[0]));
                p.setName(splited[1]);
                p.setAge(Integer.valueOf(splited[2]));
                return p;
            }
        });
        //RDDת����DataFrame
        //�ڵײ�ͨ������ķ�ʽ���Person������fields�����RDD������������DataFrame
        DataFrame df = sqlContext.createDataFrame(persons, Person.class);//��һ����RDD���ڶ����Ƕ�RDD��������
        df.show();

        df.registerTempTable("Person");
        DataFrame bigData = sqlContext.sql("select * from Person");

        //DataFrameת����RDD
        JavaRDD<Row> bigDataRDD = bigData.javaRDD();
        //sqlContext.createDataFrame(bigDataRDD, Person.class);//��һ��ת����ȥ
        JavaRDD<Person> result = bigDataRDD.map(new Function<Row, Person>() {
            @Override
            public Person call(Row row) throws Exception {
                Person p = new Person();
                p.setId(row.getAs("id"));
                p.setName(row.getAs("name"));
                p.setAge(row.getAs("age"));
                return p;
            }
        });
        List<Person> personList = result.collect();
        for(Person p : personList) {
            System.out.println(p.toString());
        }


    }
}
