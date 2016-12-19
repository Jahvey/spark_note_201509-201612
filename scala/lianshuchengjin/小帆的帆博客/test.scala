package scala.lianshuchengjin.С���ķ�����

import breeze.linalg._
import breeze.numerics._
import breeze.stats._

object Work2 {
  def main(args: Array[String]) {

    // �����������
    //    val featuresMatrix = DenseMatrix.rand[Double](3, 3)
    //    val labelMatrix = DenseMatrix.rand[Double](3, 1)

    // ��������
    val featuresMatrix = DenseMatrix(
      (1.0, 2.0, 3.0),
      (4.0, 5.0, 6.0),
      (7.0, 8.0, 9.0)
    )

    val labelMatrix = DenseMatrix(
      1.0,
      1.0,
      0.0
    )

    // ��ֵ
    // DenseVector(4.0, 5.0, 6.0)
    val featuresMean = mean(featuresMatrix(::, *)).toDenseVector
    println("��ֵ��")
    println(featuresMean)

    // ��׼��
    // DenseVector(3.0, 3.0, 3.0)
    val featuresStddev = stddev(featuresMatrix(::, *)).toDenseVector
    println("\n��׼�")
    println(featuresStddev)

    // ��ȥ��ֵ
    /**
     * -3.0  -3.0  -3.0
     * 0.0   0.0   0.0
     * 3.0   3.0   3.0
     */
    featuresMatrix(*, ::) -= featuresMean
    println("\n��ȥ��ֵ��")
    println(featuresMatrix)

    // ���Ա�׼��
    /**
     * -1.0  -1.0  -1.0
     * 0.0   0.0   0.0
     * 1.0   1.0   1.0
     */
    featuresMatrix(*, ::) /= featuresStddev
    println("\n���Ա�׼�")
    println(featuresMatrix)

    // ���ɽؾ�
    /**
     * 1.0
     * 1.0
     * 1.0
     */
    val intercept = DenseMatrix.ones[Double](featuresMatrix.rows, 1)
    println("\n�ؾࣺ")
    println(intercept)

    // ƴ�ӳ�Ϊ���յ�ѵ����
    /**
     * 1.0  -1.0  -1.0  -1.0
     * 1.0  0.0   0.0   0.0
     * 1.0  1.0   1.0   1.0
     */
    val train = DenseMatrix.horzcat(intercept, featuresMatrix)
    println("\nѵ������")
    println(train)

    // ����
    // Ϊ��������������ȫ������Ϊ1
    /**
     * 1.0
     * 1.0
     * 1.0
     * 1.0
     */
    val w = new DenseMatrix(4, 1, Array(1.0, 1.0, 1.0, 1.0))
    //    val w = DenseMatrix.rand[Double](4, 1) // �������, һ��Ҫָ������
    println("\n������")
    println(w)

    /**
     * -2.0
     * 1.0
     * 4.0
     */
    // �������wʱ�����û��ָ�����ͣ�A�ļ�������Ȼ�����д����Ǻ��潫�޷����㣬����ͨ��asInstanceOf��������ת��
    // ���wָ�������ͣ���ô��idea�У�ת�������ǻ�ɫ�ģ���˼����仰û�����ã����Բ�д
    val A = (train * w).asInstanceOf[DenseMatrix[Double]]
    println("\nA��")
    println(A)

    /**
     * 0.11920292202211755
     * 0.7310585786300049
     * 0.9820137900379085
     */
    // Sigmoid����
    val probability = 1.0 / (exp(A * -1.0) + 1.0)
    println("\nprobability��")
    println(probability)

    /**
     * MSE : 0.6041613548425021
     */
    val MSE = mean(pow(probability - labelMatrix, 2))
    println("\nMSE��")
    println(MSE)

    /**
     * RMSE : 0.777278170825929
     */
    val RMSE = sqrt(MSE)
    println("\nRMSE��")
    println(RMSE)
  }

}