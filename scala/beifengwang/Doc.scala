package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 */

/**
 * ��һ��Scala���﷨�涨�����������Ƹ�����ʱ�������ں���������Ͽո���»���
 * �ڶ���sortWith ��Ԫ�ؽ���������ȣ���������
 */
object Doc {

  def main(args: Array[String]) {
    Array(3,4,2,4,6,3).sortWith((_<_)).foreach(println _ )
  }
}