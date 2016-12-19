package scala.ALS

import org.apache.spark.mllib.evaluation.RegressionMetrics
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/6/7.
 */
object MovieCF {

  def main(args:Array[String]): Unit = {

    val conf  = new SparkConf().setMaster("local").setAppName("UserBased")
    val sc = new SparkContext(conf)

    //��ȡ��Ӱ��Ϣ������ Դ���ݸ�ʽ�� movieID,title,genres
    val moviesAndTitle = sc.textFile("E:\\���ݼ�\\movieLens\\ml-latest-small\\ml-latest-small/movies.csv")
    val title = moviesAndTitle.first()
    val movies = moviesAndTitle.filter(x => x != title).map{ line =>
      val fields = line.split(",")
      (fields(0).toInt,fields(1))
    }.collect().toMap

    //��ȡ��������ΪRDD{Rating} Դ���ݸ�ʽΪ��userId,moveiid,rating,tiemsstamp
    val ratingsAndHeader = sc.textFile("E:\\���ݼ�\\movieLens\\ml-latest-small\\ml-latest-small/ratings.csv")
    val header = ratingsAndHeader.first()
    val ratings = ratingsAndHeader.filter( x => x!= header).map{ line =>
      val fields = line.split(",")
      val rating = Rating(fields(0).toInt,fields(1).toInt,fields(2).toDouble)
      val timestamp = fields(3).toLong % 10
      (timestamp,rating)
    }

    //������ݼ�������Ϣ
    val numRatings = ratings.count()
    val numUsers = ratings.map(_._2.user).distinct().count()
    val numMovies = ratings.map(_._2.product).distinct().count()
    println("Got " + numRatings + " ratings from " + numUsers + " users on " + numMovies + " movies.")

    //����timestamp�����ݼ���Ϊѵ������timestamp < 6��,��֤����6<timestamp<8)�Ͳ��Լ���timestamp>8)
    val training = ratings.filter(x => x._1 < 6 ).values.repartition(4).cache()
    val validation = ratings.filter(x => x._1>=6 && x._1<8).values.repartition(4).cache()
    val test = ratings.filter(x=>x._1 >= 8).values.cache()

    val numTraining = training.count()
    val numValidation = validation.count()
    val numTest = test.count()

    println("Training: " + numTraining + " , validation : " + numValidation + " , test : " + numTest)

    //ʹ�ò�ͬ�Ĳ���ѵ��Эͬ����ģ�ͣ�����ѡ���RMSE��С��ģ�ͣ�Ϊ�˼������ֻ��һ����С�Ĳ�����Χѡ�񣺾���ֽ��
    // �ȴ�8-12��ѡ������ϵ����1.0-10.0��ѡ�񣬵���������10-20��ѡ�񣬹���8��ģ�ͣ�
    val ranks = List(8,12)
    val lambdas = List(1.0,10.0)
    val numIters = List(10,20)
    var bestModel:Option[MatrixFactorizationModel] = None
    var bestValidationRmse = Double.MaxValue
    var bestRank = 0
    var bestLambda = -1.0
    var bestNumIter = -1
    for(rank <- ranks;lambdas <- lambdas;numIters <- numIters) {
      val model = ALS.train(training,rank,numIters,lambdas)
      val validationRmse = computeRmse(model,validation)
      if(validationRmse<bestValidationRmse) {
        bestModel = Some(model)
        bestValidationRmse = validationRmse
        bestRank = rank
        bestLambda = lambdas
        bestNumIter = numIters
      }
    }

    val testRmse = computeRmse(bestModel.get,test)
    println("The best model was trained with rank = " + bestRank +
      " and  lambda = " + bestLambda +
      " and numIter = " + bestNumIter +
      " and its RMSE on the test set is " + testRmse + "."
    )


    //val myRatedMovieIds = myRatings.map(_.product).toSet
    val xx  = Set(8464,4298,3669,8157,4374,4173,8450,1069)
    val cands = sc.parallelize(movies.keys.filter(!xx.contains(_)).toSeq)
    val recommendationgs = bestModel.get.predict(cands.map((2,_))).collect().sortBy(- _.rating).take(10)

    var i = 1
    println("Movies recommended for you :")
    recommendationgs.foreach{ r =>
      println("%2d".format(i) + ": " + movies(r.product))
      i += 1
    }
  }

  //���庯������������RMSE
  def computeRmse (model: MatrixFactorizationModel,data:RDD[Rating]) : Double = {
    val predictions:RDD[Rating] = model.predict(data.map(x => (x.user,x.product)))
    val predictionsAndRatings = predictions.map{ x=>
      ((x.user,x.product),x.rating)
    }.join(data.map(x => ((x.user,x.product),x.rating))).values

    //val regressionMetrics = new RegressionMetrics(predictionsAndRatings)
    //println("Mean Squarted Error = " + regressionMetrics.meanSquaredError)
    //println("Root Mean Squarted Error = " + regressionMetrics.rootMeanSquaredError)

    math.sqrt(predictionsAndRatings.map(x => (x._1-x._2) * (x._1 -x._2)).mean())
  }

}
