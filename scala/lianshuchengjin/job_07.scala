package scala.lianshuchengjin

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.util.KMeansDataGenerator
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/7/25.
 */
object job_07 {

  def main(args:Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("job_07")
    val sc = new SparkContext(conf)
    sc.setLogLevel("OFF")

    val data = KMeansDataGenerator.generateKMeansRDD(sc,1000,10,2,0.8)//1000��������10�����ĵ㣬2��ά��,��ϸ�˹�ֲ�
    val parsedData = data.map(p => Vectors.dense(p))

    val initMode = "k-means||"
    val numClusters = 10
    val numIterations = 1000
    val model = new KMeans()
      .setInitializationMode(initMode)
      .setK(numClusters)
      .setMaxIterations(numIterations)
      .run(parsedData)

    //���ĵ�
    val centers = model.clusterCenters
    println("center")
    for(i <- 0 to centers.length-1) {
      println(centers(i)(0) + "\t" + centers(i)(1))
    }

    //������
    val WSSE = model.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSE)

  }
}
