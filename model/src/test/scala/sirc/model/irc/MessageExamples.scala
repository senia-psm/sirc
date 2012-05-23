/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.model.irc

import sirc.model.irc.KnownMessage._
object MessageExamples {

  case class IrcExample(str: String, msg: Message)

  val difficultExamples =
    Seq(
      IrcExample(
        "command " + (1 to 16).mkString(" "),
        RawMessage(
          None,
          "command",
          (1 to 14) map {_.toString},
          Some("15 16")))
    )
  
  val examples =
    Seq(
      IrcExample(
        ":nickname!user@host command 1 2 3 4",
        RawMessage(
          Some(Nickname("nickname", Some("user"), Some("host"))),
          "command",
          Seq("1", "2", "3", "4"))),


      IrcExample(
        ":nickname!user@host command 1 2 3 4 :a b c",
        RawMessage(
          Some(Nickname("nickname", Some("user"), Some("host"))),
          "command",
          Seq("1", "2", "3", "4"), Some("a b c"))),

      IrcExample("PASS abc123", Password("abc123")),
	  IrcExample(":servername PASS avc124", Password("avc124", Some(SimplePrefix("servername")))),
      IrcExample("NICK myNick", Nick("myNick"))
    )

  val wrongExamples =
    Seq(
      "nickname!user@host command 1 2 3 4",
      ":nickname!user command 1 2 3 4"
    )
}
