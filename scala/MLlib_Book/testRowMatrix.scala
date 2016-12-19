package scala.MLlib_Book

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/4/11.
 */
/**
 * һ����˵�����÷ֲ�ʽ������д洢����������������ǳ���ģ��䴦���ٶȺ�Ч������洢��ʽϢϢ��ء�
 * MLlib�ṩ�����ֲַ�ʽ����洢��ʽ������֧�ֳ����ε���������˫���ȸ����͵��������ݹ��ɡ�
 * �����־���ֱ�Ϊ���о��󣬴����������о����������Ϳ����
 */

/**
 * �о������������һ�־������͡��о�����������Ϊ��������ľ���洢��ʽ���е�������Խ�С��
 * ���Խ������Ϊ�о�����һ���޴�����������ļ��ϡ�ÿһ�о���һ��������ͬ��ʽ���������ݣ�
 * ��ÿһ�е��������ݶ����Ե���ȡ�������в�����
 */
object testRowMatrix {

  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local").setAppName("Cartesian")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(Array("1 2 3","4 5 6"))
      .map(_.split(" ").map(_.toDouble))
      .map(line => Vectors.dense(line))
    val rm = new RowMatrix(rdd)
    println(rm.numRows())
    println(rm.numCols())
    rm.rows.foreach(println)
  }

}
