package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * ��trait�У����Ի��ʹ�þ��巽���ͳ��󷽷�
 * �����þ��巽�������ڳ��󷽷��������󷽷���ŵ��̳�trait������ȥʵ��
 * ����trait��ʵ�������ģʽ�е�ģ�����ģʽ������
 */
object Trait_8 {

  def main(args: Array[String]) {
    val a = new P9("guoyuan")
  }
}
trait Valid {
  def getName:String
  def valid:Boolean = {
    getName == "Guoyuan"
  }
}
class P9(val name:String) extends Valid {
  println(valid)
  def getName = name
}
