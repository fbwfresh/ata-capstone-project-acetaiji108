#!/bin/bash
set -eo pipefail

source ./setupEnvironment.sh

if [ -z "$GITHUB_GROUP_NAME" ] || [ "$GITHUB_GROUP_NAME" == "replacewithyourgroupname" ] ; then
  echo "Your environment variable GITHUB_GROUP_NAME is not properly configured.  Make sure that you have set it properly in setupEnvironment.sh"
  exit 1
fi

echo "Deleting Application $CAPSTONE_SERVICE_STACK_DEV"
echo "This may take 20-30 minutes...  But if takes more than 1 hour, it may have failed. Check your CloudFormation Stack on the AWS UI for errors."
aws cloudformation delete-stack --stack-name $CAPSTONE_SERVICE_STACK_DEV
aws cloudformation wait stack-delete-complete --stack-name $CAPSTONE_SERVICE_STACK_DEV
