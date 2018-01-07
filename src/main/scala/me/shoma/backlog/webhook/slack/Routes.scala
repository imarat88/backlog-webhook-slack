package me.shoma.backlog.webhook.slack

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import me.shoma.backlog.webhook.slack.datas.WebhookRequest

trait Routes extends WebhookMarshalling {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  def routes: Route =
    pathEndOrSingleSlash {
      post {
        entity(as[WebhookRequest]) { request =>
          println(request)
          complete(request)
        }
      }
    }
}
