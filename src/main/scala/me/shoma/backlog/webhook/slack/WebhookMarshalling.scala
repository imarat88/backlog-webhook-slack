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
      ZonedDateTime.parse(json.convertTo[String], formatter)
    }
    def write(obj: ZonedDateTime): JsValue = ???
  }

  implicit val projectFormat: RootJsonFormat[Project]                       = jsonFormat1(Project)
  implicit val issueContentFormat: RootJsonFormat[IssueContent]             = jsonFormat3(IssueContent)
  implicit val pullRequestContentFormat: RootJsonFormat[PullRequestContent] = jsonFormat3(PullRequestContent)
  implicit val userFormat: RootJsonFormat[User]                             = jsonFormat1(User)

  implicit object WebhookRequestFormat extends RootJsonFormat[WebhookRequest] {
    override def read(json: JsValue): WebhookRequest = {
      val jsObject = json.asJsObject

      val contentType = jsObject.getFields("type") match {
        case Seq(value) => value.convertTo[ContentType]
        case other      => deserializationError("Cannot deserialize ContentType: invalid input. Raw input: " + other)
      }

      val content = jsObject.getFields("content") match {
        case Seq(value) => contentType match {
          case ContentType.AddIssue | ContentType.UpdateIssue | ContentType.CommentIssue | ContentType.DeleteIssue => value.convertTo[IssueContent]
          case ContentType.AddPullRequest | ContentType.UpdatePullRequest | ContentType.CommentPullRequest | ContentType.DeletePullRequest => value.convertTo[PullRequestContent]
          case other => deserializationError("Cannot deserialize Content: invalid input. Raw input: " + other)
        }
        case other => deserializationError("Cannot deserialize Content: invalid input. Raw input: " + other)
      }

      jsObject.getFields("id", "project", "created", "createdUser") match {
        case Seq(JsNumber(id), project, created, createdUser) => WebhookRequest(
          id          = id.toLong,
          contentType = contentType,
          project     = project.convertTo[Project],
          content     = content,
          createdUser = createdUser.convertTo[User],
          created     = created.convertTo[ZonedDateTime]
        )
        case other => deserializationError("Cannot deserialize WebhookRequest: invalid input. Raw input: " + other)
      }
    }

    override def write(obj: WebhookRequest): JsValue = ???
  }

  implicit val slackWebhookFormat: RootJsonFormat[SlackWebhook] = jsonFormat1(SlackWebhook)
}
