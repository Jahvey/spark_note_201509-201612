package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * ��trait�У��ǿ��Ը��Ǹ�trait�ĳ��󷽷���
 * ���Ǹ���ʱ�����ʹ����super.�����Ĵ��룬���޷�ͨ�����롣��Ϊsuper.�����ͻ�ȥ���ø�trait�ĳ��󷽷�����ʱ��trait��
 * �÷������ǻᱻ��Ϊ�ǳ����
 * ��ʱ���Ҫͨ�����룬�͵ø���trait�ķ�������abstract override����
 */
object Trait_7 {

  def main (args: Array[String]){

  }
}

trait Logger_1 {
  def log(msg:String)
}
trait MyLogger_1 extends Logger {
  abstract override def log(msg:String){
    super.log(msg)
  }
}
