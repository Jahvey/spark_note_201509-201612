package scala.lianshuchengjin.С���ķ�����

import breeze.linalg._

/**
 * Created by Administrator on 2016/6/25.
 */
object BreezeAPI {

  def main(args: Array[String]) {

    /**
     * ����
     * �Ӽ��˳�
     * ����������
     * �ӣ�+
     * ����-
     * �ˣ� ��*
     * ���� ��/
     * ����1���˷�ǰ�棬��ð�ţ������ĳ˺źͳ��ŷֱ��ʾ������������
     * ����2���ۼ�Ч�����ӵȺ�
     */
    val v1 = DenseVector(1.0,2.0,3.0,4.0)
    val v2 = DenseVector(0.5,0.5,0.5,0.5)
    println("\nv1+v2 : ")
    println(v1 + v2) //DenseVector(1.5, 2.5, 3.5, 4.5)

    println("\nv1-v2 : ")
    println(v1 - v2) //DenseVector(0.5, 1.5, 2.5, 3.5)

    println("\nv1 :* v2 : ")
    //����1���˺�ǰ�����ð��
    println(v1 :* v2) //DenseVector(0.5, 1.0, 1.5, 2.0)

    println("\nv1 :/ v2 :")
    //����1������ǰ�����ð��
    println(v1 :/ v2) //DenseVector(2.0, 4.0, 6.0, 8.0)

    //����2
    //���������Ľ�����浽v1�ϣ���Ҫ�ӵȺ�
    println("\nv1 += v2 : ")
    println(v1 += v2) //DenseVector(1.5, 2.5, 3.5, 4.5)
    println(v1) //DenseVector(1.5, 2.5, 3.5, 4.5)

    println("\nv1 - v2 : ")
    println(v1 -= v2 ) //DenseVector(1.0, 2.0, 3.0, 4.0)
    println(v1) //DenseVector(1.0, 2.0, 3.0, 4.0)

    println("\nv1 :*= v2 : ")
    println(v1 :*= v2) //DenseVector(0.5, 1.0, 1.5, 2.0)
    println(v1) //DenseVector(0.5, 1.0, 1.5, 2.0)

    println("\nv1 :/= v2 : ")
    println(v1 :/= v2) //DenseVector(1.0, 2.0, 3.0, 4.0)
    println(v1) //DenseVector(1.0, 2.0, 3.0, 4.0)


    /**
     * ���������
     * ��������������ȫһ��
     * �� �� +
     * �� �� -
     * �� �� :*
     * �� �� :/
     * ����1���˳�ǰ�棬��ð�ţ������ĳ˺źͳ��ŷֱ��ʾ������������
     * �����ۼ�Ч�����ӵȺ�
     */
    val m1 = DenseMatrix((1.0,2.0),(3.0,4.0))
    val m2 = DenseMatrix((0.5,0.5),(0.5,0.5))

    /**
     * 1.5  2.5
     * 3.5  4.5
     */
    println("\nm1 + m2 : ")
    println(m1 + m2)

    /**
     *0.5 1.5
     * 2.5  3.5
     */
    println("\nm1 - m2 : ")
    println(m1 - m2)

    /**
     * 0.5  1.0
     * 1.5  2.0
     */
    println("\nm1 :* m2 :")
    //ע�⣺�˺�ǰ�����ð��
    println(m1 :* m2)

    /**
     * 2.0  4.0
     * 6.0  8.0
     */
    println("\nm1 :/ m2 :")
    //ע�⣺����ǰ�����ð��
    println(m1 :/ m2)

    //����������Ľ�����浽m1�ϣ���Ҫ�ӵȺ�
    /**
     * 1.5  2.5
     * 3.5  4.5
     */
    println("\nm1 += m2 :")
    println(m1 += m2)
    println(m1)

    /**
     * �����˳�������
     */


    /**
     * �������������ֵ
     * �� �� +
     * �� �� -
     * �� �� *
     * �� �� /
     * ����1���ۼ�Ч�����ӵȺ�
     * ע�⣺�˳���ǰ����Ҫð�ţ���Ϊû�о�������ֵ�ĵ���ȼ���
     */

    val vv1 = DenseVector(1.0,2.0,3.0,4.0)

    println("\nvv1 + 0.5 : ")
    println(vv1 + 0.5) //DenseVector(1.5, 2.5, 3.5, 4.5)

    println("\nvv1 - 0.5 : ")
    println(vv1 - 0.5) //DenseVector(0.5, 1.5, 2.5, 3.5)

    println("\nvv1 * 0.5 : ")
    println(vv1 * 0.5) //DenseVector(0.5, 1.0, 1.5, 2.0)

    println("\nvv1 / 0.5 : ")
    println(vv1/0.5) //DenseVector(2.0, 4.0, 6.0, 8.0)

    /**
     *  +=  -=  *=  /= ͬ��һ��
     */

    /**
     * ����������
     * �� �� +
     * �� �� -
     * �� �� :*
     * �� :  :/
     * ����1���˳�ǰ�棬��ð�ţ������ĳ˺źͳ��ŷֱ��ʾ������������
     * ����2���ۼ�Ч�����ӵȺ�
     * ����3���������Ǻ�
     * ����4���Ǻ��������У��Ǻ����ң����У��������������������������޹�
     * ����5������������������
     */

    val mmm1 = DenseMatrix(
      (1.0,2.0),
      (3.0,4.0)
    )
    val vvv1 = DenseVector(1.0,2.0)
    // val vvv1 = DenseVector(1.0,2.0).t //y����ʱ�쳣������5������������������
    // val vvv1 = DenseVector(1.0,2.0).t.t //��ȷ�������һ������������Ҫת����������

    //����4���Ǻ��������У��Ǻ����ң�����
    println("--------------�Ǻ�����ߣ������в���---------------")

    /**
     * 2.0  4.0
     * 4.0  6.0
     */
    println("\nmmm1(*,::) + vvv1 : ")
    println(mmm1(*,::) + vvv1)

    /**
     * 0.0  0.0
     * 2.0  2.0
     */
    println("\nmmm1(*,::) - vvv1 : ")
    println(mmm1(*,::) - vvv1)

    //����1�˳�ǰ�棬��ð��

    /** *
      * 1.0 4.0
      * 3.0 8.0
      */
    println("\nmmm1(*,::) :* vvv1 : ")
    println(mmm1(*,::) :* vvv1)

    /**
     * 1.0  1.0
     * 3.0  2.0
     */
    println("\nmmm1(*,::) :/ vvv1 : ")
    println(mmm1(*,::) :/ vvv1)

    println("-------------�Ǻ����ұߣ������в���-------")

    /** *
      * 2.0 3.0
      * 5.0 6.0
      */
    println("\nmmm1(::,*) + vvv1 : ")
    println(mmm1(::,*) + vvv1)

    /**
     * �������˷�����ͬ��
     * �ۼ�ͬ��   :*=   :/=   +=  -=
     */

    /**
     * ����
     * ͳ��
     * ���
     */
    val mmmm1 = DenseMatrix(
      (1.0,2.0),
      (3.0,4.0)
    )

    println("------------����ͳ�����------------")
    //Axis._0 ����
    // 4.0  6.0
    println(sum(mmmm1,Axis._0))

    //Axis._1 ����
    //3.0 7.0
    println(sum(mmmm1,Axis._1))

    //��ֵ
    println("-------------��ֵ--------------")
    import breeze.stats.mean
    //Axis._0 ����
    //2.0 3.0
    println(mean(mmmm1,Axis._0))

    //Axis._1 ����
    //1.5 3.5
    println(mean(mmmm1,Axis._1))

    //����ͱ�׼��
    println("--------------����ͱ�׼��------------")
    import breeze.stats.{stddev,variance}

    //Axis._0 ����
    //2.0 2.0
    println(variance(mmmm1,Axis._0))

    //Axis._1 ����
    //DenseVector(0.5, 0.5)
    println(variance(mmmm1,Axis._1))

    //Axis._0 ����
    //1.4142135623730951  1.4142135623730951
    println(stddev(mmmm1,Axis._0))

    //Axis._1 ����
    //DenseVector(0.7071067811865476, 0.7071067811865476)
    println(stddev(mmmm1,Axis._1))

    //N�η��Ϳ���
    println("-----------N�η��Ϳ���-----------")
    import breeze.numerics.{pow,sqrt}

    /**
     * 1.0  4.0
     * 9.0  16.0
     */
    println(pow(mmmm1,2))

    /**
     * 1.0  8.0
     * 27.0 64.0
     */
    println(pow(mmmm1,3))

    /**
     *1.0                 1.4142135623730951
     * 1.7320508075688772  2.0
     */
    println(sqrt(mmmm1))

    //E��log
    println("----------------E��log------------")
    import breeze.numerics.{exp,log,log10,log1p}

    /**
     * 2.718281828459045   7.38905609893065
     * 20.085536923187668  54.598150033144236
     */
    //e = 2.718281828459045
    println(exp(mmmm1))

    /**
     * 0.0                 0.6931471805599453
     * 1.0986122886681098  1.3862943611198906
     */
    //��eΪ��
    println(log(mmmm1))

    /**
     * 0.0                  0.3010299956639812
     * 0.47712125471966244  0.6020599913279624
     */
    //��10Ϊ��
    println(log10(mmmm1))

    /**
     * 0.6931471805599453  1.0986122886681096
     * 1.3862943611198906  1.6094379124341003
     */
    // ��eΪ��
    // log1p() �Է��� log(1 + x)�������� x ��ֵ�ӽ���Ҳ�ܼ����׼ȷ�����
    println(log1p(m1))

    /**
     * ����
     * sin, sinh, asin, asinh
     * cos, cosh, acos, acosh
     * tan, tanh, atan, atanh
     * atan2
     * sinc(x) == sin(x)/x
     * sincpi(x) == sinc(x * Pi)
     *
     */

    //ȡ��
    println("------------ȡ��--------------")
    import breeze.numerics._
    val a = DenseVector(1.4,0.5,-2.3)

    //��������
    println(round(a))

    //����ȡ��
    println(ceil(a))

    //����ȡ��
    println(floor(a))

    //����0��Ϊ1��С��0��Ϊ-1
    println(signum(a))

    //����ֵ
    println(abs(a))

    /**
     * ʾ��
     * ģ���߼��ع�
     * ����Breze���У���һ������ӽ���룬Ԥ��
     */
    println("------------ģ���߼��ع�--------------")

    //�����������
    //val featuresMatrix = DenseMatrix.rand[Double](3,3)
    //val labelMatrix = DenseMatrix.rand[Double](3,1)

    //��������
    val featuresMatrix = DenseMatrix(
      (1.0,2.0,3.0),
      (4.0,5.0,6.0),
      (7.0,8.0,9.0)
    )
    val LabelMatrix = DenseMatrix(
      1.0,
      1.0,
      0.0
    )
    //��ֵ
    //DenseVector(4.0,5.0,6.0)
    val featuresMean = mean(featuresMatrix(::, *)).toDenseVector
    println("��ֵ��")
    println(featuresMean)

    //��׼��
    //DenseVector(3.0  3.0  3.0  )
    val featuresStddev = stddev(featuresMatrix(::,*)).toDenseVector
    println("\n��׼��: ")
    println(featuresStddev)

    //��ȥ��ֵ
    /**
     * -3.0  -2.0  -1.0
     * -1.0  0.0   1.0
     * 1.0   2.0   3.0
     */
    featuresMatrix(*,::) -= featuresMean
    println("\n��ȥ��ֵ��")
    println(featuresMatrix)

    //���Ա�׼��
    /**
     * -1.0                 -0.6666666666666666  -0.3333333333333333
     * -0.3333333333333333  0.0                  0.3333333333333333
     * 0.3333333333333333   0.6666666666666666   1.0
     */
    featuresMatrix(*,::) /= featuresStddev
    println("\n���Ա�׼� ")
    println(featuresMatrix)

    //���ɽؾ�
    /**
     * 1.0
     * 1.0
     * 1.0
     */
    val intercept = DenseMatrix.ones[Double](featuresMatrix.rows,1)
    println("\n�ؾࣺ ")
    println(intercept)

    //ƴ�ӳ�Ϊ���յ�ѵ����
    /**
     *1.0  -1.0                 -0.6666666666666666  -0.3333333333333333
     *1.0  -0.3333333333333333  0.0                  0.3333333333333333
     *1.0  0.3333333333333333   0.6666666666666666   1.0
     */
    val train = DenseMatrix.horzcat(intercept,featuresMatrix)
    println("\nѵ������ ")
    println(train)

    //����
    //Ϊ��������������ȫ������Ϊ1
    /**
     * 1.0
     * 1.0
     * 1.0
     * 1.0
     */
    val w = new DenseMatrix(4,1,Array(1.0,1.0,1.0,1.0))
    //val w = DensMatrix.rand[Double](4,1) //������ɣ�һ��Ҫָ������
    println("\n���� �� ")
    println(w)

    /**
     * -1.0
     * 1.0
     * 3.0
     */
    //�������wʱ�����û��ָ�����ͣ�A�ļ�������Ȼ���ᱨ�����Ǻ��潫�޷����㣬����ʹ��asInstanceOf
    //���wָ�������ͣ���ô��idea�У�ת�������ǻ�ɫ�ģ���˼����仰û�����ã����Բ�д
    val A = (train * w).asInstanceOf[DenseMatrix[Double]]
    println("\nA: ")
    println(A)

    /**
     * 0.2689414213699951
     * 0.7310585786300049
     * 0.9525741268224334
     */
    //Signoid����
    val probability = 1.0/(exp(A * -1.0) + 1.0)
    println("\nprobability ")
    println((probability))

    /**
     * MSE:0.5047245335361858
     */
    val MSE = mean(pow(probability - LabelMatrix,2))
    println("\nMSE: ")
    println(MSE)

    /**
     * RMSE:0.710439676211982
     */
    val RMSE = sqrt(MSE)
    println("\nRMSE: ")
    println(RMSE)

  }

}
