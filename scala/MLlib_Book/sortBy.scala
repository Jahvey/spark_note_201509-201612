package scala.MLlib_Book

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/4/11.
 */
/**
 * sortBy������Ҫ��3����������һ��Ϊ���뷽�������ڼ������������
 * �ڶ�����ָ�������ֵ�������ǽ�����ʾ��
 * �������Ƿ�Ƭ��������
 */
object sortBy {

  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local").setAppName("Cartesian")
    val sc = new SparkContext(conf)
    val arr = sc.parallelize(Array((5,"b"),(6,"a"),(1,"f"),(3,"d"),(4,"c"),(2,"e")))
    val str1 = arr.sortBy(word => word._1,true) //���յ�һ ����������
    val str2 = arr.sortBy(word => word._2,true) //���յ�һ����������
    str1.foreach(println)
    str2.foreach(println)
  }
}
