package me.shoma.backlog.webhook.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import me.shoma.backlog.webhook.slack.datas.{SlackWebhook, WebhookRequest, ContentType}

import scala.concurrent.{ExecutionContext, Future}

trait Controller extends WebhookMarshalling {

  val reqHeaders: Seq[HttpHeader] = Seq(
    headers.`Content-Type`(ContentTypes.`application/json`),
    headers.`Accept-Charset`(HttpCharsets.`UTF-8`)
  )

  def webhookToSlack(webhookRequest: WebhookRequest)
                    (implicit actorSystem: ActorSystem, mat: Materializer, exc: ExecutionContext): Future[HttpResponse] = {

    import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

    val config  = ConfigFactory.load()
    val url     = config.getString("slack.url")
    val user = webhookRequest.createdUser.name
    val text = webhookRequest.contentType match {
      case ContentType.AddIssue => s"${user}が課題を追加しました。"
      case _ => ""
    }
    val data = SlackWebhook(text)

    Marshal(data).to[RequestEntity] flatMap { entity =>
      val request = HttpRequest(method = HttpMethods.POST, uri = Uri(url), headers = reqHeaders.toList, entity = entity)
      Http().singleRequest(request)
    }
  }

}
