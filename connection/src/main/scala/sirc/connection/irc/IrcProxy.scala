/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.connection.irc

import akka.actor._
import java.net.{Socket, InetSocketAddress}
import akka.util.ByteString


class IrcProxy(clientSide: InetSocketAddress, serverSide: InetSocketAddress) extends Actor with ActorLogging {

  def init[T](t: T)(f: T => Unit): T = {
    f(t)
    t
  }

  def riseProxy(clientOut: IO.WriteHandle): ActorRef = {
    lazy val serverListener = context.actorOf(Props(new Actor with ActorLogging {
      def receive = {
        case s: String =>
          log.info("received from server: " + s)
          clientOut.write(ByteString(s + "\n"))
      }
    }), "serverListener")

    lazy val serverSocket = context.actorOf(Props(
      new IrcSocket(
        init(new Socket()){_.connect(serverSide)},
        serverListener
      )
    ), name = "server_socket")

    lazy val clientHandler = context.actorOf(Props(new Actor with ActorLogging {
      def receive = {
        case s: String =>
          log.info("received from client: " + s)
          serverSocket ! Send(s)
      }
    }), name = "clientHandler")

    clientHandler
  }
  var proxyServer = context.actorOf(Props(new IrcServer(clientSide, "UTF-8", riseProxy)), "proxy")

  def receive = {
    case "stop" => context.stop(self)
  }
}
