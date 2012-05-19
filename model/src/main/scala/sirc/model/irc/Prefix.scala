package sirc.model.irc

/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

sealed abstract class Prefix { def toIrc: String }
case class SimplePrefix(name: String) extends Prefix { val toIrc: String = name } //server name or nickname
case class Nickname(name: String, user: Option[String] = None, host: Option[String] = None) extends Prefix {
  require(user == None || host != None, "Can't handle user without host.")

  lazy val toIrc: String = name + user.map("!" + _).mkString + host.map("@" + _).mkString
}
