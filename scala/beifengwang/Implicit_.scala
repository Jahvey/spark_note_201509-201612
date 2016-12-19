package scala.beifengwang

/**
 * Created by Administrator on 2016/8/2.
 * ��ʽת��������������͵��룺
 * ScalaĬ�ϻ�ʹ��������ʽת����һ����Դ���ͣ�����Ŀ�����͵İ��������ڵ���ʽת��������һ���ǵ�ǰ�����������ڵĿ�����
 * Ψһ��ʶ����ʾ����ʽת��������������ʾ�ľ������������
 * �����ʽת����������������������µĻ�����ô�ͱ����ֶ�ʹ��import�﷨����ĳ�����µ���ʽת��������ͨ�����飬��������Ҫ
 * ������ʽת���ط�������ĳ���������߷����ڣ���import������ʽת������������������С��ʽת�������������򣬱��ⲻ��Ҫ��
 * ��ʽת����
 *
 * ��ʽת���ķ���ʱ����
 * 1������ĳ�����������Ǹ���������Ĳ��������ͣ��뺯������Ľ��ղ������Ͳ�ƥ�䣨���������������Ʊ���ڣ�
 * 2��ʹ��ĳ�����͵Ķ��󣬵���ĳ��������������������������������ʱ����������ĳ��˱���
 * 3��ʹ��ĳ�����͵Ķ��󣬵���ĳ����������Ȼ��������������������Ǹ���������Ĳ������ͣ��뷽������Ľ��ܲ���������
 * ��ƥ�䣨�൱�ڵ�һ�������������������Ʊ���ڼ�ǿ�棩
 */
object Implicit_ {

  def main(args: Array[String]) {

    val specialPerson = new SpecialPerson("SpecialPerson")
    val student = new Student("Student")
    val older = new Older("Older")
    val teacher = new Teacher("Teacher")
    println(buySpecialTicket(specialPerson))
    println(buySpecialTicket(student))
    println(buySpecialTicket(older))
    //println(buySpecialTicket(teacher)) ���ܳɹ����᷵��Nil��Ȼ��ѭ��������ʽת����������ѭ��

    val man = new Man("GuoYuan")
    man.emitLaser

    val ticketHouse = new Tickethouse
    println(ticketHouse.buySpecialTicket(student))

    signForExam("GuoYuan")
  }

  /**
   * ��ʿת��
   * ���磺������Ʊ���ڣ�ֻ����������Ⱥ������ѧ�������˵ȣ�
   * @param name
   */
  class SpecialPerson(val name:String)
  class Student(val name:String)
  class Older(val name:String)
  class Teacher(val name:String)

  implicit def object2SpecialPerson(obj:Object):SpecialPerson = {
    if(obj.getClass == classOf[Student]){
      val stu = obj.asInstanceOf[Student]
      new SpecialPerson((stu.name))
    } else {
      if (obj.getClass == classOf[Older]) {
        val older = obj.asInstanceOf[Older]
        new SpecialPerson(older.name)
      } else
        Nil
    }
  }

  var ticketNumber = 0
  def buySpecialTicket(p:SpecialPerson) = {
    ticketNumber += 1
    "T-" + ticketNumber
  }

  /**
   * ʹ����ʽת����ǿ��������
   * ��ʽת���ǳ�ǿ���һ�����ܣ����ǿ����ڲ�֪�����м�ǿ�������͵Ĺ��ܡ�Ҳ����˵������Ϊĳ���ඨ��һ����ǿ����࣬
   * �������໥֮�����ʽת�����Ӷ���Դ����ʹ�ü�ǿ��ķ���ʱ����Scala�Զ�������ʽת��Ϊ��ǿ�࣬Ȼ���ڵ��ø÷�����
   *
   * ���磺���˱���
   */

  class Man(val name:String)
  class Superman(val name:String){
    def emitLaser = println("emit a laster!")
  }
  implicit def man2superman(man:Man):Superman = new Superman(man.name)

  /**
   * ���磺������Ʊ���ڼ�ǿ��
   */
  class Tickethouse {
    var ticketNumber = 0
    def buySpecialTicket(p:SpecialPerson) = {
      ticketNumber += 1
      "T-" + ticketNumber
    }
  }

  /**
   * ��ʽ����
   * ��ν����ʽ������ָ�����ں������߷����У�����һ����implicit���εĲ�������ʱScala�᳢���ҵ�һ��ָ�����͵ģ���
   * implicit���εĶ��󣬼���ʽֵ����ע�������
   * Scala����������Χ�ڲ��ң�һ���ǵ�ǰ�������ڿɼ���val��var�������ʽ������һ������ʽ�������͵İ��������ڵ���ʽֵ
   *
   * ���磺����ǩ��
   */

  class SignPen{
    def write(content:String) = println(content)
  }
  implicit val signPen = new SignPen

  def signForExam(name:String)(implicit signPen:SignPen): Unit = {
    signPen.write(name + "come to exam in time")
  }


}
































