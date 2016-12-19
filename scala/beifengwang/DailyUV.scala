package scala.beifengwang

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.functions._

/**
 * Created by Administrator on 2016/8/15.
 */
object DailyUV {

  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local").setAppName("DailyUV")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    sc.setLogLevel("OFF")

    //�����û�������־���ݣ�������DataFrame
    val userAccessLog = Array(
      "2015-10-01,1122",
      "2015-10-01,1122",
      "2015-10-01,1123",
      "2015-10-01,1124",
      "2015-10-01,1124",
      "2015-10-02,1122",
      "2015-10-02,1121",
      "2015-10-02,1123",
      "2015-10-02,1123"
    )
    val userAccessLogRDD = sc.parallelize(userAccessLog,5)

    //ת��ΪDF
    //RDD -> RDD<Row>
    val userAccessLogRowRDD = userAccessLogRDD
      .map(log => Row(log.split(",")(0),log.split(",")(1).toInt))
    val structType = StructType(Array(
      StructField("date",DataTypes.StringType,true),
      StructField("userid",DataTypes.IntegerType,true)
    ))
    val userAccessLogRowDF = sqlContext.createDataFrame(userAccessLogRowRDD,structType)

    //UV:ÿ�춼�кܶ��û������ʣ�����ÿ���û�����ÿ�춼����ʺܶ�Σ�UVָ���Ƕ��û�ȥ���Ժ�ķ�������
    //���ú�����Ҫ������ʿת��
    import sqlContext.implicits._
    //�ۺϺ������÷���
    //���ȣ���DataFrame����groupBy()��������ĳһ�н��з���
    //Ȼ�󣬵���agg()��������һ�����������룬���룬����֮ǰ��groupBy()�����г��ֵ��ֶ�
    //�ڶ�������������countDistinct��sum��filter�ȣ�Spark�ṩ�����ú���
    //���ú����У�����Ĳ�����Ҳ���õ�������Ϊǰ׺���������ֶ�
    userAccessLogRowDF.groupBy("date").agg('date,countDistinct('userid)).map(row => Row(row(1),row(2))).collect().foreach(println)
























  }

}
