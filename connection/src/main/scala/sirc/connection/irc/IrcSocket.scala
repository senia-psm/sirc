/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.connection.irc

import java.net.Socket
import java.io.{IOException, BufferedReader, InputStreamReader, PrintWriter}
import scala.util.control.Exception.catching
import akka.util.duration._
import Option.{apply => ?}
import akka.actor._

case class AddListener(l: ActorRef)
case class Send(m: String)
case object Close

class IrcSocket(socket: Socket, listeners: ActorRef*) extends Actor with ActorLogging {

  private case class Msg(s: String)

  val out = context.actorOf(Props(new Actor{
    val os = new PrintWriter(socket.getOutputStream, true)
    def receive = {
      case s: String => os println s
      case Close =>
        log.info("Stop")
        socket.close()
        context.stop(self)
    }
  }), name = "outStream")

  val in = context.actorOf(Props(new Actor{
    val is = new BufferedReader(new InputStreamReader(socket.getInputStream))
    val ioCatcher = catching[Option[String]](classOf[IOException])

    def read() {
      ioCatcher.either(?(is.readLine())) match {
        case Left(t) =>
          log.debug("IOException: " + t)
          context.stop(self)
        case Right(Some(s)) => context.parent ! Msg(s)
        case Right(None) => log.warning("Null string message.")
      }
    }

    context.setReceiveTimeout(1 milliseconds)
    def receive = {
      case ReceiveTimeout => read()
    }
  }), name = "inStream")

  context.watch(out)
  context.watch(in)

  var listenersList: List[ActorRef] = List(listeners: _*)

  def receive = {
    case Msg(s) => listenersList.foreach(_ ! s)
    case AddListener(l) => listenersList ::= l
    case Close => out ! Close
    case Send(s) => out ! s
    case Terminated(`in`) =>
      log.error("In terminated")
      out ! Close
    case Terminated(`out`) =>
      log.info("Out terminated")
      context.stop(in)
      context.stop(self)
  }
}
