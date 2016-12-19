package scala.SparkSQL

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/4/5.
 */

/**
 * ʹ��Spark SQL�е����ú��������ݽ��з�����Spark SQL API��ͬ���ǣ�DataFrame�е����ú��������Ľ�����ص���һ��Column����
 *
 * ���������ͣ�
 * 1���ۺϺ���������countDistinct��sumDistinct��
 * 2�����Ϻ���������sort_array,explode��
 * 3�����ڣ�ʱ�亯��������hour,quarter,next_day
 * 4����ѧ����������asin��atan��sqrt��tan��round��
 * 5����������������rawNumber��
 * 6���ַ���������concat��format_number,rexexp_extract
 * 7������������isNaN��sha��randn��callUDF
 */
object SparkSQLAgg {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("SparkSQLAgg").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val userData = Array(
      "2016-3-27,001,http://spark.apache.arg/",
      "2016-3-27,001,http://hadoop.apache.arg/",
      "2016-3-27,002,http://flink.apache.arg/",
      "2016-3-28,003,http://kafka.apache.arg/",
      "2016-3-28,004,http://spark.apache.arg/",
      "2016-3-28,002,http://hive.apache.arg/",
      "2016-3-28,001,http://parquet.apache.arg/",
      "2016-3-28,001,http://spark.apache.arg/"
    )
    val userDataRDD = sc.parallelize(userData)

    /**
     * �����ݽ���Ԥ��������DataFrame��Ҫ���RDDת����DataFrame����Ҫ�Ȱ�RDD�е�Ԫ�����ͱ��Row���ͣ�
     * ���ͬʱҪ�ṩDataFrame�е�Colums��Ԫ����������
     */

    val userDataRDDRow = userDataRDD.map(row => {val splited = row.split(",");Row(splited(0),splited(1).toInt,splited(2))})
    val structTypes = StructType(Array(
      StructField("time",DataTypes.StringType,true),
      StructField("id",DataTypes.IntegerType,true),
      StructField("url",DataTypes.StringType,true)
    ))

    val  userDataDF = sqlContext.createDataFrame(userDataRDDRow,structTypes)

    /**
     * ʹ��Spark SQl�ṩ�����ú�����DataFra���в������ر�ע�⣬���ú������ɵ�Column�������Զ�����CG
     */
    import org.apache.spark.sql.functions._
    import sqlContext.implicits._ //Ҫʹ��Spark SQL�����ú�����һ��Ҫ����SQLContext�µ���ʿת��
    userDataDF.groupBy("time").agg('time,countDistinct('id)).show()




































  }
}
