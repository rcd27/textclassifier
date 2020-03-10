name := "textclassifier"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.8.3"
libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.8.3"
libraryDependencies += "com.opencsv" % "opencsv" % "5.1"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"
libraryDependencies += "org.apache.lucene" % "lucene-analyzers-common" % "8.4.1"

enablePlugins(JmhPlugin)
