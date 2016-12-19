package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * Scala��֧������̳ж��trait��һ�ε��ö��trait�е�ͬһ��������ֻҪ�ö��trait��ͬһ�������У������ִ��super.��������
 * ���е��ö��trait�ж��еķ���ʱ�����Ȼ�����ұߵ�trait�ķ�����ʼִ�У�Ȼ����������ִ�У��γ�һ����������
 * �������Էǳ�ǿ����ʵ���൱�����ģʽ�е�������ģʽ��һ�־���ʵ������
 */
object Trait_6 {

  def main(args: Array[String]) {

    val a = new P7("Guoyuan")
    a.sayHello
  }
}
trait Handler {
  def handle(data:String){}
}
trait DataValidHandler extends Handler {
  override def handle(data:String): Unit = {
    println("check data : " + data)
    super.handle(data)
  }
}
trait SignatureValidHandler extends Handler {
  override def handle(data:String): Unit = {
    println("check signature: " + data)
    super.handle(data)
  }
}
class P7(val name:String) extends SignatureValidHandler with DataValidHandler {
  def sayHello = {
    println("Hello, " + name)
    handle((name))
  }
}