package me.shoma.backlog.webhook.slack.datas

object ContentType {

  case object AddIssue extends ContentType(1)
  case object UpdateIssue extends ContentType(2)
  case object CommentIssue extends ContentType(3)
  case object DeleteIssue extends ContentType(4)

  case object AddPullRequest extends ContentType(18)
  case object UpdatePullRequest extends ContentType(19)
  case object CommentPullRequest extends ContentType(20)
  case object DeletePullRequest extends ContentType(21)

  def apply(id: Int): ContentType = id match {
    case 1 => AddIssue
    case 2 => UpdateIssue
    case 3 => CommentIssue
    case 4 => DeleteIssue
    case 18 => AddPullRequest
    case 19 => UpdatePullRequest
    case 20 => CommentPullRequest
    case 21 => DeletePullRequest
    case _ => throw new IllegalArgumentException("Unknown id: " + id)
  }
}


sealed abstract class ContentType(id: Int)