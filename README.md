
![1](https://user-images.githubusercontent.com/37349912/230755976-1dc8e0bd-38ea-473f-8175-74c2acbcc745.png)
![2](https://user-images.githubusercontent.com/37349912/230755979-bb13f47d-677b-4d06-ab5b-00767320fcd3.png)
![3](https://user-images.githubusercontent.com/37349912/230755980-47fedbcb-df0a-4e80-ab5c-ae96c6fb6dac.png)
![4](https://user-images.githubusercontent.com/37349912/230755981-ddd93445-0ba0-4d24-840c-300f24257053.png)
![5](https://user-images.githubusercontent.com/37349912/230755982-9690c525-bbb5-4263-ae8c-4cd33be0a93f.png)
![6](https://user-images.githubusercontent.com/37349912/230755984-dfa92750-058a-4ed4-bb6f-641b84e0d4df.png)
![7](https://user-images.githubusercontent.com/37349912/230755985-f0ef00c5-2ae3-43ca-a44a-4cd34aa08cea.png)
![8](https://user-images.githubusercontent.com/37349912/230755986-c586df42-2f7b-4d8f-8839-0aa527c3ea22.png)
![9](https://user-images.githubusercontent.com/37349912/230755988-5b78a47e-d240-4165-882d-371c954a79a3.png)
![10](https://user-images.githubusercontent.com/37349912/230755989-6ee03add-e331-47cf-b94a-873c7bf7ebc3.png)

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


