package scala.ItemCF

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/6/1.
 */
object ItemCF {

  def main(args: Array[String]) {

    //1 ����Spark����
    val conf = new SparkConf().setMaster("local").setAppName("ItemCF")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    //2 ��ȡ��������
    val  data_path = "sample_itemf2.txt"
    val data = sc.textFile(data_path)
    val userdata = data.map(_.split(",")).map(f => (ItemPref(f(0),f(1),f(2).toDouble))).cache()

    //3 ����ģ��
    val mysimil = new ItemSimilarity()
    val simil_rdd1 = mysimil.Similarity(userdata,"euclidean")
    val recommd = new RecommendedItem
    val recommd_rdd1 = recommd.Recommend(simil_rdd1,userdata,1)

    //4 ��ӡ���
    println(s"��Ʒ���ƶȾ���:${simil_rdd1.count()}")
    simil_rdd1.collect().foreach{ ItemSimi =>
      println(ItemSimi.itemid1+"," + ItemSimi.itemid2 + "," + ItemSimi.similar)
    }
    println(s"�û��Ƽ��б�:${recommd_rdd1.count()}")
    recommd_rdd1.collect().foreach{UserRecomm =>
      println(UserRecomm.userid + "," + UserRecomm.itemid + "," + UserRecomm.pref)
    }


  }

}
