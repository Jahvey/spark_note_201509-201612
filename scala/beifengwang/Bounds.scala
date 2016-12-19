package scala.beifengwang

/**
 * Created by Administrator on 2016/8/1.
 * ��ָ���������͵�ʱ����ʱ��������Ҫ�Է������͵ķ�Χ���н綨�������ǿ�������������͡����磬���ǿ���Ҫ��ĳ���������ͣ�
 * ���ͱ�����ĳ��������࣬�����ڳ����оͿ��Է��ĵص��÷������ͼ̳еĸ���ķ������������������ʹ�ú����С���ʱ�Ϳ���ʹ��
 * ���±߽�Bounds�����ԡ�
 * Scala�����±߽��������������ͱ�����ĳ��������࣬���߱�����ĳ����ĸ���
 */
object Bounds {

  def main(args: Array[String]) {

    val a = new Student("A")
    val b = new Person("B")
    val party = new Party(a,b)
    party.play

    getLostIDCard(b)
    val jack = new Father("jack")
    val xx = new Child("xx")
    getLostIDCard(jack)
    getLostIDCard(xx)

    val dog = new Dog("XiaoHuang")
    new Party2(a,dog)//�ɹ����У�������

    val calculator = new Calculator(1,2)
    println(calculator.max)

    val gongbaojiding = new Meat("gongbaojiding")
    val yuxiangrousi = new Meat("yuxiangrousi")
    val meatPackage = packageFood(gongbaojiding,yuxiangrousi)

    val card_Master = new Card[Master]("Master")
    val card_Professional = new Card[Professional]("Professional")
    enterMeet(card_Master)
    enterMeet(card_Professional)

    val card1_Master = new Card1[Master]("Master")
    val card1_Professional = new Card1[Professional]("Professional")
    enterMeet1(card1_Master)
    enterMeet1(card1_Professional)

  }


  /**�ϱ߽�*/
  class Person(val name:String) {
    def sayHello = println("Hello,i'm " + name)
    def makeFriends(p:Person): Unit = {
      sayHello
      p.sayHello
    }
  }

  class Student(name:String) extends Person(name)

  class Party[T <: Person](p1:T,p2:T){ //�ϱ߽��ʹ��
    def play = p1.makeFriends(p2)
  }

  /**�±߽�Bounds
    * �����ƶ��������͵��ϱ߽磬������ָ���±߽磬��ָ���������ͱ�����ĳ����ĸ���
    * ���磺����������ȡ���֤�������Ǳ����Լ�ȥ��Ҳ���������ĸ��׹�ȥ��ȡ
    * */

  class Father(val name:String)

  class Child(name:String) extends Father(name)

  def getLostIDCard[T >: Child](p:T): Unit = {
    if(p.getClass == classOf[Child]) println("please tell us your parents names")
    else if(p.getClass == classOf[Father]) println("please sign your name to get your child's lost id card.")
    else println("sorry,you are not allowed to get this id card.")
  }

  /**
   * ���±߽�Bounds����Ȼ������һ�ַ������ͣ�֧���и��ӹ�ϵ�Ķ������͡����ǣ���ĳ���������±߽�Boundsָ���ĸ���
   * ���ͷ�Χ�ڵ��඼û���κι�ϵ����Ĭ���ǿ϶����ܽ��ܵġ�
   * Ȼ����View Bounds��Ϊһ�����±߽�Bounds�ļ�ǿ�棬֧�ֿ��Զ����ͽ�����ʽת������ָ�������ͽ�����ʽת�������ж�
   * �Ƿ��ڱ߽�����ͷ�Χ��*/
  class Dog(val name:String){
    def sayHello = println("Wang,Wang,i'm " + name)
  }
  implicit def dog2person(obj:Object):Person = {
    if(obj.isInstanceOf[Dog]) {
      val dog = obj.asInstanceOf[Dog]
      new Person(dog.name)
    } else
      Nil
  }
  class Party2[T <% Person](p1:T,p2:T)

  /**
   * Context Bounds��һ�������Bounds��������ݷ������͵����������硰T�����͡�Ҫ��������һ������Ϊ�����͡�T������
   * ��ʽֵ����ʵ������Ϊ��Context Bounds֮���Խ�Context������Ϊ�����ڵ���һ��ȫ�ֵ������ģ���Ҫʹ�õ������ĵ�
   * ��ʽֵ�Լ�ע�롣
   *
   * ���磺ʹ��Scala���õıȽ����Ƚϴ�С
   */

  class Calculator[T:Ordering](val number1:T,val number2:T) {
    def max(implicit order:Ordering[T]) = if(order.compare(number1,number2)>0) number1 else number2
  }

  /**
   * ��Scala�У����Ҫʵ����һ���������飬�ͱ���ʹ��Manifest Context Bounds��Ҳ����˵���������Ԫ������ΪT�Ļ�����ҪΪ
   * ����ߺ������塾T:Manifest���������ͣ���ʵ������൱��Context Bound��һ������������������ʵ����Array[T]���ַ�������
   *
   * ���磺������ˣ�һ��ʳƷ���һ����
   * */

  class Meat(val name:String)

  class Vegetable(val name:String)

  def packageFood[T:Manifest](food:T*) = {
    val foodPackage = new Array[T](food.length)
    for(i <- 0 until food.length) foodPackage(i) = food(i)
    foodPackage
  }

  /**
   *
   * ���磺����᳡
   */

  class Master
  class Professional extends Master
  //��ʦ�Լ���ʦ����һ�µ���Ƭ�����Խ���᳡
  class Card[+T](val name:String) //��仰ʲô��˼�أ����� val a = new Card[Master]("A")
  //val b = new Card[Propfessional]("B) ,��ΪMaster��Professional�ĸ��࣬����Card��Master��
  //Ҳ��Card��Professional���ĸ��࣬�����Э��,�������Ϊclass Card[T](val name:String)������������
  //����ʱ��a��ȷ��b�ʹ�����

  def enterMeet(card:Card[Master]): Unit ={
    println("welcome to have this meeting!")
  }

  //���
  class Card1[-T](val name:String)
  def enterMeet1(card:Card1[Professional]): Unit ={
    println("welcome to have this meeting!")
  }

  /**
   * Э�������ܽ�һ�£�
   * ����˵����
   * ���Master��Professional�ĸ��࣬����Card[Master]��Card[Professional]�ĸ��࣬�����Э��
   * ���Master��Professional�ĸ��࣬����Card[Master]��Card[Professional]�����࣬�����Э��
   */

  /**
   * ��Scala���һ����������Ͳ���������Existential Type�����������͡�
   *
   * Array��T��
   * Array��_��Ҳ���Ǵ���һ������
   * */
}















