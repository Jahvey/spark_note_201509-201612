package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * ��Scala�У�traitҲ���Լ̳���class����ʱ���class�ͻ��Ϊ���м̳и�trait����ĸ���
 */
object Trait_11 {

  def main(args: Array[String]) {

    val a = new P15("GuoYuan")
    a.sayHello
  }
}
class MyUtil {
  def printlnMessage(msg:String) = println(msg)
}
trait Logger_11 extends MyUtil{
  def log(msg:String) = printlnMessage("log: " + msg)
}
class P15(val name:String) extends Logger {
  def sayHello {
    log("Hi,i'm " + name)
    println("Hi,i'm " + name)
  }
}