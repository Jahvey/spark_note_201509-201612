package scala.ML

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Administrator on 2016/7/2.
 */
object TF_IDF {

  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local[16]").setAppName("TF_IDF")
    val sc = new SparkContext(conf)
    sc.setLogLevel("OFF")

    //�������أ�http://qwone.com/~jason/20Newsgroups/
    val path = "E:\\���ݼ�\\���������ݼ�\\20news-bydate\\20news-bydate-train/*"
    val rdd = sc.wholeTextFiles(path)
    val text = rdd.map{ case (file,text) => text }
    println(text.count())

    //���������� ���������Ϣ���ǿ��Եõ����������е���Ϣ����������ȣ�
    val newsgroups = rdd.map { case (file, text) =>
      file.split("/").takeRight(2).head
    }
    val countByGroup = newsgroups.map(n => (n,1)).reduceByKey(_+_).collect.sortBy(-_._2).mkString("\n")
    println("���������⣺ "+countByGroup)

    /**
     * �����ı��������̵ĵ�һ�������з�ÿһ���ĵ���ԭʼ����Ϊ������ʣ�Ҳ�����������ɼ��ϡ�������̽����ִʡ�
     * ����ʵ����򵥵Ŀո�ִʣ�����ÿ���ĵ������е��ʱ�ΪСд��
     * ���н�� 402978
     * �ӽ���п��Եĳ�����ʱ��Խ�С���ı�������ͬ���ʵĸ�����Ҳ������������������ά����Ҳ���ܻ�ǳ��ߣ�
     */
    val whiteSpaceSplit = text.flatMap(t => t.split(" ").map(_.toLowerCase()))
    println("����ͳ�Ʋ���ת��ΪСд�� "+whiteSpaceSplit.distinct().count())

    /**
     * ֮ǰ�򵥵ķִʷ���������ܶ൥�ʣ�������಻�ǵ��ʵ��ַ�����������ţ�û�й��˵����󲿷ִַʷ�������
     * ����Щ�ַ��Ƴ������ǿ���ʹ��������ʽ�з�ԭʼ�ĵ����Ƴ���Щ�ǵ����ַ���
     * �⽫������ٲ�ͬ���ʵ�����
     */
    val nonWordSplit = text.flatMap(t => t.split("""\W+""")).map(_.toLowerCase())
    println("�Ƴ��Ƿ����ʣ� "+nonWordSplit.distinct().count())

    /**
     * ��������ʹ�÷ǵ�������ģʽ���з��ı���Ч����������Ȼ�кܶ�������ֵĵ���ʣ�¡�����Щ����£����ֻ��Ϊ�ĵ��е�
     * ��Ҫ���ݡ�������������˵����һ������Ҫ���˵����ֺͰ������ֵĵ��ʣ�
     * ʹ������ģʽ���Թ��˵������ģʽ��ƥ��ĵ��ʣ�
     */
    val regex = """[^0-9]*""".r
    val filterNumbers = nonWordSplit.filter(token =>
      regex.pattern.matcher(token).matches()
    )
    println("�Ƴ����֣� "+filterNumbers.distinct().count())

    /**
     * �Ƴ�ͣ�ô�
     * ͣ�ô���ָ������һ���ı������ʹ�����ı����������ĵ��кܶ�εĳ��ôʡ���׼��Ӣ��ͣ�ôʰ��� and��but��the��of�ȡ�
     * ��ȡ�ı������ı�׼�����Ǵӳ�ȡ�Ĵ����ų�ͣ�ôʡ�
     * ��ʹ��TF_IDF��Ȩʱ����Ȩģʽ�Ѿ�������㡣һ��ͣ�ô����Ǻܵ͵�IDF����������һ���ܵ͵�TF-IDFȨֵ����˳�Ϊһ��
     * ����Ҫ�Ĵʡ���Щʱ�򣬶�����Ϣ��������������ͣ�ô�����Ҫ�����������ǣ���û�������ȡ����ʱ�Ƴ�ͣ�ôʣ���Ϊ��
     * ���Խ�������������ά�Ⱥ�ѵ�����ݵĴ�С��
     *
     * �鿴��Ƶ�� �õ�����ǰé�ĵ���
     */
    val tokenCounts = filterNumbers.map(t => (t,1)).reduceByKey(_+_)
    val oreringDesc = Ordering.by[(String,Int),Int](_._2)
    println("�Ƴ�ͣ�ôʣ� "+tokenCounts.top(20)(oreringDesc).mkString("\n"))

