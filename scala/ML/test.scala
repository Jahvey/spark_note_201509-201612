package scala.ML

/**
 * Created by Administrator on 2016/3/24.
 */
object test extends App{

  def sgdDemo {
    val featuresMatrix: List[List[Double]] = List(List(1, 4), List(2, 5), List(5, 1), List(4, 2))//��������
    val labelMatrix: List[Double] = List(19, 26, 19, 20)//��ʵֵ����
    var theta: List[Double] = List(0, 0)
    var loss: Double = 10.0
    for {
      i <- 0 until 1000 //��������
      if (loss > 0.01) //��������loss<=0.01
    } {
      var error_sum = 0.0 //�����
      var j = i % 4
      var h = 0.0
      for (k <- 0 until 2) {
        h += featuresMatrix(j)(k) * theta(k)
      } //��������Ĳ������ݼ��е�j������ļ������ǩ
      error_sum = labelMatrix(j) - h //��������Ĳ������ݼ������ǩ���������ǩ�����ֵ
      var cacheTheta: List[Double] = List()

      for (k <- 0 until 2) {
        val updaterTheta = theta(k) + 0.001 * (error_sum) * featuresMatrix(j)(k)
        cacheTheta = updaterTheta +: cacheTheta
      } //����Ȩ������
      cacheTheta.foreach(t => print(t + ","))
      print(error_sum + "\n")
      theta = cacheTheta
      //���������
      var currentLoss: Double = 0
      for (j <- 0 until 4) {
        var sum = 0.0
        for (k <- 0 until 2) {
          sum += featuresMatrix(j)(k) * theta(k)
        }
        currentLoss += (sum - labelMatrix(j)) * (sum - labelMatrix(j))
      }
      loss = currentLoss
      println("loss->>>>" + loss / 4 + ",i>>>>>" + i)
    }
  }
}
