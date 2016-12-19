package scala.MLlib_Book

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/4/11.
 */
/**
 * reduce �������Դ���һ���Ѷ���ķ�����Ϊ���ݴ�������һ����ʾ��һ��Ѱ��
 * ��ַ�����һ�δ���
 */
object reduce {

  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local").setAppName("Cartesian")
    val sc = new SparkContext(conf)
    val arr = sc.parallelize(Array("one", "two", "three", "four", "five"))
    val result = arr.reduce(myFun)
    result.foreach(print)
  }
  def myFun(str1:String,str2:String): String = {
    var str = str1;
    if(str2.size >= str.size) {
      str = str2
    }
    return str
  }

}
