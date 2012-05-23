package sirc.model.irc

/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

import scala.util.parsing.combinator._

object Parser extends JavaTokenParsers{
  override val skipWhitespace = false;

  private def isIpv4(as: String*) = as(0).toInt > 0 && as.map{_.toInt}.forall{ i => i >=0 && i <= 255}
  private def isIpv6(as: Seq[String], b: Option[String], cs: Seq[String]) = b match {
    case None => as.length + cs.length == 8
    case _ => as.length + cs.length < 8
  }

  def message: Parser[Message] = opt(":" ~> prefix) ~ command ~ params ^^ { case p ~ c ~ par => RawMessage(p, c, par)}
  def command: Parser[String] = """[A-Za-z]{1,}""".r | """\d{3}""".r
  def prefix: Parser[Prefix] = servername <~ " " | fullNickname <~ " "
  def servername: Parser[SimplePrefix] = hostname ^^ SimplePrefix
  def hostname: Parser[String] = repsep(shortname, ".") ^^ {_.mkString(".")}
  def shortname: Parser[String] = """[A-Za-z\d][A-Za-z\d-]*[A-Za-z\d]""".r

  def fullNickname: Parser[Nickname] = nickname ~ opt(opt("!" ~> user) ~ "@" ~ host) ^^ {
    case n ~ s => Nickname(n, s.flatMap{_._1._1}, s.map{_._2})
  }

  def nickname: Parser[String] = """[A-Za-z\[\]\\`_\^{|}][A-Za-z\d\[\]\\`_\^{|}-]*""".r
  def host: Parser[String] = hostname | hostaddr

  def hostaddr: Parser[String] = ip4addr | ipv6addr
  def ip4addr: Parser[String] = """\d{1,3}""".r ~ repN(3, "." ~> """\d{1,3}""".r) ^? (
      { case a ~ (b :: c :: d :: Nil) if isIpv4(a, b, c, d) => Seq(a,b,c,d).mkString(".") },
      { case a ~ bs => "Not a valid ipv4 address: " + (a :: bs).mkString(".")}
    )
  def ipv6addr: Parser[String] = repsep("""[\da-fA-F]{1,4}""".r, ":") ~ "::".? ~ repsep("""[\da-fA-F]{1,4}""".r, ":") ^? (
      { case as ~ b ~ cs if isIpv6(as, b, cs) => as.mkString(":") + b.mkString + cs.mkString(":")},
      { case as ~ b ~ cs => "Not a valid ipv6 address: " + as.mkString(":") + b.mkString + cs.mkString(":")}
    )

  def params: Parser[Seq[String]] = paramsMax | paramsComm
  def paramsMax: Parser[Seq[String]] = repN(14, " " ~> middle) ~ opt(" " ~> ":".? ~ trailing ) ^? (
      {
        case ms ~ Some(None ~ t) if t.head != ':' => ms :+ t
        case ms ~ ot => ms ++ ot.map{_._2}
      },
      {"Leading \":\" don't belongs to last parameter: " + _}
    )
  def paramsComm: Parser[Seq[String]] = rep(" " ~> middle) ~ opt(" :" ~> trailing ) ^? (
      { case ms ~ t if ms.length <= 14 => ms ++ t },
      { "Too many parameters: " + _ }
    )
  lazy val middle: Parser[String] = """[^ :\n][^ \n]*""".r
  lazy val trailing: Parser[String] = """[^\n]*""".r
  //  lazy val letter: Parser[String] = """[A-Za-z]""".r
  //  lazy val digit: Parser[String] = """\d""".r
  //  lazy val special: Parser[String] = """[\[\]\\`_\^{|}]""".r
  lazy val user: Parser[String] = """[^\n @]*""".r

  def apply(s: String) = parseAll(message, s)
  def parseFullNickname(s: String) = parseAll(fullNickname, s)
}
