package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 */
object Array_ {

  def main(args: Array[String]) {

    val a = Array(1,2,3,4,5)
    println("sum: " + a.sum)
    println("max: " + a.max)

    //�������������
    scala.util.Sorting.quickSort(a)
    //��ȡ����������Ԫ������
    println(a.mkString)
    println(a.mkString(","))
    println(a.mkString("<",",",">"))
    //toString
    println(a.toString)
  }

}
