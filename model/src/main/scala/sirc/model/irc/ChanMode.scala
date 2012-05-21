/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.model.irc

sealed abstract class ChanMode(s: String) {
  def toIrc: String = s
}

object ChanMode{
  lazy val all: Set[ChanMode] = Set(Creator, Operator, Voice, Anonymous, InviteOnly, Moderated, NoMsgFromOutside,
                                    Quiet, Private, Secret, Reop, FixTopic, Key, Limit, Ban, Exception, Invitation)
  lazy val map: Map[String, ChanMode] = all.map(m => (m.toIrc, m))(collection.breakOut)

  def unapply(s: String): Option[ChanMode] = map.get(s)

  // O - give "channel creator" status;
  case object Creator extends ChanMode("O")

  // o - give/take channel operator privilege;
  case object Operator extends ChanMode("o")

  // v - give/take the voice privilege;
  case object Voice extends ChanMode("v")

  // a - toggle the anonymous channel flag;
  case object Anonymous extends ChanMode("a")

  // i - toggle the invite-only channel flag;
  case object InviteOnly extends ChanMode("i")

  // m - toggle the moderated channel;
  case object Moderated extends ChanMode("m")

  // n - toggle the no messages to channel from clients on the outside;
  case object NoMsgFromOutside extends ChanMode("n")

  // q - toggle the quiet channel flag;
  case object Quiet extends ChanMode("q")

  // p - toggle the private channel flag;
  case object Private extends ChanMode("p")

  // s - toggle the secret channel flag;
  case object Secret extends ChanMode("s")

  // r - toggle the server reop channel flag;
  case object Reop extends ChanMode("r")

  // t - toggle the topic settable by channel operator only flag;
  case object FixTopic extends ChanMode("t")

  // k - set/remove the channel key (password);
  case object Key extends ChanMode("k")

  // l - set/remove the user limit to channel;
  case object Limit extends ChanMode("l")

  // b - set/remove ban mask to keep users out;
  case object Ban extends ChanMode("b")

  // e - set/remove an exception mask to override a ban mask;
  case object Exception extends ChanMode("e")

  // I - set/remove an invitation mask to automatically override the invite-only flag;
  case object Invitation extends ChanMode("I")

}

