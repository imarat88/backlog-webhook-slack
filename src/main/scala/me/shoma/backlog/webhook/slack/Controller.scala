package me.shoma.backlog.webhook.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import me.shoma.backlog.webhook.slack.datas._
import me.shoma.backlog.webhook.slack.datas.{ ContentType => CT }

import scala.concurrent.{ExecutionContext, Future}

trait Controller extends WebhookMarshalling {

  val reqHeaders: Seq[HttpHeader] = Seq(
    headers.`Content-Type`(ContentTypes.`application/json`),
    headers.`Accept-Charset`(HttpCharsets.`UTF-8`)
  )

  def webhookToSlack(backlog: WebhookRequest)
                    (implicit actorSystem: ActorSystem, mat: Materializer, exc: ExecutionContext): Future[HttpResponse] = {

    import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

    val config  = ConfigFactory.load()
    val slackUrl = config.getString("slack.url")
    val backlogUrl = config.getString("backlog.url")
    val user = backlog.createdUser.name
    val text = backlog.content match {
      case IssueContent(key_id, summary, _) =>
        val url = s"$backlogUrl/view/${backlog.project.projectKey}-$key_id"
        backlog.contentType match {
          case CT.AddIssue     => s"${user}が課題を追加しました。\n$summary\n$url"
          case CT.UpdateIssue  => s"${user}が課題を更新しました。\n$summary\n$url"
          case CT.CommentIssue => s"${user}が課題にコメントしました。\n$summary\n$url"
          case CT.DeleteIssue  => s"${user}が課題を削除しました。\n$summary\n$url"
          case _ => ""
        }
      case PullRequestContent(_, _, _) => backlog.contentType match {
        case CT.AddPullRequest     => s"${user}がプルリクエストを作成しました。"
        case CT.UpdatePullRequest  => s"${user}がプルリクエストを更新しました。"
        case CT.CommentPullRequest => s"${user}がプルリクエストにコメントしました。"
        case CT.DeletePullRequest  => s"${user}がプルリクエストを削除しました。"
        case _ => ""
      }
      case _ => ""
    }

    val data = SlackWebhook(text)

    Marshal(data).to[RequestEntity] flatMap { entity =>
      val request = HttpRequest(method = HttpMethods.POST, uri = Uri(slackUrl), headers = reqHeaders.toList, entity = entity)
      Http().singleRequest(request)
    }
  }

}
