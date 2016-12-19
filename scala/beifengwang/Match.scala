package scala.beifengwang

import java.io.FileNotFoundException

/**
 * Created by Administrator on 2016/8/1.
 */
import java.io._
object Match {

  class Person
  case class Teacher(name:String,subject:String) extends Person
  case class Student(name:String,classroom:String) extends Person

  def main(args: Array[String]) {
    judgeGrade("GuoYuan","C") // Just so so
    judgeGrade("GuoYuan","D") //GuoYuan,you are a good boy,come on
    judgeGrade("xx","C")
    judgeGrade("xx","F")

    processException(new IllegalArgumentException("expect two arguments,but found one argument"))
    processException(new FileNotFoundException("test,txt not fount"))
    processException(new IOException("get data from socket fail"))
    processException(new ArrayIndexOutOfBoundsException("array is null"))


    greeting(Array("Guo"))
    greeting(Array("lihua","july","jack"))
    greeting(Array("Guo","b","c"))
    greeting(Array("Guo","a","b","c","d"))

    greeting2(List("Guo"))
    greeting2(List("lihua","july","jack"))
    greeting2(List("Guo","b","c"))
    greeting2(List("Guo","a","b","c","d"))


    judgeidentify(Student("GuoYuan","jisuaji"))
    judgeidentify(Teacher("Yao","shujuku"))


    getGrade("a")
    getGrade("c")
    getGrade("d")


  }

  //ģʽƥ��Ļ����﷨
  def judgeGrade(name:String,grade:String): Unit = {
    grade match {
      case "A" => println("Excellent")//��ֵ����ƥ��
      case "B" => println("Good")
      case "C" if name == "xx" => println("come on," + name)
      case "C" => println("Just so so")
      case _ if name == "GuoYuan" => println(name + ",you are a good boy,come on") //�������
      case _grade => println("you need to work harder,your grade is " + _grade)//��ֵ
    }
  }

  //�����ͽ���ģʽƥ��
  def processException(e:Exception): Unit ={
    e match {
      case e1:IllegalArgumentException => println("you passed illegal argument.exception is : " + e1)
      case e2:FileNotFoundException => println("cannot find the file.exception is: " + e2)
      case e3:IOException => println("io error occurs.exception is " + e3)
      case _:Exception => println("cannot know which exception you hava!")
    }
  }

  //��Array��List��Ԫ�ؽ���ģʽƥ��
  def greeting(arr:Array[String]): Unit = {
    arr match {
      case Array("Guo") => println("How are you .Guo")
      case Array(gril1,gril2,gril3) => println("hi,grils,i'm jack ,nice to meet you " + gril1 + ","+gril2+","+gril3)
      case Array("Guo",_*) => println("Hi,Guo,why not introduce your friends to me!")
      case _ => println("who are you")
    }
  }

  def greeting2(arr:List[String]): Unit = {
    arr match {
      case "Guo"::Nil => println("How are you .Guo")
      case gril1::gril2::gril3::Nil => println("hi,grils,i'm jack ,nice to meet you " + gril1 + ","+gril2+","+gril3)
      case "Guo"::tail => println("Hi,Guo,why not introduce your friends to me!")
      case _ => println("who are you")
    }
  }

  /**
   * Scala���ṩ��һ��������࣬��case class��������������Ҳ���Գ��������ࡣcase class��ʵ�е�������java�е�javaBean��
   * �����ֻ����field��������Scala����ʱ�Զ��ṩgetter��setter���� ������û��method
   * case class�������캯�����ܵĲ���ͨ������Ҫʹ��var��val���Σ�Scala�Զ��ͻ�ʹ��val���Σ�����������Լ�ʹ��var���Σ�
   * ��ô���ǻᰴ��var����
   * Scala�Զ�Ϊcase class�����˰�������Ҳ����object�����Ҷ�����apply()�������÷������������캯������ͬ�Ĳ�����������
   * case class����
   */


  //case class��ģʽƥ��
  def judgeidentify(p:Person): Unit = {
    p match {
      case Teacher(name,subject) => println("Teacher,name is " + name + ",subject you teach is " + subject+".")
      case Student(name,classroom) => println("Student,name is " + name + ",your classroom is " + classroom+".")
      case _ => println("Illegal Access!")
    }
  }

  /**
   * Scala��һ����������ͣ�����Option��Option������ֵ��һ����Some����ʾ��ֵ��һ����None����ʾû��ֵ
   */
  //Option��ģʽƥ��
  val grades = Map("a" ->"A","b"->"B","c"->"C")
  def getGrade(name:String): Unit = {
    val grade = grades.get(name)
    grade match {
      case Some(grade) => println("your grade is " + grade)
      case None => println("Sorry,your grade information is not in the system")
    }
  }

}



























