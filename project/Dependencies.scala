import sbt._

object Dependencies {

  val blindsight = "2.0.0"
  val terseLogback = "1.2.0"
  val blacklite = "1.0.1"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.8"

  // Basic Logback
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.11"
  val logstashLogbackEncoder = "net.logstash.logback" % "logstash-logback-encoder" % "7.3"
  val janino = "org.codehaus.janino" % "janino" % "3.0.11"
  val jansi  = "org.fusesource.jansi" % "jansi" % "1.17.1"
  val julToSlf4j = "org.slf4j" % "jul-to-slf4j" % "1.7.30"

  // https://github.com/tersesystems/blacklite#logback
  val blackliteLogback = "com.tersesystems.blacklite" % "blacklite-logback" % blacklite
  // https://github.com/tersesystems/blacklite#codec
  //val blackliteZtdCodec = "com.tersesystems.blacklite" % "blacklite-codec-zstd" % blacklite

  // https://tersesystems.github.io/terse-logback/
  val terseLogbackClassic = "com.tersesystems.logback" % "logback-classic" % terseLogback
  val logbackUniqueId = "com.tersesystems.logback" % "logback-uniqueid-appender" % terseLogback

  // https://tersesystems.github.io/blindsight/usage/inspections.html
  val blindsightInspection = "com.tersesystems.blindsight" %% "blindsight-inspection" % blindsight
  // https://tersesystems.github.io/blindsight/usage/scripting.html
  val blindsightScripting = "com.tersesystems.blindsight" %% "blindsight-scripting" % blindsight

  val blindsightApi = "com.tersesystems.blindsight" %% "blindsight-api" % blindsight
  val blindsightDsl = "com.tersesystems.blindsight" %% "blindsight-dsl" % blindsight
  val blindsightLogstash = "com.tersesystems.blindsight" %% "blindsight-logstash" % blindsight
}
