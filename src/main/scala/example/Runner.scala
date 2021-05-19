package example

import ch.qos.logback.classic.LoggerContext
import com.tersesystems.blindsight.scripting.{ScriptBasedLocation, ScriptHandle, ScriptManager}
import com.tersesystems.blindsight.{Argument, Condition, Logger, LoggerFactory, ToArgument, bobj}

object Runner {

  def main(args: Array[String]): Unit = {
    new SLF4JExample().run()
    //new SimpleExample().run()
    //new ConditionalExample().run()
    //new ContextualExample().run()
    //new IntentionsExample().run()
    //new ScriptExample().run()

    stopLogback()
  }

  class SLF4JExample extends Runnable {
    private val logger = org.slf4j.LoggerFactory.getLogger(getClass)
    def run(): Unit = {
      logger.debug("SLF4J API statement")
    }
  }

  case class Person(name: String, age: Int)
  object Person {
    implicit val personToArgument: ToArgument[Person] = ToArgument { person =>
      import com.tersesystems.blindsight.DSL._
      Argument(("name" -> person.name) ~ ("age" -> person.age))
    }
  }

  class SimpleExample extends Runnable {
    private val logger: Logger = LoggerFactory.getLogger

    def run(): Unit = {
      logger.debug("Blindsight statement!")
      val debugMethod: logger.Method = logger.debug
      debugMethod.apply("Only called ifLoggingDebug() == true")
      logger.info { log =>
        log("This is an info statement")
      }
      logger.info("Arguments are both logfmt and JSON {}", Person("will", 12))
    }

  }

  class ConditionalExample extends Runnable {
    private val condition: Condition = Condition.always
    private def runtimeCondition: Boolean = System.currentTimeMillis() % 2 == 0
    private val logger: Logger = LoggerFactory.getLogger.withCondition(condition)

    def run(): Unit = {
      logger.warn("This is a conditional warning")
      logger.info.when(runtimeCondition) { log =>
        log("Only logs on even millis")
      }
    }
  }

  class ContextualExample extends Runnable {
    import com.tersesystems.blindsight.DSL._
    private val logger: Logger = LoggerFactory.getLogger.withMarker(bobj("correlationId" -> 123))

    def run(): Unit = {
      logger.info("This statement has a correlation id in JSON")
    }
  }

  class IntentionsExample extends Runnable {
    import com.tersesystems.blindsight.inspection.InspectionMacros._
    private val logger: Logger = LoggerFactory.getLogger

    def run(): Unit = {
      decorateIfs(dif => logger.debug(s"${dif.code} = ${dif.result}")) {
        if (System.currentTimeMillis() % 17 == 0) {
          println("branch 1")
        } else if (System.getProperty("derp") == null) {
          println("branch 2")
        } else {
          println("else branch")
        }
      }
    }
  }

  class ScriptExample extends Runnable {
    def run(): Unit = {
      val scriptHandle = new ScriptHandle {
        override def isInvalid: Boolean = false
        override val script: String =
          s"""import strings as s from 'std.tf';
            |alias s.index_of as index_of;
            |
            |library blindsight {
            |  function evaluate: (long level, string enc, long line, string file) ->
            |    if (index_of(enc, "ScriptExample") > -1) then true
            |    else false;
            |}
            |""".stripMargin
        override def report(e: Throwable): Unit = e.printStackTrace()
      }
      val sm     = new ScriptManager(scriptHandle)
      val logger = LoggerFactory.getLogger

      val location = new ScriptBasedLocation(sm, true)
      logger.debug.when(location.here) { log =>
        log(s"This only logs if the location.here has ScriptExample!")
      }
    }
  }

  def stopLogback(): Unit = {
    // http://logback.qos.ch/manual/configuration.html#stopContext
    org.slf4j.LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext].stop()
  }

}
