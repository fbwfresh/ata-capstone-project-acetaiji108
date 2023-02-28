#!/bin/bash
set -eo pipefail

echo "Deleting Application CAPSTONE_APPLICATION_STACK"
aws cloudformation delete-stack --stack-name $CAPSTONE_APPLICATION_STACK
echo "Deleting Application CAPSTONE_SERVICE_STACK"
aws cloudformation delete-stack --stack-name $CAPSTONE_SERVICE_STACK

echo "Waiting for deletion of Application CAPSTONE_APPLICATION_STACK"
echo "This may take 2-3 minutes...  But if takes more than 5 minutes then it may have failed. Check your CloudFormation Stack on the AWS UI for errors."
aws cloudformation wait stack-delete-complete --stack-name $CAPSTONE_APPLICATION_STACK

echo "Waiting for deletion of Service CAPSTONE_SERVICE_STACK"
echo "This may take 2-3 minutes...  But if takes more than 5 minutes then it may have failed. Check your CloudFormation Stack on the AWS UI for errors."
aws cloudformation wait stack-delete-complete --stack-name $CAPSTONE_SERVICE_STACK

echo "Checking Artifact Bucket $CAPSTONE_ARTIFACT_BUCKET"
if [ -z "$(aws s3api head-bucket --bucket "$CAPSTONE_ARTIFACT_BUCKET" 2>&1)" ] ; then
  echo "Deleting Artifact Bucket $CAPSTONE_ARTIFACT_BUCKET"
  aws s3 rm s3://$CAPSTONE_ARTIFACT_BUCKET --recursive
  aws s3api delete-objects --bucket $CAPSTONE_ARTIFACT_BUCKET --delete "$(aws s3api list-object-versions --bucket $CAPSTONE_ARTIFACT_BUCKET --query='{Objects: Versions[].{Key:Key,VersionId:VersionId}}')" 1>/dev/null
  aws s3api delete-objects --bucket $CAPSTONE_ARTIFACT_BUCKET --delete "$(aws s3api list-object-versions --bucket $CAPSTONE_ARTIFACT_BUCKET --query='{Objects: DeleteMarkers[].{Key:Key,VersionId:VersionId}}')" 1>/dev/null
  aws s3 rb --force s3://$CAPSTONE_ARTIFACT_BUCKET
fi

echo "Deleting Pipeline $CAPSTONE_PIPELINE_STACK"
aws cloudformation delete-stack --stack-name $CAPSTONE_PIPELINE_STACK

echo "Waiting for deletion of pipeline $CAPSTONE_PIPELINE_STACK"
echo "This may take 2-3 minutes...  But if takes more than 5 minutes then it may have failed. Check your CloudFormation Stack on the AWS UI for errors."
aws cloudformation wait stack-delete-complete --stack-name $CAPSTONE_PIPELINE_STACK

rm -rf build .gradle target
