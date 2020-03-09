logLevel := Level.Warn

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.1")
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.3.7")