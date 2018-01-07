package me.shoma.backlog.webhook.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import me.shoma.backlog.webhook.slack.datas.WebhookRequest

import scala.concurrent.ExecutionContext

trait Routes extends Controller with WebhookMarshalling {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  def routes(implicit actorSystem: ActorSystem, mat: Materializer, exc: ExecutionContext): Route =
    pathEndOrSingleSlash {
      post {
        entity(as[WebhookRequest]) { request =>
          complete(webhookToSlack(request))
        }
      }
    }
}
