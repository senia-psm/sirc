/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.model.irc

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
      IrcExample("NICK myNick", Nick("myNick")),

IrcExample("PASS secretpasswordhere", Password("secretpasswordhere")),

IrcExample("NICK Wiz", Nick("Wiz")),
IrcExample(":WiZ!jto@tolsun.oulu.fi NICK Kilroy", Nick("Kilroy", Nickname("WiZ", "jto", "tolsun.oulu.fi").some)),

IrcExample("USER guest 0 * :Ronnie Reagan", User("guest", "0", "Ronnie Reagan")),
IrcExample("USER guest 8 * :Ronnie Reagan", User("guest", "8", "Ronnie Reagan")),

IrcExample("OPER foo bar", Oper("foo", "bar")),

IrcExample("MODE WiZ -w", ClientMode("WiZ", Seq(false.some -> Wallops))),
IrcExample("MODE Angel +i", ClientMode("Angel", Seq(true.some -> Invisible))),
IrcExample("MODE WiZ -o", ClientMode("WiZ", Seq(false.some -> OperatorFlag))),

IrcExample("SERVICE dict * *.fr 0 0 :French Dictionary", Service("dict", "*.fr", "French Dictionary")),

IrcExample("QUIT :Gone to have lunch", Quit("Gone to have lunch")),
IrcExample(":syrk!kalt@millennium.stealth.net QUIT :Gone to have lunch", Quit("Gone to have lunch", Nickname("syrk", "kalt", "millennium.stealth.net").some)),

IrcExample("SQUIT tolsun.oulu.fi :Bad Link", SQuit(SimplePrefix("tolsun.oulu.fi"), "Bad Link")),
IrcExample(":Trillian SQUIT cm22.eng.umd.edu :Server out of control", SQuit(SimplePrefix("cm22.eng.umd.edu"), "Server out of control", SimplePrefix("Trillian").some)),

IrcExample("JOIN 0", SpecialJoin()),

IrcExample("JOIN #foobar", Join(Seq("#foobar"))),
IrcExample("JOIN &foo fubar", Join(Seq("&foo"), Seq("fubar"))),
IrcExample("JOIN #foo,&bar fubar", Join(Seq("#foo", "&bar"), Seq("fubar"))),
IrcExample("JOIN #foo,#bar fubar,foobar", Join(Seq("#foo", "#bar"), Seq("fubar", "foobar"))),
IrcExample("JOIN #foo,#bar", Join(Seq("#foo", "#bar"))),
IrcExample(":WiZ!jto@tolsun.oulu.fi JOIN #Twilight_zone", Join(Seq("#Twilight_zone"), Nickname("WiZ", "jto", "tolsun.oulu.fi").some)),

IrcExample("PART #twilight_zone", Part(Seq("#twilight_zone"))),
IrcExample("PART #oz-ops,&group5", Part(Seq("#oz-ops", "&group5"))),
IrcExample(":WiZ!jto@tolsun.oulu.fi PART #playzone :I lost", Part(Seq("#playzone"), "I lost".some, Nickname("WiZ", "jto", "tolsun.oulu.fi").some)),

IrcExample("MODE #Finnish +imI *!*@*.fi", ChannelMode("#Finnish", Seq((true, InviteOnly, none[String]), (true, Moderated, none[String]), (true, Invitation, "*!*@*.fi".some)))),
IrcExample("MODE #Finnish +o Kilroy", ChannelMode("#Finnish", Seq((true, Operator, "Kilroy".some)))),
IrcExample("MODE #Finnish +v Wiz", ChannelMode("#Finnish", Seq((true, Voice, "Wiz".some)))),
IrcExample("MODE #Fins -s", ChannelMode("#Fins", Seq((false, Secret, none[String])))),
IrcExample("MODE #42 +k oulu", ChannelMode("#42", Seq((true, Key, "oulu".some)))),
IrcExample("MODE #42 -k oulu", ChannelMode("#42", Seq((false, Key, "oulu".some)))),
IrcExample("MODE #eu-opers +l 10", ChannelMode("#eu-opers", Seq((true, Limit, "10".some)))),
IrcExample(":WiZ!jto@tolsun.oulu.fi MODE #eu-opers -l", ChannelMode("#eu-opers", Seq((false, Limit, none[String])), Nickname("WiZ", "jto", "tolsun.oulu.fi").some)),
IrcExample("MODE &oulu +b", ChannelMode("&oulu", Seq((true, Ban, none[String])))),
IrcExample("MODE &oulu +b *!*@*", ChannelMode("&oulu", Seq((true, Ban, "*!*@*".some)))),
IrcExample("MODE &oulu +b *!*@*.edu +e *!*@*.bu.edu", ChannelMode("&oulu", Seq((true, Ban, "*!*@*.edu".some), (true, Exception, "*!*@*.bu.edu".some)))),
IrcExample("MODE #bu +be *!*@*.edu *!*@*.bu.edu", ChannelMode("#bu", Seq((true, Ban, "*!*@*.edu".some), (true, Exception, "*!*@*.bu.edu".some)))),
IrcExample("MODE #meditation e", ChannelMode("#meditation", Seq((true, Exception, none[String])))),
IrcExample("MODE #meditation I", ChannelMode("#meditation", Seq((true, Invitation, none[String])))),
IrcExample("MODE !12345ircd O", ChannelMode("!12345ircd", Seq((true, Creator, none[String])))),

