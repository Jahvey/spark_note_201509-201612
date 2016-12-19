package scala.SparkSQL

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.{SaveMode, SQLContext}
import org.apache.spark.sql.hive.HiveContext

/**
 * Created by Administrator on 2016/4/3.
 */
object SparkSQL2Hive {
  def  main (args: Array[String]) {
    val conf = new SparkConf().setMaster("spark://master:7077").setAppName("SparkSQL2Hive");
    val sc = new JavaSparkContext(conf);
    /**
     * ��һ��ͨ��HiveContext����ֱ�Ӳ���hive�е�����
     * �ڶ������ǿ���ֱ��ͨ��saveAsTable�ķ�ʽ��DataFrame�е����ݱ��浽Hive��
     * ����������ֱ��ͨ��HiveContext��table������ֱ�Ӽ���Hive�еĶ��������DataFrame
     */
    val hiveContext = new HiveContext(sc);//������Ӿ�ֱ�ӿ���������Hive
    hiveContext.sql("use hive")
    hiveContext.sql("DROP TABLE IF EXISTS people")
    hiveContext.sql("CREATE TABLE IF NOT EXISTS people(name STRING,age INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' LINES TERMINATED BY '\\n'")
    hiveContext.sql("LOAD DATA LOCAL INPATH '/usr/local/data/people.txt' INTO TABLE people")

    hiveContext.sql("DROP TABLE IF EXISTS peoplescores")
    hiveContext.sql("CREATE TABLE IF NOT EXISTS peoplescores(name STRING,score INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' LINES TERMINATED BY '\\n'")
    hiveContext.sql("LOAD DATA LOCAL INPATH '/usr/local/data/peoplescore.txt' INTO TABLE peoplescores")

    /**
     * ͨ��HiveContextʹ��jlinֱ�ӻ���Hive�����ű���в���
     */
    val resultDF = hiveContext.sql("SELECT pi.name,pi.age,ps.score FROM people pi " +
      "JOIN peoplescores ps ON pi.name = ps.name where ps.score>90")
    //hiveContext.sql("DROP TABLE��IF EXISTS peopleInformationresult")
    resultDF.saveAsTable("peopleInformationresult",SaveMode.Overwrite)

    hiveContext.table("peopleInformationresult").show()
















































  }
}
