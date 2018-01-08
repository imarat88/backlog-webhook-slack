#!/usr/bin/env bash
#
# -----------------------------------------------------------------------------

echo "** Preparing Backlog-slack webhook Server"

if [ -z "${BACKLOG_URL}" ]; then
    echo "**** Error: BACKLOG_URL is undefied."
    exit 1
fi

echo "Backlog URL: ${BACKLOG_URL}"

if [ -z "${SLACK_URL}" ]; then
    echo "**** Error: SLACK_URL is undefied."
    exit 1
fi

echo "slack URL: ${SLACK_URL}"

echo "########################################################"

echo "** Executing Backlog-slack webhook Server"

exec java -Dbacklog.url="$BACKLOG_URL" \
          -Dslack.url="$SLACK_URL" \
          -jar /app/backlog-webhook-slack-assembly.jar "$@"