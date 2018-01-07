#!/usr/bin/env bash
#
# -----------------------------------------------------------------------------

echo "** Preparing Backlog-slack webhook Server"

echo "########################################################"

echo "** Executing Backlog-slack webhook Server"

exec java -jar /app/backlog-webhook-slack-assembly.jar "$@"