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

sealed abstract class KnownMessage(prefix: Option[Prefix],
                                   command: String,
                                   params: Seq[String],
                                   lastParam: Option[String] = None)
  extends Message(prefix, command, params, lastParam)

//  ERR_NEEDMOREPARAMS, ERR_ALREADYREGISTRED
case class Password(password: String, prefix: Option[Prefix] = None) extends KnownMessage(prefix, "PASS", password :: Nil)

// ERR_NONICKNAMEGIVEN, ERR_ERRONEUSNICKNAME, ERR_NICKNAMEINUSE, ERR_NICKCOLLISION, ERR_UNAVAILRESOURCE, ERR_RESTRICTED
case class Nick(nick: String, prefix: Option[Prefix] = None) extends KnownMessage(prefix, "NICK", nick :: Nil)

// ERR_NEEDMOREPARAMS, ERR_ALREADYREGISTRED
case class User(user: String,
                mode: String,
                realName: String,
                prefix: Option[Prefix] = None,
                unused: Option[String] = None)
  extends KnownMessage(prefix, "USER", user :: mode :: unused.getOrElse("*") :: Nil, Some(realName))

// ERR_NEEDMOREPARAMS, RPL_YOUREOPER, ERR_NOOPERHOST, ERR_PASSWDMISMATCH
case class Oper(name: String, password: String, prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "OPER", password :: Nil)

// ERR_NEEDMOREPARAMS, ERR_USERSDONTMATCH, ERR_UMODEUNKNOWNFLAG, RPL_UMODEIS
case class ClientMode(nickname: String, modes: Seq[(Option[Boolean], UserMode)], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "MODE", nickname :: modes.map(m => m._1.map(if (_) "+" else "-").mkString + m._2.toIrc) ++: Nil)

// ERR_ALREADYREGISTRED, ERR_NEEDMOREPARAMS, ERR_ERRONEUSNICKNAME, RPL_YOURESERVICE, RPL_YOURHOST, RPL_MYINFO
case class Service(nickname: String,
                   distribution: String,
                   info: String,
                   servType: String = "0",
                   reserved: String = "*",
                   reserved2: String = "0",
                   prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "SERVICE", nickname :: reserved :: distribution :: servType :: reserved2 :: Nil, Some(info))

case class Quit(quitMessage: Option[String] = None, prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "QUIT", Nil, quitMessage)

// ERR_NOPRIVILEGES, ERR_NOSUCHSERVER, ERR_NEEDMOREPARAMS
case class SQuit(server: SimplePrefix, comment: String, prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "SQUIT", server.toIrc :: Nil, Some(comment))

// ERR_NEEDMOREPARAMS, ERR_BANNEDFROMCHAN, ERR_INVITEONLYCHAN, ERR_BADCHANNELKEY, ERR_CHANNELISFULL, ERR_BADCHANMASK,
// ERR_NOSUCHCHANNEL, ERR_TOOMANYCHANNELS, ERR_TOOMANYTARGETS, ERR_UNAVAILRESOURCE, RPL_TOPIC
/** leave all channels */
case class SpecialJoin(prefix: Option[Prefix] = None) extends KnownMessage(prefix, "JOIN", "0" :: Nil)

// ERR_NEEDMOREPARAMS, ERR_BANNEDFROMCHAN, ERR_INVITEONLYCHAN, ERR_BADCHANNELKEY, ERR_CHANNELISFULL, ERR_BADCHANMASK,
// ERR_NOSUCHCHANNEL, ERR_TOOMANYCHANNELS, ERR_TOOMANYTARGETS, ERR_UNAVAILRESOURCE, RPL_TOPIC
case class Join(channels: Seq[String], keys: Seq[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "JOIN", channels.mkString(",") :: keys.mkString(",") :: Nil)

// ERR_NEEDMOREPARAMS, ERR_KEYSET, ERR_NOCHANMODES, ERR_CHANOPRIVSNEEDED, ERR_USERNOTINCHANNEL, ERR_UNKNOWNMODE,
// RPL_CHANNELMODEIS, RPL_BANLIST, RPL_ENDOFBANLIST, RPL_EXCEPTLIST, RPL_ENDOFEXCEPTLIST, RPL_INVITELIST,
// RPL_ENDOFINVITELIST, RPL_UNIQOPIS
case class ChannelMode(channel: String, modes: Seq[(Boolean, ChanMode, Option[String])], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "MODE", modes.flatMap(m => ((if (m._1) "+" else "-") + m._2.toIrc) :: m._3.toList ))

// ERR_NEEDMOREPARAMS, ERR_NOTONCHANNEL, RPL_NOTOPIC, RPL_TOPIC, ERR_CHANOPRIVSNEEDED, ERR_NOCHANMODES
case class Topic(channel: String, topic: Option[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "TOPIC", channel :: Nil, topic)

// ERR_TOOMANYMATCHES, ERR_NOSUCHSERVER, RPL_NAMREPLY, RPL_ENDOFNAMES
case class Names(channels: Seq[String], target: Option[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "NAMES", channels.mkString(",") :: target ++: Nil)

// ERR_TOOMANYMATCHES, ERR_NOSUCHSERVER, RPL_LIST, RPL_LISTEND
case class List(channels: Seq[String], target: Option[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "LIST", channels.mkString(",") :: target ++: Nil)

// ERR_NEEDMOREPARAMS, ERR_NOSUCHNICK, ERR_NOTONCHANNEL, ERR_USERONCHANNEL, ERR_CHANOPRIVSNEEDED, RPL_INVITING, RPL_AWAY
case class Invite(nickname: String, channel: String, prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "INVITE", nickname :: channel :: Nil)

// ERR_NEEDMOREPARAMS, ERR_NOSUCHCHANNEL, ERR_BADCHANMASK, ERR_CHANOPRIVSNEEDED, ERR_USERNOTINCHANNEL, ERR_NOTONCHANNEL
case class Kick(channels: Seq[String], users: Seq[String], comment: Option[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "KICK", channels.mkString(",") :: users.mkString(",") :: Nil, comment)

case class Privmsg(target: String, text: String, prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "PRIVMSG", target :: Nil, Some(text))

case class Notice(target: String, text: String, prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "NOTICE", target :: Nil, Some(text))

case class Motd(target: Option[String], prefix: Option[Prefix] = None) extends KnownMessage(prefix, "MOTD", target ++: Nil)

case class LUsers(mask: Option[String], target: Option[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "LUSERS", target.map(Seq(mask.get, _)).getOrElse(mask.toList))

case class Version(target: Option[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "VERSION", target ++: Nil)

case class Stats(query: Option[String], target: Option[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "STATS", target.map(Seq(query.get, _)).getOrElse(query.toList))

case class Links(remoteServer: Option[String], serverMask: Option[String], prefix: Option[Prefix] = None)
  extends KnownMessage(prefix, "LINKS", remoteServer.map(Seq(_, serverMask.get)).getOrElse(serverMask.toList))