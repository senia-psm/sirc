/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.connection.irc

object TestProxy {
  import sirc.connection.irc._
  import akka.actor._
  import java.net.InetSocketAddress
  import com.typesafe.config.ConfigFactory
  import akka.actor.ActorSystem

  def apply() {
    val customConf = ConfigFactory.parseString("akka { loglevel = DEBUG } ")
    val system = ActorSystem("MySystem", ConfigFactory.load(customConf))
    system.actorOf(Props(new IrcProxy(new InetSocketAddress(6543), new InetSocketAddress("irc.tomsk.net", 6666))))
  }
}
