package me.shoma.backlog.webhook.slack

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

import me.shoma.backlog.webhook.slack.datas._
import spray.json._

trait WebhookMarshalling extends DefaultJsonProtocol {

  implicit object ContentTypeFormat extends RootJsonFormat[ContentType] {
    override def read(json: JsValue): ContentType = ContentType(json.convertTo[Int])
    override def write(obj: ContentType): JsValue = ???
  }

  implicit object ZonedDateTimeProtocol extends RootJsonFormat[ZonedDateTime] {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.systemDefault)
    def read(json: JsValue): ZonedDateTime = {
      println(json.convertTo[String])
      ZonedDateTime.parse(json.convertTo[String], formatter)
    }
    def write(obj: ZonedDateTime): JsValue = ???
  }

  implicit val contentFormat: RootJsonFormat[Content] = jsonFormat2(Content)
  implicit val userFormat: RootJsonFormat[User]       = jsonFormat1(User)

  implicit object WebhookRequestFormat extends RootJsonFormat[WebhookRequest] {
    override def read(json: JsValue): WebhookRequest = {
      val jsObject = json.asJsObject
      jsObject.getFields("id", "type", "content", "created", "createdUser") match {
        case Seq(JsNumber(id), contentType, content, created, createdUser) => WebhookRequest(
          id          = id.toLong,
          contentType = contentType.convertTo[ContentType],
          content     = content.convertTo[Content],
          createdUser = createdUser.convertTo[User],
          created     = created.convertTo[ZonedDateTime]
        )
        case other => deserializationError("Cannot deserialize WebhookRequest: invalid input. Raw input: " + other)
      }
    }

    override def write(obj: WebhookRequest): JsValue = ???
  }
}
