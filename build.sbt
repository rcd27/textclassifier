name := "textclassifier"

version := "1.0"
scalaVersion := "2.13.1"

lazy val `textclassifier` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"


libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

// Serialization
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"
libraryDependencies += "com.opencsv" % "opencsv" % "5.1"
// Text analyze
libraryDependencies += "org.apache.lucene" % "lucene-analyzers-common" % "7.2.1"
// HTML parsing
libraryDependencies += "org.jsoup" % "jsoup" % "1.13.1"

enablePlugins(JmhPlugin)