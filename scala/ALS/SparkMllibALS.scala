package scala.ALS


import org.apache.spark.mllib.recommendation._
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/6/7.
 */
object SparkMllibALS {

  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local").setAppName("UserBased")
    val sc = new SparkContext(conf)

    val data = sc.textFile("E:\\softwares\\spark-1.6.0-bin-hadoop2.6\\data\\mllib\\als\\test.data")
    val ratings = data.map(_.split(",") match { case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt, rate.toDouble)
    })

    val rank = 10 //���ð���Ϊ10���о���fenjie
    val numIterations = 10 //���õ�������Ϊ10��
    val alpha = 0.01 //���þ���ֽ������ϵ��Ϊ0.01
    val model = ALS.train(ratings, rank, numIterations, alpha)

    println("Rating of user 1 towards item 1 is : " + model.predict(1, 1))
    //Ԥ���û�1�����Ȥ��2����Ŀ
    model.recommendProducts(1, 2).foreach { rating =>
      println("Product" + rating.product + "Rating = " + rating.rating)
    }

    val usersProducts = ratings.map { case Rating(user, product, rate) =>
      (user, product)
    }
    val predictions = model.predict(usersProducts).map { case Rating(user, product, rate) =>
      ((user, product), rate)
    }
    val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
      ((user,product),rate)
    }.join(predictions)
    //����������
    val MSE = ratesAndPreds.map{ case ((user,product),(r1,r2)) =>
      val err = r1-r2
      err * err
    }.mean()

    println("Mean squared Error = " + MSE)

  }

}
