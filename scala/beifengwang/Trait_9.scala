package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * trait�Ĺ������
 * ��Scala�У�traitҲ���й������ģ�Ҳ����trait�еģ����������κη����еĴ���
 * ���̳���trait����Ĺ���������£�
 * 1������Ĺ��캯��ִ�У�
 * 2��trait�Ĺ������ִ�У����trait����������ִ�У�
 * 3������traitʱ���ȹ��츸trait��������trait�̳�ͬһ����trait����traitֻ�ṹ�����Σ�
 * 4������trait�������֮������Ĺ��캯��ִ��
 */
object Trait_9 {

  def main(args: Array[String]) {
    val a = new S10
  }

}
class P10{println("P10's constructor!")}
trait Logger_10{println("Logger_10's constructor")}
trait MyLogger_10 extends Logger_10 {println("MyLogger's constructor")}
trait TimeLogger_10 extends Logger_10 {println("TimeLogger_10's constructor")}
class S10 extends P10 with MyLogger_10 with TimeLogger_10 {
  println("S10's constructor")
}