    /**
     * ��the,146532��
     * (to,75064)
     * (of,...)
     * (a,...)
     * .......
     * ���������ϣ��ܶೣ�ôʿ��Ա���עΪͣ�ôʡ�����Щ���е�ĳЩ�ʺ��������ôʼ��ϳ�һ��ͣ�ôʼ������˵���Щ��֮��
     * �Ϳ��Կ���ʣ�µĵ���
     * ͣ�ôʼ�һ��ܴ�����չʾһС���֣�����ԭ����Ϊ��֮��չ��TF-IDF���ڳ��ôʵ�Ӱ�죩
     */
    val stopwords = Set(
      "the","a","an","of","or","in","for","by","on","but","is","not",
    "with","as","was","if",
      "they","are","this","and","it","have","from","at","my",
    "be","that","to"
    )
    val tokenCountsFilteredStopwords = tokenCounts.filter{ case(k,v) =>
      !stopwords.contains(k)
    }
    println("�Ƴ�ͣ�ôʣ� "+tokenCountsFilteredStopwords.top(20)(oreringDesc).mkString("\n"))

    /**
     * ɾ����Щ��������һ���ַ��ĵ��ʣ����Ƴ�ͣ�ôʵ�ԭ������
     */
    val tokenCountsFilteredSize = tokenCountsFilteredStopwords.filter{ case (k,v) => k.size >= 2}
    println("�Ƴ������ַ��� "+tokenCountsFilteredSize.top(20)(oreringDesc).mkString("\n"))

    /**
     * ����Ƶ��ȥ������
     *
     * �ڷִʵ�ʱ�򣬻���һ�ֱȽϳ��õ�ȥ�����ʵķ�����ȥ�������ı����г���Ƶ�ʺܵ͵ĵ��ʡ����磬����ı�����
     * ����Ƶ����͵ĵ��ʣ�ע����������ʹ�ò�ͬ������ʽ��������������Ľ����
     */
    val oreringAsc = Ordering.by[(String,Int),Int](-_._2)
    println("�鿴��Ƶ�׵Ĵʣ� "+tokenCountsFilteredSize.top(20)(oreringAsc).mkString("\n"))

    /**
     * (lnegth,1)
     * (bluffing,1)
     * (preload,1)
     * ........
     * ֻ����һ�εĵ�����û�м�ֵ�ģ���Ϊ��Щ��������û���㹻��ѵ�����ݡ�
     * Ӧ����һ�����˺������ų���Щ���ٳ��ֵĵ���
     */
    val rareTokens = tokenCounts.filter{ case (k,v) => v < 2}.map{ case (k,v) => k}.collect().toSet
    val tokenCounttsFilteredAll = tokenCountsFilteredSize.filter{ case ( k,v) => !rareTokens.contains(k)}
    println("�Ƴ��ʹ�Ƶ�ĵ���֮�� "+tokenCounttsFilteredAll.top(20)(oreringAsc).mkString("\n"))
    println("�Ƴ��ʹ�Ƶ���ʣ� "+tokenCounttsFilteredAll.count) //51801
    /**
     * ͨ���ڷִ�������Ӧ��������Щ���˲��裬��������ά�ȴ�402978���͵�51801
     * ���ڰѹ����߼���ϵ�һ�������У���Ӧ�õ�RDD�е�ÿ���ĵ���
     */
    def tokenize(line:String):Seq[String] = {
      line.split("""\W+""")
      .map(_.toLowerCase())
      .filter(token => regex.pattern.matcher(token).matches)
      .filterNot(token => stopwords.contains(token))
      .filterNot(token => rareTokens.contains(token))
      .filter(token => token.size >= 2)
      .toSeq
    }
    //ͨ������Ĵ�����Լ�����溯���Ƿ������ͬ�����
    println("��飺 "+text.flatMap(doc => tokenize(doc)).distinct().count())

    /**
     * ���ǿ��԰�RDD��ÿ���ĵ���������ķ�ʽ�ִ�
     */
    val tokens = text.map(doc => tokenize(doc))
    println("ÿ���ĵ����Խ���� "+tokens.first.take(20))

    /**
     * ������ȡ�ʸ�
     * ��ȡ�ʸ����ı�����ͷִ��бȽϳ��á�����һ������������ת��Ϊһ��������ʽ�������ʸ����ķ��������磬������ʽ����
     * ת��Ϊ������dogs���dog������walking��walker�����Ŀ���ת��Ϊwalk����ȡ�ʸɺܸ��ӣ�һ��ͨ����׼��NLP��������
     * �����������ʵ�֣�����NLTK��OpenNLP��Lucene���������ﲻ������
     */

