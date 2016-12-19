package scala.MLlib_Book

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/4/11.
 */
/**
 * keyBy������Ϊ���ݼ��е�ÿ��������������һ��key���Ӷ�������ԭ���ĸ��������γɼ�ֵ��
 */
object keyBy {

  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local").setAppName("Cartesian")
    val sc = new SparkContext(conf)
    val arr = sc.parallelize(Array("one","two","three","four","five"))
    val arr2 = arr.keyBy(word => word.size)
    arr2.foreach(println)
    val arr3 = arr.keyBy(word => 1)
    arr3.foreach(println)
  }

}
