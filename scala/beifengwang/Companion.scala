package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 * �����һ��class������һ����classͬ����object����ô�ͳ����object��class�İ�������class��object�İ�����
 * ������Ͱ��������������һ��.scala�ļ�֮��
 * ������Ͱ������������ص�����ڣ�������Է���private field
 */
object Companion {

  private val eyeNum = 2

  def main(args: Array[String]) {
    (new Companion).say
  }

}
class Companion {
  def say = println(Companion.eyeNum) //������Ͱ������������ص�����ڣ�������Է���private field
}
