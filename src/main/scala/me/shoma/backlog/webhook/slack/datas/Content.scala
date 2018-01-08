package me.shoma.backlog.webhook.slack.datas

sealed trait Content

case class IssueContent(
  key_id: Long,
  summary: String,
  description: String
) extends Content

case class PullRequestContent(
  id: Long,
  summary: String,
  description: String
) extends Content
