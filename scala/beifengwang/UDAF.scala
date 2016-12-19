package scala.beifengwang

import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2016/8/16.
 * UDF,��ʵ���������Ե������룬����һ�����
 * UDAF���������Զ������룬���оۺϼ��㣬����һ����������ܸ�ǿ��
  */
object UDAF {

   def main(args: Array[String]) {
     val conf = new SparkConf().setAppName("UDAF").setMaster("local")
     val sc = new SparkContext(conf)
     val sqlContext = new SQLContext(sc)

     val names = Array("leo","Marry","Jack","Tom","leo","Marry","Jack","Tom","leo","Marry","Jack","Tom")
     val namesRDD = sc.parallelize(names,3)
     val namesRowRDD = namesRDD.map(name => Row(name))
     val structType = StructType(Array(StructField("name",DataTypes.StringType,true)))
     val namesDF = sqlContext.createDataFrame(namesRowRDD,structType)

     namesDF.registerTempTable("names")

     sqlContext.udf.register("strCount",new StringCount)

     sqlContext.sql("select name,strCount(name) from names group by name").collect().foreach(println)

   }
 }
