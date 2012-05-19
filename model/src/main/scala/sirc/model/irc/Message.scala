package sirc.model.irc

/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

sealed abstract class Message(prefix: Option[Prefix],
                              command: String,
                              params: Seq[String],
                              lastParam: Option[String] = None) {
  assert(prefix.forall(p => Parser.parseAll(Parser.prefix, p.toIrc + " ").successful), "Not a valid prefix: " + prefix)
  assert(Parser.parseAll(Parser.command, command).successful, "Not a valid command: " + command)
  assert(params.forall(p => Parser.parseAll(Parser.middle, p).successful), "Not valid params: " + params)
  assert(lastParam.forall(p => Parser.parseAll(Parser.trailing, p).successful), "Not a valid lastParam: " + lastParam)

  val inner = (prefix, command, params, lastParam)

  def toIrc: String = prefix.map {":" + _.toIrc + " "}.mkString +
                      command +
                      params.map(" " + _).mkString +
                      lastParam.map {" :" + _}.mkString
}

case class RowMessage(prefix: Option[Prefix],
                      command: String,
                      params: Seq[String],
                      lastParam: Option[String]) extends Message(prefix, command, params, lastParam)

object RowMessage {
  def apply(prefix: Option[Prefix] = None,
            command: String,
            params: Seq[String]): RowMessage =
    if (params.lastOption.map(l => l.head == ':' || l.contains(' ')).getOrElse(false))
      new RowMessage(prefix, command, params.init, params.lastOption)
    else
      new RowMessage(prefix, command, params, None)

  def apply(t: (Option[Prefix], String, Seq[String], Option[String])): RowMessage = new RowMessage(t._1, t._2, t._3, t._4)
}

//  ERR_NEEDMOREPARAMS, ERR_ALREADYREGISTRED
case class Password(password: String, prefix: Option[Prefix] = None) extends Message(prefix, "PASS", password :: Nil)

// ERR_NONICKNAMEGIVEN, ERR_ERRONEUSNICKNAME, ERR_NICKNAMEINUSE, ERR_NICKCOLLISION, ERR_UNAVAILRESOURCE, ERR_RESTRICTED
case class Nick(nick: String, prefix: Option[Prefix] = None) extends Message(prefix, "NICK", nick :: Nil)

