package example

// Always import the following for ease of use :-)
import com.tersesystems.blindsight._
import com.tersesystems.blindsight.DSL._

object Runner {
  def main(args: Array[String]): Unit = {
    startLogback()  

    // Demonstrate that JUL and SLF4J go to same logging outputs
    new JULExample().run()
    new SLF4JExample().run()
    
    new SimpleExample().run()
    //new ConditionalExample().run()
    //new ContextualExample().run()
    //new IntentionsExample().run()
    //new ScriptExample().run()
    //new FlowExample().run()
    
    stopLogback()
  }

  class JULExample extends Runnable {
    import com.tersesystems.logback.classic._

    private val logger = java.util.logging.Logger.getLogger(getClass.getName)
    def run(): Unit = {
      logger.info("JUL statement")
      // you wouldn't see this ordinarily, but can use ChangeLogLevel to turn it on
      // and the LevelChangePropagator will handle it (do not use jul.Logger.setLevel)
      val changer = new ChangeLogLevel()
      changer.changeLogLevel(getClass.getName, "DEBUG")
      logger.log(java.util.logging.Level.FINE, "FINE JUL statement")
    }
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

  class FlowExample extends Runnable {
    import com.tersesystems.blindsight.flow._

    private val logger: Logger = LoggerFactory.getLogger
    implicit def flowBehavior[B: ToArgument]: FlowBehavior[B] = new XLoggerFlowBehavior()

    def run(): Unit = {
      val result = flowMethod(1, 1)
    }

    def flowMethod(arg1: Int, arg2: Int): Int = logger.flow.info {
        arg1 + arg2
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
    import com.tersesystems.blindsight.scripting._

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

  private def loggerContext = {
    import ch.qos.logback.classic.LoggerContext 
    org.slf4j.LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  }

  def startLogback() {
    // startLogback should run in main class static block or as first statement
    // you need this to ensure you initialize Logback **before** JUL logging.
    loggerContext
  }

  def stopLogback(): Unit = {
    // ideally stop explicitly for async loggers (there is also a shutdown hook just in case)
    // http://logback.qos.ch/manual/configuration.html#stopContext
    loggerContext.stop()
  }

}
