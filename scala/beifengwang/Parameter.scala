package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 */
object Parameter {

  def main(args: Array[String]) {
    parameters("df",10) //˵���˱䳤��������ŵ������������
  }

  def parameters (a:String = "dd",b:Int)= {
    println(a + " " + b)
  }
}
