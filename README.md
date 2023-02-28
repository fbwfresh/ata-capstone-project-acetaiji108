# ATA-Capstone-Project

Follow the instructions in the course for completing the group Capstone project.

### Fill out the environment variables
Complete `setupEnvironment.sh` with the group repo name and the github username of the team member holding the repo.
Confirm these are in lower case.
The repo owner should confirm that all team members have been added to collaborate on the repo.

### To create the Lambda Example table in DynamoDB:

You must do this for the ServiceLambda to work!

```
aws cloudformation create-stack --stack-name lambda-table --template-body file://LambdaExampleTable.yml --capabilities CAPABILITY_IAM
```

### To deploy the Development Environment

Run `./deployDev.sh`

As you are taking a break from work, use the END LAB button in Vocareum instead of removing the pipeline each time.
The End Lab button will pause the lab and resources, not allowing the budget to be used. When you're ready to start again,
click the Start Lab button to begin again with renewed AWS credentials.

To tear down the deployment then run `./cleanupDev.sh`

### To deploy the CI/CD Pipeline

Fill out `setupEnvironment.sh` with the url of the github repo and the username (in all lowercase) of the 
team member who is maintaining the repo. Confirm that the team member has added your username as a contributor to the repo.

Run `./createPipeline.sh`

As you are taking a break from work, use the END LAB button in Vocareum instead of removing the pipeline each time.
The End Lab button will pause the lab and resources, not allowing the budget to be used. When you're ready to start again,
click the Start Lab button to begin again with renewed AWS credentials.

To teardown the pipeline, run `./cleanupPipeline.sh`


