/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.model.irc

sealed abstract class UserMode(s: String) {
  def toIrc = s
}

object UserMode{
  lazy val all = Set(Away, Invisible, Wallops, Restricted, OperatorFlag, LocalOperatorFlag, ServerNotices)
  lazy val map: Map[String, UserMode] = all.map(m => (m.toIrc, m))(collection.breakOut)

  def unapply(s: String): Option[UserMode] = map.get(s)

  //a - user is flagged as away;
  case object Away extends UserMode("a")

  //i - marks a users as invisible;
  case object Invisible extends UserMode("i")

  //w - user receives wallops;
  case object Wallops extends UserMode("w")

  //r - restricted user connection;
  case object Restricted extends UserMode("r")

  //o - operator flag;
  case object OperatorFlag extends UserMode("o")

  //O - local operator flag;
  case object LocalOperatorFlag extends UserMode("O")

  //s - marks a user for receipt of server notices.
  case object ServerNotices extends UserMode("s")
}

