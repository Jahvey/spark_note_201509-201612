package scala.beifengwang

/**
 * Created by Administrator on 2016/7/30.
 */
object Break {
  /**
   * scalaû���ṩ������java��break��䡣
   * ���ǿ���ʹ��boolean���ͱ�����return����Breaks��break����������ʹ��
   * @param args
   */
  def main(args: Array[String]) {
    var n:Int = 10
    for(c <- "hello word") {
      if (5 == n)
        scala.util.control.Breaks.break
      else {
        n -= 1
        println(c)
      }
    }
  }

}