    /**
     * ѵ��TF-IDFģ��
     * ��������ʹ��MLLib��ÿƪ����ɴ�����ʽ���ĵ���������ʽ����һ����ʹ��HashingTFʵ�֣���ʹ��������ϣ��ÿ������
     * �ı��Ĵ���ӳ��Ϊһ����Ƶ�������±ꡣ֮��ʹ��һ��ȫ�ֵ�IDF�����Ѵ�Ƶ����ת��ΪTF-IDF������
     *
     * ÿ��������±�������ʵĹ�ϣֵ������ӳ�䵽����������ĳ��ά�ȣ��������ֵ�Ǳ����TF-IDFȨ��
     *
     * ���ȣ�����������Ҫ���࣬��UN����һ��HasingTFʵ���������ά�Ȳ���dim��Ĭ������ά����pow(20,20)�����߽ӽ�100�򣩣�
     * �������ѡ��pow(2,18)������26000������Ϊʹ��50000������Ӧ�ò�������ܶ�Ĺ�ϣ��ͻ������С��ά�������ڴ���ٲ���
     * չʾ���������㡣
     */
    import org.apache.spark.mllib.linalg.{SparseVector => SV}
    import org.apache.spark.mllib.feature.HashingTF
    import org.apache.spark.mllib.feature.IDF
    val dim = math.pow(2,18).toInt
    val hashingTF = new HashingTF(dim)
    val tf = hashingTF.transform(tokens)
    tf.cache()

    /**
     * HashingTF��Transform������ÿ�������ĵ�������������У�ӳ�䵽һ��MLlib��Vector����
     * �۲�һ��ת�������ݵĵ�һ��Ԫ�ص���Ϣ
     */
    val v = tf.first().asInstanceOf[SV]
    println("v.size: "+v.size)
    println("v.values.size: " + v.values.size)
    println("v.values.take(10).toSeq: "+ v.values.take(10).toSeq)
    println("v.indices.take(10).toSeq : "+ v.indices.take(10).toSeq)

    /**
     * ����ͨ�������µ�IDFʵ��������RDD��fit���������ô�Ƶ������Ϊ���������Ŀ��е�ÿ�����ʼ��������ı�Ƶ�ʡ�֮��
     * ʹ��IDF��transform����ת����Ƶ����ΪTF-IDF����
     */
    val idf = new IDF().fit(tf)
    val tfidf = idf.transform(tf)
    val v2 = tfidf.first.asInstanceOf[SV]
    println("v2.values.size: " + v2.values.size)
    println("v2.values.take(10)�� "+ v2.values.take(10).toSeq)
    println("v2.indices.take(10).toSeq: "+ v2.indices.take(10).toSeq)

    /**
     * ���� TF-IDFȨ��
     */
    //���ȼ��������ĵ���TF-IDF��С�����Ȩֵ
    val minMaxVals = tfidf.map{ v =>
      val sv  = v.asInstanceOf[SV]
      (sv.values.min,sv.values.max)
    }
    val globalMinMax = minMaxVals.reduce{ case ((min1,max1),(min2,max2)) =>
      (math.min(min1,min2),math.max(max1,max2))
    }
    println("globalMinMax: "+ globalMinMax)
    //�۲�һ�²�ͬ���ʵ�TF-IDF
    //û�й��˵�ͣ�ô�
    val common = sc.parallelize(Seq(Seq("you","do","we")))
    val tfCommon = hashingTF.transform(common)
    val tfidfCommon = idf.transform(tfCommon)
    val  commonVector = tfidfCommon.first().asInstanceOf[SV]
    println("commonVector.values.toSeq: "+ commonVector.values.toSeq)

    //�Բ������ֵĵ���Ӧ����ͬ��ת��
    val uncommon = sc.parallelize(Seq(Seq("telescope","legislation","investment")))
    val tfUncommon = hashingTF.transform(uncommon)
    val tfidfUncommon = idf.transform(tfUncommon)
    val  uncommonVector = tfidfUncommon.first().asInstanceOf[SV]
    println("uncommonVector.values.toSeq: "+ uncommonVector.values.toSeq)

