/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.connection.irc

import java.net.InetSocketAddress
import akka.actor._

class IrcServer(address: InetSocketAddress, charset: String, clientFactory: IO.WriteHandle => ActorRef) extends Actor with ActorLogging {

  val state = IO.IterateeRef.Map.async[IO.Handle]()(context.dispatcher)

  override def preStart {
    IOManager(context.system) listen address
  }

  def receive = {
    case IO.NewClient(server) ⇒
      val socket = server.accept()
      val client = clientFactory(socket.asWritable)
      state(socket) flatMap (_ ⇒ IrcServer.processRequest(client, socket, charset))

    case IO.Read(socket, bytes) ⇒
      state(socket)(IO Chunk bytes)

    case IO.Closed(socket, cause) ⇒
      state(socket)(IO EOF None)
      state -= socket
  }
}

object IrcServer{
  val seps = "\r\n".map{_.toByte}

  def readLn(charset: String) =
    for {
      drop <- IO takeWhile(b => seps.contains(b))
      msg <- IO takeWhile(b => !seps.contains(b))
    } yield msg.decodeString(charset)

  def processRequest(client: ActorRef, socket: IO.SocketHandle, charset: String): IO.Iteratee[Unit] = {
    IO repeat {
      for {
        msg <- readLn(charset)
      } yield {
        client ! msg
      }
    }
  }
}