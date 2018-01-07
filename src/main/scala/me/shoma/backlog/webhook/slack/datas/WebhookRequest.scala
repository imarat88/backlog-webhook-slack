package me.shoma.backlog.webhook.slack.datas

import java.time.ZonedDateTime

case class WebhookRequest(
  id: Long,
  contentType: ContentType,
  project: Project,
  content: Content,
  createdUser: User,
  created: ZonedDateTime
)
