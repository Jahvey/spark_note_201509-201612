package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * ��Scala�У�trait��û�н��ܲ����Ĺ��캯���ģ�����trait��class��Ψһ���𣬵�������������Ҫtrait�ܹ���field���г�ʼ����
 * ����ô�죿ֻ��ʹ��Scala�ǳ������һ�ָ߼�����------��ǰ����
 * ���trait_9�����
 */
object Trait_10 {

  def main(args: Array[String]) {


    /**����һ*/
    val p = new {
      val msg:String = "init"
    } with P11 with SayHello_11
  }
}

/**����һ*/
trait SayHello_11{
  val msg : String
  println(msg.toString)
}
class P11

/**������*/
class P12 extends {
  val msg:String = "int"
} with SayHello_11

/**������*/
trait Sayhello_12 {
  lazy val msg:String = null
  println(msg.toString)
}
class P13 extends Sayhello_12{
  override lazy val msg:String = "init"
}