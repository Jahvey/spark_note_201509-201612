package scala.MLlib_Book

import org.apache.spark.mllib.linalg.{Vectors, Matrices}

/**
 * Created by Administrator on 2016/4/11.
 */
/**
 * ���ؾ���
 */
object testMatrix {

  def main(args: Array[String]) {
    val mx = Matrices.dense(2,3,Array(1,2,3,4,5,6))//����һ�����ؾ���
    println(mx)
  }

}
