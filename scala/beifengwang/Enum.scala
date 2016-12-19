package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 *
 */
object Enum {

  def main(args: Array[String]) {
    println(Season.SPRINT)

    println(Season1.AUTUMN)
    println(Season1(0))
    println(Season1.withName("spring"))
    println(Season1.AUTUMN.id)
    println(Season1.AUTUMN.toString)

    //ʹ��ö��object.values���Ա���ö��ֵ
    for(ele <- Season.values) println(ele)
  }
}

/**
 * Scalaû��ֱ���ṩ������Java�е�Enum������ö�����ԣ����Ҫʵ��ö�٣�����Ҫ��object�̳�Enumeration�࣬��
 * ����Value��������ʼ��ö��ֵ
 */
object Season extends Enumeration{
  val SPRINT,SUMMER,AUTUMN,WINTER = Value
}

/**
 * ������ͨ��Value����ö��ֵ��id��name��ͨ��id��toString���Ի�ȡ��������ͨ��id��name������ö��ֵ
 */
object Season1 extends Enumeration {
  val SPRING = Value(0,"spring")
  val SUMMER = Value(1,"summer")
  val AUTUMN = Value(2,"autumn")
  val WINTER = Value(3,"winter")
}
