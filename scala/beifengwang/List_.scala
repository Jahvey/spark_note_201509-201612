package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * �õݹ麯������List��ÿ��Ԫ�ض�����ָ��ǰ׺������ӡ����ǰ׺��Ԫ��
 */
object List_ {

  def main(args: Array[String]) {
    decorator(List(1,2,3,4),"+")

    /**LinkedList����һ���ɱ���б�ʹ��elem(head)����������ͷ����ʹ��next(tail)����������β��*/
    val list = scala.collection.mutable.LinkedList(1,2,3,4,5)
    println(list.elem + "  " + list.next)
    println(list.head + "  " + list.tail)
  }

  def decorator(list:List[Int],prefix:String): Unit ={
    if(list != Nil) { //Nil�൱��java�����null
      println(prefix + list.head)
      decorator(list.tail,prefix)
    }
  }
}
