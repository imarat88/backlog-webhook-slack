package me.shoma.backlog.webhook.slack

import me.shoma.backlog.webhook.slack.datas._
import spray.json._

trait WebhookMarshalling extends DefaultJsonProtocol {

  implicit val webhookRequestFormat: RootJsonFormat[WebhookRequest] = jsonFormat1(WebhookRequest)
}
