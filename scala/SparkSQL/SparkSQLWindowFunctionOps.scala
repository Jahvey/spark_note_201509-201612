package scala.SparkSQL

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/4/5.
 */
object SparkSQLWindowFunctionOps {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("SparkSQLWindowFunctionOps")
    val sc = new SparkContext(conf)
    val hiveContext = new HiveContext(sc)
    hiveContext.sql("use hive")
    hiveContext.sql("DROP TABLE IF EXISTS scores")
    hiveContext.sql("CREATE TABLE IF NOT EXISTS scores(name STRING,score INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LINES TERMINATED BY '\\n'")
    hiveContext.sql("LOAD DATA LOCAL INPATH '/usr/local/data/TopNGroup.txt' INTO TABLE scores")

    /**
     * ʹ���Ӳ�ѯ�ķ�ʽ���Ŀ�����ݵ���ȡ����Ŀ�������ڲ�ʹ�ô��ں���row_number���з�������
     * PARTITION BY��ָ�����ں��������Key��
     * ORDER BY��������������
     *
     */
    val result = hiveContext.sql("SELECT name,score " +
      "FROM ( SELECT name,score,row_number() OVER(PARTITION BY name ORDER BY score DESC) rank  FROM scores) sub_scores " +
      "WHERE rank <=4")
    result.show()

    hiveContext.sql("DROP TABLE IF EXISTS sortedresultScores")
    result.saveAsTable("sortedresultScores")



















  }
}
