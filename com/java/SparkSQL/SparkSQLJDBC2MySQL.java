package com.java.SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/4/3.
 */
public class SparkSQLJDBC2MySQL {

    public static void main(String [] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("SparkSQLJDBC2MySQL");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        /**
         * 1,ͨ��format("jdbc")�ķ�ʽ˵��sparkSQL������������Դ��ͨ��JDBC��õġ�JDBC���һ�㶼�����ݿ⣬����
         * MySQL��Oracle��
         * 2,ͨ��DataFrameReader��option������Ҫ���ʵ����ݿ����Ϣ���ݽ�ȥ��
         *      url���������ݿ��jdbc���ӵ�ַ��
         *      dbtable������Ҫ����ʹ���ĸ����ݿ�
         * 3��Driver������Spark SQL�������ݿ�ľ��������������������������
         * 4������JDBC��������Jar�����Է���Spark��libraryĿ¼��Ҳ������ʹ��SparkSubmit��ʱ��ָ�������jar��
         *       ����ʹ����ʱ�򶼲���Ҫ���JDBC��Jar��
         */
        DataFrameReader reader = sqlContext.read().format("jdbc");
        reader.option("url","jdbc:mysql://master:3306/spark");
        reader.option("btable","modmo");
        reader.option("driver","com.mysql.jdbc.Driver");
        reader.option("user","root");
        reader.option("password", "root");
        /**
         * ��ʵ�ʵ���ҵ�����������У�������������ݹ�ģ�ر������10�������ݣ���ʱ���ô�ͳ��DBȥ����Ļ���
         * һ����Ҫ��10�������ݷֳ�������δ�������ֳ� 100���������ڵ�̨Server�Ĵ���������,��ʵ�ʵĴ������
         * ���ܻ�ǳ����ӣ�ͨ����ͳ��Java EE�ĸ�������Ժ��ѻ��߲�����ʵ�ִ����㷨����ʱ����Spark SQL������ݿ��е�
         * ���ݲ����зֲ�ʽ����Ϳ��Էǳ��õĽ�������⣬��������Spark SQL����DB�е�������Ҫʱ�䣬����һ�����
         * Spark SQL�;���Ҫ������DB֮�����һ�������Σ�redis,tachyon��;�����м�ʹ��Redis�����԰�Spark�����ٶ���ߵ�
         * ����45����
         */
        DataFrame momoDataSourceDFFrameFromMySQL = reader.load();//����momo����DataFram
        momoDataSourceDFFrameFromMySQL.show();
        momoDataSourceDFFrameFromMySQL.registerTempTable("test1");
        sqlContext.sql("select name from test1").show();


        reader.option("dbtable", "qianqian");
        DataFrame qianqianDataSourceDFFrameFromMySQL = reader.load();//����qianqian����DataFram
        qianqianDataSourceDFFrameFromMySQL.show();
        qianqianDataSourceDFFrameFromMySQL.registerTempTable("test2");
        sqlContext.sql("select age from test2").show();


        sqlContext.sql("select name,age from test1 a  JOIN test2 b ON a.id = b.id").show();

        DataFrame personDF = sqlContext.sql("select name,age from test1 a  JOIN test2 b ON a.id = b.id");
        /**
         * 1,��DataFrameҪ��ͨ��SparkSQL��cora��ML�ȸ��Ӳ����������д�����ݿ��ʱ��������Ȩ�޵����⣬ȷ�����ݿ�
         * ��Ȩ�˵�ǰ����SparkSQL���û���
         * 2��DataFrameҪд���ݵ�DB��ʱ��һ�㶼������ֱ��д��ȥ������Ҫת��RDD��ͨ��RDDд���ݵ�DB�С�
         */
        System.out.println("*************************");
        personDF.javaRDD().foreachPartition(new VoidFunction<Iterator<Row>>() {
            @Override
            public void call(Iterator<Row> rowIterator) throws Exception {
                Connection conn2MySQL = DriverManager.getConnection("jdbc:mysql://master:3306/spark", "root", "root");
                Statement statement = conn2MySQL.createStatement();
                while (rowIterator.hasNext()) {
                    StringBuilder sqlText = new StringBuilder();
                    sqlText.append("Insert into hebing(name,age) values( ");
                    Row row = rowIterator.next();
                    sqlText.append("'" + row.getAs("name").toString() + "'" + ",");
                    sqlText.append(row.getAs("age").toString()+")");
                    statement.execute(sqlText.toString());
                }
            }
        });































    }

}
