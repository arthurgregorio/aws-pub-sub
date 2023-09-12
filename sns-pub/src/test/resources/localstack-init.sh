#!/bin/bash

LOCALSTACK_HOST=localhost

echo "Creating SNS topics..."

SNS_TOPICS=("order-created_notification")

for topic in "${SNS_TOPICS[@]}"
do
  aws sns create-topic \
      --endpoint-url=http://$LOCALSTACK_HOST:4566 \
      --name=$topic
  echo "Topic [$topic] created"
done

echo "Configuration completed, ready to dev!"
