name := "sirc-connection"

version := "0.0.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0"

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "[0.6.9,)"  withSources() withJavadoc()

libraryDependencies += "org.scalatest" %% "scalatest" % "[1.7.2,)" % "test"

scalacOptions += "-deprecation"
