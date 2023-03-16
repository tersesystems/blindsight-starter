package example

// Always import the following for ease of use :-)
import com.tersesystems.blindsight._
import com.tersesystems.blindsight.DSL._

import java.time.Instant
import java.util.Date

object Runner {
  def main(args: Array[String]): Unit = {
    startLogback()  

    new SimpleExample().run()

    stopLogback()
  }

  case class Person(name: String, age: Int)
  object Person {
    implicit val personToArgument: ToArgument[Person] = { person =>
      Argument(
        bobj(
          ("name" -> person.name),
          ("age" -> person.age)
        )
      )
    }
  }

  implicit val dateToArgument: ToArgument[java.util.Date] = ToArgument { date =>
    Argument(date.getTime)
  }

  implicit val instantToArgument: ToArgument[java.time.Instant] = ToArgument { instant =>
    Argument(instant.toString)
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
      logger.info("Using args {} {} {}", Person("will", 12), new Date(), Instant.now())
      logger.info("Using Arguments({} {} {})", Person("will", 12), new Date(), Instant.now())

      val fluent = logger.fluent
      fluent.info.message("{}").argument(new Date()).log()
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
