package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * Scala�е�Trait���Զ������field����trait�еľ��巽������Ի��ڳ���field����д
 * ���Ǽ̳�trait���࣬����븲�ǳ���field���ṩ�����ֵ
 */
object Trait_4 {

  def main(args: Array[String]) {
    val a = new P5("guo")
    val b = new P5("yuan")
    a.makeFriends(b)
  }
}

trait SayHello {
  val msg:String
  def sayHello(name:String) = println(msg + "," + name)
}
class P5(val name:String) extends SayHello {
  val msg:String = "hello"
  def makeFriends(p:P5): Unit ={
    sayHello(p.name)
    println("i'm " + name + " , I want to make friends with you!")
  }
}
