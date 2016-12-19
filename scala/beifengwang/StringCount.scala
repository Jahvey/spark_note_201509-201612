package scala.beifengwang

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
 * Created by Administrator on 2016/8/16.
 */
class StringCount extends  UserDefinedAggregateFunction{
  //inputScheme��ָ���ǣ��������ݵ�����
  override def inputSchema: StructType = {
    StructType(Array(StructField("str",DataTypes.StringType,true)))
  }

  //bufferSchema,ָ���ǣ��м���оۺ�ʱ������������ݵ�����
  override def bufferSchema: StructType = {
    StructType(Array(StructField("count",DataTypes.IntegerType,true)))
  }


  //dataType��ָ���ǣ���������ֵ������
  override def dataType: DataType = {
    IntegerType
  }

  override def deterministic: Boolean = true

  //Ϊÿ�����������ִ�г�ʼ������
  override def initialize(buffer: MutableAggregationBuffer): Unit ={
    buffer(0) = 0
  }

  //ָ���ǣ�ÿ�����飬���µ�ֵ������ʱ����ν��з����Ӧ�ľۺϵļ���
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getAs[Int](0) + 1
  }

  //����spark�Ƿֲ�ʽ�ģ�����һ����������ݣ����ܻ��ڲ�ͬ�Ľڵ��Ͻ��оֲ��ۺϣ�����update
  //���ǣ����һ�����飬�ڸ����ڵ��ϵľۺ�ֵ��Ҫ����merge��Ҳ���Ǻϲ�
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getAs[Int](0) + buffer2.getAs[Int](0)
  }

  //���ָ���ǣ�һ������ľۺ�ֵ�����ͨ���м�Ļ���ۺ�ֵ����󷵻�һ�����յľۺ�ֵ
  override def evaluate(buffer: Row): Any = {
    buffer.getAs[Int](0)
  }

















}
