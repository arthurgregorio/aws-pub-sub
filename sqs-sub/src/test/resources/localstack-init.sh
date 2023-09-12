#!/bin/bash

: '
To test local br-finance-integration and br-netsuite-adapter together, after running compose from br-netsuite-adapter,
run those commands locally to setup the localstack environment:

> create SQS queue DLQ
aws sqs create-queue \
      --endpoint-url=http://localhost:4566 \
      --queue-name="order-processing_queue_dlq"

> create SQS queue and point the redrive police to the DLQ
aws sqs create-queue \
      --endpoint-url=http://localhost:4566 \
      --queue-name="order-processing_queue" \
      --attributes DelaySeconds=5,RedrivePolicy="\"{\\\"deadLetterTargetArn\\\":\\\"arn:aws:sqs:us-west-2:000000000000:order-processing_queue_dlq\\\",\\\"maxReceiveCount\\\":\\\"3\\\"}\""

> subscribe SNS topic to the SQS queue
aws sns subscribe \
      --endpoint-url=http://localhost:4566 \
      --topic-arn=arn:aws:sns:us-west-2:000000000000:order-created_notification \
      --protocol=sqs \
      --notification-endpoint=arn:aws:sqs:us-west-2:000000000000:order-processing_queue

> get subscription ARN value and put into this command, run it to set RAW message coming from the topic
aws sns set-subscription-attributes \
  --endpoint-url=http://localhost:4566 \
  --subscription-arn="arn:aws:sns:us-west-2:000000000000:order-created_notification:b8a0f371-cd5a-4f1c-b00b-4db84bac2a07" \
  --attribute-name=RawMessageDelivery \
  --attribute-value=true
'

LOCALSTACK_HOST=localhost

echo "Creating SQS queues..."

SQS_QUEUES=("order-processing_queue")

for queue in "${SQS_QUEUES[@]}"
do
  aws sqs create-queue \
      --endpoint-url=http://$LOCALSTACK_HOST:4566 \
      --queue-name="${queue}_dlq"
  echo "DLQ queue [${queue}_dlq] created"

  aws sqs create-queue \
      --endpoint-url=http://$LOCALSTACK_HOST:4566 \
      --queue-name=$queue \
      --attributes DelaySeconds=5,RedrivePolicy="\"{\\\"deadLetterTargetArn\\\":\\\"arn:aws:sqs:us-west-2:000000000000:${queue}_dlq\\\",\\\"maxReceiveCount\\\":\\\"3\\\"}\""
  echo "Queue [$queue] created"
done
