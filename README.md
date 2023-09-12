# AWS pub-sub sample

This is a sample project to illustrate what I (Arthur) did on [this blog post]() about spring boot integration with 
AWS services to create more resilient applications using the pub-sub strategy.

### Technologies

- Spring boot v3
- AWS Spring v3
- Localstack
- Kotlin for the SQS sub
- Java for the SNS pub

### Starting the services

First, start the SNS side pub compose, this will create the base environment for the other service. After that, run the 
following scripts on your console to configure the SQS side:

1. Create a DLQ for the queue we want to use as subscriber to the topic:
```shell
aws sqs create-queue \
      --endpoint-url=http://localhost:4566 \
      --queue-name="order-processing_queue_dlq"
```
2. Create the queue we want to use as subscriber to the topic:
```shell
aws sqs create-queue \
      --endpoint-url=http://localhost:4566 \
      --queue-name="order-processing_queue" \
      --attributes DelaySeconds=5,RedrivePolicy="\"{\\\"deadLetterTargetArn\\\":\\\"arn:aws:sqs:us-west-2:000000000000:order-processing_queue_dlq\\\",\\\"maxReceiveCount\\\":\\\"3\\\"}\""
```
3. Subscribe the queue to the SNS topic created when you started the compose file from the sns-pub project:
```shell
aws sns subscribe \
      --endpoint-url=http://localhost:4566 \
      --topic-arn=arn:aws:sns:us-west-2:000000000000:order-created_notification \
      --protocol=sqs \
      --notification-endpoint=arn:aws:sqs:us-west-2:000000000000:order-processing_queue
```
4. The last command will output an subscription ARN, use it to set the raw delivery of messages to this subscription:
```shell
aws sns set-subscription-attributes \
  --endpoint-url=http://localhost:4566 \
  --subscription-arn="{put the subscription ARN here}" \
  --attribute-name=RawMessageDelivery \
  --attribute-value=true
```
The steps enumerated above will configure what you need to make the services work together.

After everything is set, just make a simple POST call to the order endpoint with the following payload:

- POST http://localhost:8085/api/v1/orders
```json
{
    "number": "a7cdae0a-7224-432c-9a77-c8c0da922a50",
    "requester": "Someone",
    "items": [
        {
            "description": "Some Food",
            "quantity": 2,
            "value": 10.2
        },
        {
            "description": "Some Part",
            "quantity": 3,
            "value": 9.99
        },
        {
            "description": "Some Thing",
            "quantity": 3,
            "value": 3.50
        }
    ]
}
```
That's it, do it and, you should be able to see the event going to the SNS topic, to the subscriber queue and them, being 
consumed by the _sqs-sub_ service.
