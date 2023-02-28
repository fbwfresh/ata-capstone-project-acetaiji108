branch=main

source ./setupEnvironment.sh

if [ -z "$CAPSTONE_REPO_NAME" ] ; then
  echo "Your environment variables are not properly configured.  Make sure that you have filled out setupEnvironment.sh and that script is set to run as part of your PATH"
  exit 1
fi

if [ -z "$GITHUB_TOKEN" ] ; then
  echo "Your environment variable GITHUB_TOKEN is not properly configured.  Make sure that you have added it to your .bash_profile"
  exit 1
fi

if [ -z "$GITHUB_GROUP_NAME" ] || [ "$GITHUB_GROUP_NAME" == "replacewithyourgroupname" ] ; then
  echo "Your environment variable GITHUB_GROUP_NAME is not properly configured.  Make sure that you have set it properly in setupEnvironment.sh"
  exit 1
fi


echo "Outputting parameters for the pipeline..."
echo "Project name: $CAPSTONE_PROJECT_NAME"
echo "Github Group Name: $GITHUB_GROUP_NAME"
echo "Repo path: $CAPSTONE_REPO_NAME"
echo "Branch: $branch"

aws cloudformation create-stack --stack-name $CAPSTONE_PROJECT_NAME-$GITHUB_GROUP_NAME --template-url https://ata-deployment-scripts.s3.us-east-1.amazonaws.com/CICDPipeline-Capstone.yml --parameters ParameterKey=ProjectName,ParameterValue=$CAPSTONE_PROJECT_NAME ParameterKey=GithubUserName,ParameterValue=$GITHUB_USERNAME ParameterKey=GithubGroupName,ParameterValue=$GITHUB_GROUP_NAME ParameterKey=Repo,ParameterValue=$CAPSTONE_REPO_NAME ParameterKey=Branch,ParameterValue=$branch ParameterKey=GithubToken,ParameterValue=$GITHUB_TOKEN --capabilities CAPABILITY_IAM CAPABILITY_AUTO_EXPAND
