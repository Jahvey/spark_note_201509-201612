package scala.beifengwang

/**
 * Created by Administrator on 2016/7/31.
 * �Ƴ���һ�������������еĸ���
 */
object case1 {

  def main(args: Array[String]) {

    import scala.collection.mutable.ArrayBuffer
    val a = ArrayBuffer[Int]()
    a += (1,2,3,4,5,-1,-3,-5,-9)

    /**Ч�ʵ��£�ԭ����remove��ʱ��Ҫ�ƶ�һ������Ԫ�ص�λ��*/
    var foundFirstNegative = false
    var arrayLength = a.length
    var index = 0
    while(index < arrayLength) {
      if(a(index) >= 0) {
        index += 1
      } else {
        if(!foundFirstNegative) {foundFirstNegative = true;index += 1}
        else{a.remove(index);arrayLength -= 1}
      }
    }
    println(a.mkString(","))

    val b = ArrayBuffer[Int]()
    b += (1,2,3,4,5,-1,-3,-5,-9)
    //û��¼���в���Ҫ�Ƴ���Ԫ�ص��������Ժ�һ�����Ƴ���Ҫ�Ƴ���Ԫ��
    //���ܽϸߣ������ڵ�Ԫ��Ǩ��ֻҪִ��һ�μ���
    var  foundFirstNegative1 = false
    val keepIndex = for(i <- 0 until b.length if !foundFirstNegative1 || b(i) >0) yield{
      if(b(i) < 0 ) foundFirstNegative1 = true
      i
    }
    for(i <- 0 until keepIndex.length){b(i) = b(keepIndex(i))}
    b.trimEnd(b.length - keepIndex.length)
    println(b.mkString(","))
  }

}
