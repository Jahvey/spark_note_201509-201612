package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 * �����fieldʹ��private�����Σ���ô�������field����˽�еģ�����ķ����У�����ֱ�ӷ���������������private field
 * ��������£������ϣ��field������������ʵ�����ô����ʹ��private[this]����ζ�Ŷ���˽�е�field��ֻ�б������ڿ��Է��ʵ�
 */
object Private_this {

  def main(args: Array[String]) {

    val student1 = new Student_1
    val student2 = new Student_1
    println(student1.older(student2))

    //private[this]��ʹ��
    /**
     * �������Student_1��ʱ��ʹ��private[this] var myAge = 0
     * ��def older(s:Student_1) = {
     *  myAge > s.myAge ----------------------�˴��ᱨ��
     * }
     */
  }

}
class Student_1{
  private var myAge = 0
  def age_=(newValue:Int) {
    if(newValue > 0) myAge = newValue
    else println("illegal age!")
  }
  def age = myAge
  def older(s:Student_1) = {
    myAge > s.myAge
  }
}


