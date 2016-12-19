package scala.beifengwang

/**
 * Created by Administrator on 2016/8/2.
 */
import scala.actors.Actor
object Actor_ {

  def main(args: Array[String]) {

    val helloActor = new HelloActor
    helloActor.start()
    helloActor ! "GuoYuan"

    val userManagerActor = new UserManagerActor
    userManagerActor.start()
    userManagerActor ! Register("GuoYuan","1234")
    userManagerActor ! Login("GuoYuan","1234")

    val guoTelephone = new GuoTelephone
    guoTelephone.start()
    val jackTelephoneActor = new JackTelephoneActor(guoTelephone)
    jackTelephoneActor.start()
  }

  /**
   * Scala�ṩ��Actor trait�������Ǹ�����ؽ���actor���̱߳�̣���Actor trait��������Jav�е�Thread��Runnableһ������
   * �����Ķ��̻߳���ͽӿڡ�����ֻҪ��дActor trait��act����������ʵ���Լ����߳�ִ���壬��Java����дrun�������ơ����⣬
   * ʹ��start������������actor��ʹ�ã����ţ���actor������Ϣ��actor�ڲ�ʹ��receive��ģʽƥ�������Ϣ
   */

  class HelloActor extends Actor {
    def act(){
      while(true){
        receive{
          case name:String => println("Hello, " + name)
        }
      }
    }
  }

  /**
   * ��Ϣ=case class
   * �����û�ע���¼��ע��
   */

  case class Login(username:String,password:String)
  case class Register(username:String,password:String)
  class UserManagerActor extends Actor{
    def act(): Unit = {
      while(true) {
        receive{
          case Login(username,password) => println("login: " + username + ": " + password)
          case Register(username,password) => println("register: " + username + ": " + password)
        }
      }
    }
  }

  /**
   * �������Actor֮��Ҫ�����շ���Ϣ����ôscala�Ľ����ǣ�һ��actor������һ��actor������Ϣʱ��ͬʱ�����Լ������ã�
   * ����actor�յ��Լ�����Ϣʱ��ֱ��ͨ��������Ϣ��actor�����ã������Ը����ظ���Ϣ
   *
   * ���磺��绰
   */
  case class Message(content:String,sender:Actor)
  class GuoTelephone extends Actor {
    def act(): Unit = {
      while(true) {
        receive{
          case Message(content,sender) => {
            println("Guo telephone " + content)
            sender ! "i'm Guo,please call me after 10 minutes"
          }
        }
      }
    }
  }
  class JackTelephoneActor(val guoTelephoneActor: Actor) extends  Actor {
    def act(): Unit = {
      guoTelephoneActor ! Message("Hello,GuoYuan,i'm jack,are you free now?",this)
      receive{
        case response:String => println("jack telephone: " + response)
      }
    }
  }

  /**
   * Ĭ������£���Ϣ�����첽�ģ��������ϣ�����͵���Ϣ��ͬ���ģ����Է����ܺ�һ��Ҫ���Լ����ؽ������ô����ʹ��
   *   !?�ķ�ʽ������Ϣ����    val reply = actor !? message.
   *
   *   ���Ҫ�첽����һ����Ϣ�������ں���Ҫ�����Ϣ�ķ���ֵ����ô����ʹ��Future����!!�﷨��
   *   val future = actor !! message.
   *   val reply = future().
   */

}