    //Ӧ��һ��ʹ��TF-IDF�����������ı����ƶ�
    /**
     * �������ƶ�
     * �����Ѿ�ͨ��TF-IDF���ı�ת����������ʾ�ˡ�
     *
     * ������Ϊ�����ĵ����еĵ���Խ�����ƶ�Խ�ߣ���֮���ƶ�Խ�͡���Ϊ���ǿ���ͨ���������������ĵ���������������ƶȣ���
     * ÿһ�����������ĵ��еĵ��ʹ��ɣ����Թ��еĵ��ʸ�����ĵ��������ƶ�Ҳ����ߡ�
     * �����������ڴ���ʹ�ǳ���ͬ���ĵ�Ҳ���ܰ����ܶ���ͬ�ĳ��ôʣ�����ͣ�ôʣ���Ȼ������Ϊ�ϵ͵�TF-IDFȨֵ����Щ����
     * ����Ե���Ľ�������ϴ�Ӱ�죬��˲�������ƶȵļ������̫��Ӱ�졣
     *
     * ��������Ԥ�����������������������ѡ������űȽ����ơ�Ȼ��һ���ǲ���������ӣ�
     */
    val hockeyText = rdd.filter{ case (file,text) =>
        file.contains("hockey")
    }
    val hockeyTF = hockeyText.mapValues(doc =>
      hashingTF.transform(tokenize(doc))
    )
    val hockeyTfIdf = idf.transform(hockeyTF.map(_._2))

    /**
     * �����������ĵ������󣬾Ϳ������ѡ�������������������������ǵ��������ƶȣ�ʹ��Breeze�����Դ������� �����Ȱ�MLlib
     * ����ת����Breezeϡ��������
     */
    import breeze.linalg._
    val hockey1 = hockeyTfIdf.sample(true,0.1,42).first.asInstanceOf[SV]
    val breeze1 = new SparseVector(hockey1.indices,hockey1.values,hockey1.size)
    val hockey2 = hockeyTfIdf.sample(true,0.1,43).first.asInstanceOf[SV]
    val breeze2 = new SparseVector(hockey2.indices,hockey2.values,hockey2.size)
    val cosineSim = breeze1.dot(breeze2)/(norm(breeze1))*(norm(breeze2))
    println(cosineSim)  // 0.06
    /**
     * ���ֵ�������ܵ��ˣ������ı������д���Ψһ�ĵ����ܻ�ʹ���������Чά�Ⱥܸߡ���ˣ����ǿ�����Ϊ��ʹ����̸����ͬ����
     * ���ĵ�Ҳ�������Ž�С����ͬ���ʣ�������нϵ͵����ƶȷ�����
     *
     * ��Ϊ���գ����ǿ��Ժ���һ�����������Ƚϣ�����һ���ĵ������������ĵ�������һ���ĵ����ѡ����comp.graphics�����飬
     * ʹ����ȫ��ͬ�ķ�����
     */
    val graphicsText = rdd.filter{ case (file,text) =>
      file.contains("com.graphics")
    }
    val graphicsTF = graphicsText.mapValues { doc =>
      hashingTF.transform(tokenize(doc))
    }
    val graphicsTfIdf = idf.transform(graphicsTF.map(_._2))
    val graphics = graphicsTfIdf.sample(true,0.1,42).first.asInstanceOf[SV]
    val breezeGraphics = new SparseVector(graphics.indices,graphics.values,graphics.size)
    val cosineSim2 = breeze1.dot(breezeGraphics)/(norm(breeze1)) * (norm(breezeGraphics))
    println(cosineSim2)  // 0.0047
    /**
     * ���һƪ�˶���ػ�������ĵ��ܿ��ܻغ��������ĵ��нϸߵ����ƶȡ�������ϣ��̸�۵��ĵ���Ӧ�ú�̸����������ĵ�
     * ��ô���ơ�����ͨ��������˵���Ƿ���ˣ�
     */
    val baseballText = rdd.filter{ case (file,text) =>
      file.contains("com.baseball")
    }
    val baseballTF = baseballText.mapValues { doc =>
      hashingTF.transform(tokenize(doc))
    }
    val baseballTfIdf = idf.transform(baseballTF.map(_._2))
    val baseball = baseballTfIdf.sample(true,0.1,42).first.asInstanceOf[SV]
    val breezeBaseball = new SparseVector(baseball.indices,baseball.values,baseball.size)
    val cosineSim3 = breeze1.dot(breezeBaseball)/(norm(breeze1)) * (norm(breezeBaseball))
    println(cosineSim3)  // 0.05
    //�����ҵ��İ�����������ĵ����������ƶ���0.05����comop.graphics�ĵ�����Ѿ��ܸߣ����Ǻ���һƪ�������ĵ������ϵ�
  }

}
