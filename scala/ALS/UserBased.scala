package scala.ALS

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/5/31.
 */
object UserBased {

  def  main (args: Array[String]){

    val conf  = new SparkConf().setMaster("local").setAppName("UserBased")
    val sc = new SparkContext(conf)

    //���������ݶ�ȡ��CoordinateMatrix��
    import org.apache.spark.mllib.linalg.distributed._
    val data = sc.textFile("E:\\softwares\\spark-1.6.0-bin-hadoop2.6\\data\\mllib\\als\\test.data")
    val parsedData = data.map(_.split(',') match {
      case Array(user,item,rate) =>MatrixEntry(user.toLong - 1, item.toLong - 1,rate.toDouble)
    })
    val ratings = new CoordinateMatrix(parsedData)

    //��CoordinateMatrixת��ΪRowMatrix���������û����������ƶȡ�����RowMatrixֻ�ܼ�����֮������ƶȣ����û�������
    //���б�ʾ�����CoordinateMatrix��Ҫ�ȼ���ת�ã�
    val matrix = ratings.transpose().toRowMatrix();
    val similarities = matrix.columnSimilarities(0.1)

    //������ҪԤ���û�1����Ŀ1�����֣���ôԤ���������û�1���������ּ��������û�����Ŀ1���ֵİ����ƶȵļ�Ȩƽ����
    //�����û�1��ƽ������
    val ratingsOfUser1 = ratings.toRowMatrix().rows.toArray()(0).toArray
    val avgRatingOfUser1 = ratingsOfUser1.sum/ratingsOfUser1.size
    //���������û�����Ŀ1�ļ�Ȩƽ����
    val ratingsToItem1 = matrix.rows.toArray()(0).toArray.drop(1)
    val weights = similarities.entries.filter(_.i == 0).sortBy(_.j).map(_.value).collect()
    val weigthedR = ( 0 to 2 ).map(t => weights(t)*ratingsToItem1(t)).sum /weights.sum
    //������Ԥ����
    printf("Rating of user 1 towords item 1 is : " + (avgRatingOfUser1 + weigthedR))
  }

}
