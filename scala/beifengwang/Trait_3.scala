package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * Scala�е�Trait���Զ������field����ʱ�̳�trait������Զ������trait�ж����field
 * �������ֻ�ȡfield�ķ�ʽ��̳�class�ǲ�ͬ�ģ�����Ǽ̳�class��ȡ��field��ʵ���Ƕ����ڸ����У����������һ�����ã�
 * ���̳�trait��ȡ��field����ֱ�ӱ���ӵ�������
 */
object Trait_3 {

  def main(args: Array[String]) {

    val a = new S4("guoyuan")
    a.sayHello
  }
}

trait P4{
  val eyeNum:Int = 2
}
class S4(val name:String) extends P4{
  def sayHello = println("Hi,i'm " + name + " , i have " + eyeNum + " eyes.")
}

