package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 * Scala�е�Trait���Բ���ֻ������󷽷��������Զ�����巽������ʱtrait�����ǰ�����ͨ�ù��߷����Ķ���
 * ��һ��ר�õ������������������������˵trait�Ĺ��ܻ�������
 * ������˵��trait�п��԰���һЩ�ܶ��඼ͨ�õĹ��ܷ����������ӡ��־�ȵȣ�spark�о�ʹ����trait��������ͨ�õ���־��ӡ����
 */
object Trait_2 {
  def main(args: Array[String]) {

    val a = new P3("guo")
    val b = new P3("yuan")
    a.makeFriends(b)
  }
}
trait Logger {
  def log(message:String) = println(message)
}
class P3(val name:String) extends Logger {
  def makeFriends(p:P3): Unit ={
    println("Hi,i'm " + name + " , i'm glad to make friends with you," + p.name)
    log("makeFriends method is invoked with parameter Person[name=" + p.name + "]")
  }
}