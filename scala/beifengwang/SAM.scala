package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * ��Java�У���֧��ֱ�ӽ���������һ��������Ϊ������ͨ����˵��Ψһ�İ취���Ƕ���һ��ʵ����ĳ���ӿڵ����
 * ʵ�����󣬸ö���ֻ��һ������������Щ�ӿڶ�ֻ�е����ĳ��󷽷���Ҳ����single abstract method�����ΪSAM
 *
 * ����Scala�ǿ��Ե���Java�Ĵ���ģ���˵����ǵ���Java��ĳ������ʱ�����ܾͲ��ò�����SAM���ݸ��������ǳ�
 * �鷳������Scala����֧��ֱ�Ӵ��ݺ����ġ���ʱ�Ϳ���ʹ��Scala�ṩ�ģ��ڵ���Java����ʱ��ʹ�õĹ��ܣ�
 * SAMת��������SAMת��ΪScala����
 *
 * Ҫʹ��SAMת������Ҫʹ��Scala�ṩ�����ԣ���ʽת��
 */

import javax.swing._
import java.awt.event._
object SAM {

  def main(args: Array[String]) {

    val button = new JButton("Click")
    //������java��д��
    button.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent){
        println("Click Me!!!")
      }
    })

    /**������Scala��SAMת��*/
    implicit def convert2ActionListener(actionProcessFunc:(ActionEvent) => Unit) = new ActionListener {
      override def actionPerformed(e: ActionEvent){
        actionProcessFunc(e)
      }
    }
    button.addActionListener((event:ActionEvent) => println("Click Me!!!"))
  }
}