IrcExample(":WiZ!jto@tolsun.oulu.fi TOPIC #test :New topic", Topic("#test", "New topic".some, Nickname("WiZ", "jto", "tolsun.oulu.fi"))),
IrcExample("TOPIC #test :another topic", Topic("#test", "another topic".some)),
IrcExample("TOPIC #test :", Topic("#test", "".some)),
IrcExample("TOPIC #test", Topic("#test", none[String])),

IrcExample("NAMES #twilight_zone,#42", Names(Seq("#twilight_zone", "#42"))),
IrcExample("NAMES", Names(Nil)),

IrcExample("LIST", List(Nil)),
IrcExample("LIST #twilight_zone,#42", List(Seq("#twilight_zone", "#42"))),

IrcExample(":Angel!wings@irc.org INVITE Wiz #Dust", Invite("Wiz", "#Dust", Nickname("Angel", "wings", "irc.org").some)),
IrcExample("INVITE Wiz #Twilight_Zone", Invite("Wiz", "#Twilight_Zone")),

IrcExample("KICK &Melbourne Matthew", Kick(Seq("&Melbourne"), Seq("Matthew"))),
IrcExample("KICK #Finnish John :Speaking English", Kick(Seq("#Finnish"), Seq("John"), "Speaking English".some)),
IrcExample(":WiZ!jto@tolsun.oulu.fi KICK #Finnish John", Kick(Seq("#Finnish"), Seq("John"), prefix = Nickname("WiZ", "jto", "tolsun.oulu.fi").some)),

IrcExample(":Angel!wings@irc.org PRIVMSG Wiz :Are you receiving this message ?", Privmsg("Wiz", "Are you receiving this message ?", Nickname("Angel", "wings", "irc.org").some)),
IrcExample("PRIVMSG Angel :yes I'm receiving it !", Privmsg("Angel", "yes I'm receiving it !")),
IrcExample("PRIVMSG jto@tolsun.oulu.fi :Hello !", Privmsg("jto@tolsun.oulu.fi", "Hello !")),
IrcExample("PRIVMSG kalt%millennium.stealth.net@irc.stealth.net :Are you a frog?", Privmsg("kalt%millennium.stealth.net@irc.stealth.net", "Are you a frog?")),
IrcExample("PRIVMSG kalt%millennium.stealth.net :Do you like cheese?", Privmsg("kalt%millennium.stealth.net", "Do you like cheese?")),
IrcExample("PRIVMSG Wiz!jto@tolsun.oulu.fi :Hello !", Privmsg("Wiz!jto@tolsun.oulu.fi", "Hello !")),
IrcExample("PRIVMSG $*.fi :Server tolsun.oulu.fi rebooting.", Privmsg("$*.fi", "Server tolsun.oulu.fi rebooting.")),
IrcExample("PRIVMSG #*.edu :NSFNet is undergoing work, expect interruptions", Privmsg("#*.edu", "NSFNet is undergoing work, expect interruptions")),

