package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 * object�Ĺ�����ʵ��class���ƣ����˲��ܶ�����ܲ�����constructor֮��
 * objectҲ���Լ̳г����࣬�����ǳ�����ķ���
 */
object ObjectAndAbstract {

  def main(args: Array[String]) {
    HelloImpl.sayHello("guoyuan")
  }
}
abstract  class Hello(var message:String) {
  def sayHello(name:String):Unit
}
object HelloImpl extends Hello("hello") {
  override def sayHello(name:String) = {
    println(message + "," + name)
  }
}
