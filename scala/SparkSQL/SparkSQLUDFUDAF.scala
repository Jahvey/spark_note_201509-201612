package scala.SparkSQL

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/4/5.
 */

/**
 * UDF:User Defined Function���û��Զ���ĺ���������������ʱһ����������ݼ�¼��ʵ���Ͻ�������ͨ��scala����
 * UDAF��User Defined Aggregation Function���û��Զ���ľۺϺ����������������������ݼ��ϣ��ܹ��ھۺϲ����Ļ����Ͻ���
 * �Զ��������
 *
 * ʵ���Ͻ�������˵UDF�ᱻSpark SQL�е�Catalyst��װ��ΪExpression�����ջ�ͨ��eval�������������������Row���˴���Row��
 * DataFrame�е�Rowû���κι�ϵ��
 */
object SparkSQLUDFUDAF {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SparkSQLUDFUDAF").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val bigData = Array("Spark","Spark","hadoop","spark","hadoop","spark","spark","hadoop","spark","hadoop")
    val bigDataRDD = sc.parallelize(bigData)
    val bigDataRDDRow = bigDataRDD.map(item => Row(item))
    val structType = StructType(Array(StructField("word",DataTypes.StringType,true)))
    val bigDataDF = sqlContext.createDataFrame(bigDataRDDRow,structType)
    bigDataDF.registerTempTable("bigDataTable")

    /**
     * ͨ��SQLContextע��UDF����Scala 2.10.x�汾UDF���������Խ���22�����������
     */
    sqlContext.udf.register("computeLength",(input:String) => input.length)

    //ֱ����SQL�����ʹ��UDF������ʹ��SQL�Զ����ڲ�����һ��
    sqlContext.sql("select word,computeLength(word) length  from bigDataTable").show()

    sqlContext.udf.register("wordCount",new MyUDAF)
    sqlContext.sql("select word,wordCount(word) as count,computeLength(word) as length from bigDataTable " +
      "group by word").show()

    while(true) {

    }
  }
}
    /**
     * ����ģ��ʵ��UDAF
     */
class MyUDAF extends UserDefinedAggregateFunction {
  /**
   * �÷���ָ�������������ݵ�����
   * @return
   */
  override def inputSchema: StructType = StructType(Array(StructField("input",DataTypes.StringType,true)))

  /**
   * �ڽ��оۺϲ�����ʱ����Ҫ��������ݵĽ��������
   * @return
   */
  override def bufferSchema: StructType = StructType(Array(StructField("count",DataTypes.IntegerType,true)))

  /**
   * ָ��UDAF��������󷵻صĽ������
   * @return
   */
  override def dataType: DataType = IntegerType

  override def deterministic: Boolean = true

  /**
   * ��aggregate֮ǰÿ�����ݵĳ�ʼ�����
   * @param buffer
   */
  override def initialize(buffer: MutableAggregationBuffer): Unit = {buffer(0) = 0}

  /**
   * �ڽ��оۺϵ�ʱ��ÿ�����µ�ֵ�������Է����ľۺ���ν��м���
   * ���صľۺϲ������൱��hadoop MapReduceģ���е�combiner
   * @param buffer
   * @param input
   */
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getAs[Int](0) + 1
  }

  /**
   * ����ڷֲ�ʽ�ڵ����local Reduce��ɺ���Ҫ����ȫ�ּ����Merge����
   * @param buffer1
   * @param buffer2
   */
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) =  buffer1.getAs[Int](0) +buffer2.getAs[Int](0)
  }

  /**
   * ����UDAF���ļ�����
   * @param buffer
   * @return
   */
  override def evaluate(buffer: Row): Any = buffer.getAs[Int](0)
}
