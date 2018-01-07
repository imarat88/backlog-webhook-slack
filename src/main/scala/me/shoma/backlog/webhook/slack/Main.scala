package me.shoma.backlog.webhook.slack

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContextExecutor, Future}

object Main extends App
    with RequestTimeout
    with Routes {

  implicit val system: ActorSystem              = ActorSystem()
  implicit val ec: ExecutionContextExecutor     = system.dispatcher
  implicit val materializer: ActorMaterializer  = ActorMaterializer()

  val config  = ConfigFactory.load()
  val host    = config.getString("http.host")
  val port    = config.getInt("http.port")

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, host, port)

  val log = Logging(system.eventStream, "backlog-webhook")

  bindingFuture.map { serverBinding =>
    log.info(s"RestApi bound to ${serverBinding.localAddress} ")
  }.onComplete {
    case scala.util.Success(_) =>
      log.info("Success to bind to {}:{}", host, port)
    case scala.util.Failure(ex) =>
      log.error(ex, "Failed to bind to {}:{}!", host, port)
      system.terminate()
  }
}
