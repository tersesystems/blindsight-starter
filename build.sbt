import Dependencies._

ThisBuild / scalaVersion     := "2.13.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "blindsight-starter",

    // internal jdk libraries use java util logging
    libraryDependencies += julToSlf4j,

    // Basic logback
    libraryDependencies += logbackClassic,
    libraryDependencies += logstashLogbackEncoder,
    libraryDependencies += janino,
    libraryDependencies += jansi,
    
    // sqlite appender
    libraryDependencies += blackliteLogback,

    // terse-logback utilities
    libraryDependencies += terseLogbackClassic,
    libraryDependencies += logbackUniqueId,

    // Blindsight API and logstash-logback-encoder integration
    libraryDependencies += blindsightLogstash,
    libraryDependencies += blindsightInspection,
    libraryDependencies += blindsightScripting,

    libraryDependencies += scalaTest % Test
  )

