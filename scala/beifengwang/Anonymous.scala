package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 * �������࣬Ҳ����˵�����Զ���һ�����û�����ֵ����࣬��ֱ�Ӵ��������Ȼ�󽫶��������
 * ����һ��������֮���������Խ���������Ķ��󴫵ݸ�����������
 */
object Anonymous {

  def main(args: Array[String]) {
    val p = new A2("guoyuan") {
      override def sayHello = "Hi,i'm "+ name
    }
    greeting(p)
  }
  def greeting(p:A2{def sayHello:String}): Unit = {
    println(p.sayHello)
  }

}
class A2(protected val name:String) {
  def sayHello = "Hello,i'm " + name
}
