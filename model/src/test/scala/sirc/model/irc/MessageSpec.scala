/**
 * Copyright (c) 2012. Semjon Popugaev (senia).
 * Licensed under the GPLv3.
 * See the LICENSE file for details.
 */

package sirc.model.irc

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class MessageSpec extends FunSpec with ShouldMatchers {

  describe("A message") {

    it("should produse correct irc string") {
      for (example <- MessageExamples.examples)
        example.str should (
          equal(example.msg.toIrc) or
          equal(example.msg.toIrc.dropRight(example.msg.inner._4.map(_.length + 1).getOrElse(0)) +
                example.msg.inner._4.getOrElse("")) //without ":" before last parameter
        )
    }
  }
}
