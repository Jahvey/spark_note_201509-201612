package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 * isInstanceOfֻ���жϳ������Ƿ���ָ���༰������Ķ��󣬶����ܾ�ȷ�жϳ����������ָ����Ķ���
 * ���Ҫ��ȷ���ж϶������ָ����Ķ�����ô��ֻ��ʹ��getClass��classOf��
 * ����.getClass���Ծ�ȷ��ȡ������࣬classOf[��]���Ծ�ȷ��ȡ�࣬Ȼ��ʹ��==�����������ж�
 * ģʽƥ���൱��isInstanceOf
 */
object GetClassAndclassOf {

  def main(args: Array[String]) {

    val p:Person = new Student_3
    println(p.isInstanceOf[Person])
    println(p.getClass == classOf[Person])
    println(p.getClass == classOf[Student_3])
  }
}
class Person
class Student_3 extends  Person
