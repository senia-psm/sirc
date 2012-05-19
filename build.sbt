name := "sirc"

version := "0.0.1"

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "[0.6.9,)"  withSources() withJavadoc()

libraryDependencies += "org.scalatest" %% "scalatest" % "[1.7.2,)" % "test"

scalacOptions += "-deprecation"
