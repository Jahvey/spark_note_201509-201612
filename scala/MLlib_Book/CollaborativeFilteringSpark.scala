package scala.MLlib_Book

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/4/11.
 */
object CollaborativeFilteringSpark {

  val conf = new SparkConf().setMaster("local").setAppName("CollaborativeFilteringSpark")//���û�������
  val sc = new SparkContext(conf) //��ʼ������
  val users = sc.parallelize(Array("aaa","bbb","ccc","ddd","eee"))//�����û�
  val films = sc.parallelize(Array("smzdm","ylxb","znb","nbsc","fcwr"))//���õ�Ӱ��
  import scala.collection.mutable.Map
  val source = Map[String,Map[String,Int]]() //ʹ��һ��sourceǶ��map��Ϊ������Map����Ӱ����ֵ���Ĵ洢
  val fileSource = Map[String,Int]()
  def getSource():Map[String,Map[String,Int]] = {
    val user1FilmSource = Map("smzdm" -> 2,"ylxb" -> 3,"znb" -> 1,"nhsc" -> 0,"fcwr" -> 1)
    val user2FilmSource = Map("smzdm" -> 1,"ylxb" -> 2,"znb" -> 2,"nhsc" -> 1,"fcwr" -> 4)
    val user3FilmSource = Map("smzdm" -> 2,"ylxb" -> 1,"znb" -> 0,"nhsc" -> 1,"fcwr" -> 4)
    val user4FilmSource = Map("smzdm" -> 3,"ylxb" -> 2,"znb" -> 0,"nhsc" -> 5,"fcwr" -> 3)
    val user5FilmSource = Map("smzdm" -> 5,"ylxb" -> 3,"znb" -> 1,"nhsc" -> 1,"fcwr" -> 2)
    source += ("aaa" -> user1FilmSource)
    source += ("bbb" -> user2FilmSource)
    source += ("ccc" -> user3FilmSource)
    source += ("ddd" -> user4FilmSource)
    source += ("eee" -> user5FilmSource)
    source
  }

  //���������ֵ�������������ƶ�
  def getColloborateSource(user1:String,user2:String):Double = {
    val user1FilmSource = source.get(user1).get.values.toVector//��õ�һ���û�������
    val user2FilmSource = source.get(user2).get.values.toVector//��õڶ����û�������
    val member = user1FilmSource.zip(user2FilmSource).map(d => d._1 * d._2).reduce(_+_).toDouble//�Թ�ʽ���Ӳ��ֽ��м���
    val temp1 = math.sqrt(user1FilmSource.map(num => math.pow(num,2)).reduce(_+_))//��ĸ1
    val temp2 = math.sqrt(user2FilmSource.map(num => math.pow(num,2)).reduce(_+_))//��ĸ2
    val denominator = temp1 * temp2
    member/denominator
  }

  def main(args:Array[String]): Unit = {
    getSource()
    val name = "bbb"
    users.foreach( user => {
      println(name + "�����" + user + "�������Է����ǣ�" + getColloborateSource(name,user))
    })
  }
}
