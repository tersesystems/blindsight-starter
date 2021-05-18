import sbt._

object Dependencies {

  val blindsight = "1.5.1"
  val terseLogback = "0.16.2"
  val blacklite = "1.0.1"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.8"

  // Basic Logback
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val logstashLogbackEncoder = "net.logstash.logback" % "logstash-logback-encoder" % "6.6"
  val janino = "org.codehaus.janino" % "janino" % "3.0.11"
  val jansi  = "org.fusesource.jansi" % "jansi" % "1.17.1"

  // SQLite appender
  val blackliteLogback = "com.tersesystems.blacklite" % "blacklite-logback" % blacklite

  // terse-logback utilties
  val terseLogbackClassic = "com.tersesystems.logback" % "logback-classic" % terseLogback
  val logbackUniqueId = "com.tersesystems.logback" % "logback-uniqueid-appender" % terseLogback

  // blindsight API and integration with logstash-logback-encoder
  val blindsightLogstash = "com.tersesystems.blindsight" %% "blindsight-logstash" % blindsight
  val blindsightInspection = "com.tersesystems.blindsight" %% "blindsight-inspection" % blindsight
  val blindsightScripting = "com.tersesystems.blindsight" %% "blindsight-scripting" % blindsight
}