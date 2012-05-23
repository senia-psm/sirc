/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.model.irc

import org.scalatest.FunSpec
import org.scalatest.matchers.{ShouldMatchers, MatchResult, Matcher}

class ParserSpec extends FunSpec with ShouldMatchers {

  def beSuccessful(s: String) = new Matcher[Parser.ParseResult[Message]] {
      def apply(r: Parser.ParseResult[Message]) =
        MatchResult(
          r.successful,
          "Parse result of \"" + s + "\" was not successful: " + r,
          "Parse result of \"" + s + "\" was successful: " + r
        )
    }

  describe("A Parser") {

    it("should parse simple examples") {
      for (example <- MessageExamples.examples)
        Parser(example.str) should beSuccessful(example.str)
    }

    it("should fail on wrong examples") {
      for (example <- MessageExamples.wrongExamples)
        Parser(example) should not (beSuccessful(example))
    }

    it("should parse correctly") {
      for (example <- MessageExamples.examples)
        assert(Parser(example.str).get === RawMessage(example.msg.inner))
    }

    it("should parse result of 'toIrc' method") {
      for (example <- MessageExamples.examples)
        Parser(example.msg.toIrc) should beSuccessful(example.msg.toIrc)
    }

    it("should generate same message from result of 'toIrc' method") {
      for (example <- MessageExamples.examples)
        assert(Parser(example.msg.toIrc).get === RawMessage(example.msg.inner))
    }

    it("should make coffee") (pending)
  }
}
