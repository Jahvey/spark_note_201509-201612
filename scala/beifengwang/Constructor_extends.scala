package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 * Scala�У�ÿ���������һ����constructor������������constructor����ÿ������constructor�ĵ�һ�ж������ǵ���
 * ��������constructor������constructor���������ĸ���constructor��һ��������ֱ�ӵ��ø����constructor��
 * ֻҪ���������constructor�е��ø����constructor��һ�������﷨������ͨ������������캯�������ø���Ĺ��캯��
 * ע�⣡����Ǹ����н��ܵĲ���������name��age�������н���ʱ���Ͳ�Ҫ���κ�val����var�������ˣ��������Ϊ������Ҫ
 * ���Ǹ����field��������Ǹ����н��ܵĲ���������score������Ҫ��val��var�����Σ���������Σ������Ϊ�ǹ�������
 * һ�������������ٱ���ʱ����ɶ�����ֶΡ�
 */
object Constructor_extends {

  def main(args: Array[String]) {
    val b1 = new B1("guoyuan")
    println(b1.score + " " + b1.name)
  }
}
class A1(val name:String,val age:Int)
class B1(name:String,age:Int,var score:Double) extends A1(name,age) {
  def this(name:String) {
    this(name,0,0)
  }
  def this(age:Int){
    this("leo",age,0)
  }
}
