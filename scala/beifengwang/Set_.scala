package scala.beifengwang

import scala.collection.mutable

/**
 * Created by Administrator on 2016/8/1.
 */
object Set_ {

  def main(args: Array[String]) {

    /**
     * Set����һ��û���ظ�Ԫ�صļ���
     * ���ظ�Ԫ�ؼ���Set��û���õģ�����val s = Set(1,2,3);s + 1;s+4
     * ����Set�ǲ���֤����˳��ģ�Ҳ����˵��Set�е�Ԫ��������ģ�
     */
    val s = new scala.collection.mutable.HashSet[Int]();
    s += 1; s += 2; s += 5
    println(s)//Set(1, 5, 2)

    /**LinkedHashSet����һ������ά������˳��*/
    val linkedHashSet= new mutable.LinkedHashSet[Int]();
    linkedHashSet += 1; linkedHashSet += 2; linkedHashSet += 3
    println(linkedHashSet)//Set(1, 2, 3)

    /**SortedSet���Զ�����key����������*/
    val sortedSet = scala.collection.mutable.SortedSet("orange","apple","banana")
    println(sortedSet)//TreeSet(apple, banana, orange)
  }
}
