name := "textclassifier"

version := "0.1"

scalaVersion := "2.13.1"

//testing
libraryDependencies += "org.specs2" %% "specs2-core" % "4.8.3"
libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.8.3"
//serialization
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"
libraryDependencies += "com.opencsv" % "opencsv" % "5.1"
//text analyze
libraryDependencies += "org.apache.lucene" % "lucene-analyzers-common" % "7.2.1"
//network
libraryDependencies += "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.2"
//html parsing
libraryDependencies += "org.jsoup" % "jsoup" % "1.13.1"

enablePlugins(JmhPlugin)
