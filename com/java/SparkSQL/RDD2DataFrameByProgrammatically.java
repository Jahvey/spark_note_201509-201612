package com.java.SparkSQL;

import org.apache.calcite.avatica.ColumnMetaData;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class RDD2DataFrameByProgrammatically {

    public static void main(String [] args) {

        SparkConf conf = new SparkConf().setMaster("local").setAppName("RDD2DataFrameByProgrammatically");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        JavaRDD<String> lines = sc.textFile("E:/softwares/spark-1.6.0/examples/src/main/resources/person.txt");
        /**
         * ��RDD�Ļ����ϴ�������ΪRow��RDD
         */
        JavaRDD perpsonsRDD = lines.map(new Function<String, Row>() {
            @Override
            public Row call(String lines) throws Exception {
                String[] splited = lines.split("\t");
                return RowFactory.create(Integer.valueOf(splited[0]), splited[1], Integer.valueOf(splited[2]));
            }
        });

        /**
         * ��̬����DateFrame��Ԫ����
         */
        List<StructField>  structField = new ArrayList<StructField>();
        structField.add(DataTypes.createStructField("id", DataTypes.IntegerType, true));
        structField.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        structField.add(DataTypes.createStructField("age", DataTypes.IntegerType, true));
        //����StructType���������DataFrameԪ���ݵ�����
        StructType structType = DataTypes.createStructType(structField);
        /**
         * �������е�MetaData�Լ�RDD<ROW>������DataFrame
         */
        DataFrame personsDF = sqlContext.createDataFrame(perpsonsRDD,structType);

        /**
         * ע���Ϊ��ʱ��
         */
        personsDF.registerTempTable("persons");
        /**
         * �������ݵĶ�ά�ȷ���
         */
        DataFrame result = sqlContext.sql("select * from persons");

        result.show();

        /**
         * DataFrameת����ΪRDD
         */
        List<Row>  listRow = result.javaRDD().collect();

        for(Row row : listRow) {
            System.out.println(row.getAs("id").toString());
            System.out.println(row);
        }

    }
}