IrcExample(":Angel!wings@irc.org NOTICE Wiz :Are you receiving this message ?", Notice("Wiz", "Are you receiving this message ?", Nickname("Angel", "wings", "irc.org").some)),
IrcExample("NOTICE Angel :yes I'm receiving it !", Notice("Angel", "yes I'm receiving it !")),
IrcExample("NOTICE jto@tolsun.oulu.fi :Hello !", Notice("jto@tolsun.oulu.fi", "Hello !")),
IrcExample("NOTICE kalt%millennium.stealth.net@irc.stealth.net :Are you a frog?", Notice("kalt%millennium.stealth.net@irc.stealth.net", "Are you a frog?")),
IrcExample("NOTICE kalt%millennium.stealth.net :Do you like cheese?", Notice("kalt%millennium.stealth.net", "Do you like cheese?")),
IrcExample("NOTICE Wiz!jto@tolsun.oulu.fi :Hello !", Notice("Wiz!jto@tolsun.oulu.fi", "Hello !")),
IrcExample("NOTICE $*.fi :Server tolsun.oulu.fi rebooting.", Notice("$*.fi", "Server tolsun.oulu.fi rebooting.")),
IrcExample("NOTICE #*.edu :NSFNet is undergoing work, expect interruptions", Notice("#*.edu", "NSFNet is undergoing work, expect interruptions")),

IrcExample("VERSION tolsun.oulu.fi", Version("tolsun.oulu.fi")),

IrcExample("STATS m", Stats("m".some)),

IrcExample("LINKS *.au", Links(none[String], "*.au".some)),
IrcExample("LINKS *.edu *.bu.edu", Links("*.edu".some, "*.bu.edu".some)),

IrcExample("TIME tolsun.oulu.fi", Time("tolsun.oulu.fi".some)),

IrcExample("CONNECT tolsun.oulu.fi 6667", Connect("tolsun.oulu.fi", 6667)),

IrcExample("TRACE *.oulu.fi", Trace("*.oulu.fi".some)),

IrcExample("ADMIN tolsun.oulu.fi", Admin("tolsun.oulu.fi".some)),
IrcExample("ADMIN syrk", Admin("tolsun.oulu.fi".some)),

IrcExample("INFO csd.bu.edu", Info("csd.bu.edu".some)),
IrcExample("INFO Angel", Info("Angel".some)),

IrcExample("SQUERY irchelp :HELP privmsg", SQuery("irchelp", "HELP privmsg")),
IrcExample("SQUERY dict@irc.fr :fr2en blaireau", SQuery("dict@irc.fr", "fr2en blaireau")),

IrcExample("WHO *.fi", Who("*.fi".some)),
IrcExample("WHO jto* o", Who("jto*".some, true)),

IrcExample("WHOIS wiz", Whois(Seq("wiz"))),
IrcExample("WHOIS eff.org trillian", Whois(Seq("trillian"), "eff.org".some)),

IrcExample("WHOWAS Wiz", Whowas(Seq("Wiz"))),
IrcExample("WHOWAS Mermaid 9", Whowas(Seq("Mermaid"), 9.some)),
IrcExample("WHOWAS Trillian 1 *.edu", Whowas(Seq("Trillian"), 1.some, "*.edu".some)),

IrcExample("PING tolsun.oulu.fi", Ping("tolsun.oulu.fi")),
IrcExample("PING WiZ tolsun.oulu.fi", Ping("WiZ", "tolsun.oulu.fi".some)),
IrcExample("PING :irc.funet.fi", Ping("irc.funet.fi")),

IrcExample("PONG csd.bu.edu tolsun.oulu.fi", Pong("csd.bu.edu", "tolsun.oulu.fi".some)),

IrcExample("ERROR :Server *.fi already exists", Error("Server *.fi already exists")),

IrcExample("AWAY :Gone to lunch.  Back in 5", Away("Gone to lunch.  Back in 5".some)),

IrcExample("REHASH", Rehash()),

IrcExample("DIE", Die()),

IrcExample("RESTART", Restart()),

IrcExample("SUMMON jto", Summon("jto")),
IrcExample("SUMMON jto tolsun.oulu.fi", Summon("jto", "tolsun.oulu.fi".some)),

IrcExample("USERS eff.org", Users("eff.org".some)),

IrcExample(":csd.bu.edu WALLOPS :Connect '*.uiuc.edu 6667' from Joshua", Wallops("Connect '*.uiuc.edu 6667' from Joshua", SimplePrefix("csd.bu.edu"))),

IrcExample("USERHOST Wiz Michael syrk", Userhost(Seq("Wiz", "Michael", "syrk"))),

IrcExample("ISON phone trillian WiZ jarlek Avalon Angel Monstah syrk", IsOn(Seq("phone", "trillian", "WiZ", "jarlek", "Avalon", "Angel", "Monstah", "syrk")))
    )

  val wrongExamples =
    Seq(
      "nickname!user@host command 1 2 3 4",
      ":nickname!user command 1 2 3 4"
    )
}
