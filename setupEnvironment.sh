# Step One- Fill out the two requested variables.

# Fill out the following values
# The path of your repo on github. Don't include the whole URL, just the part after github.com/KenzieAcademy-SoftwareEngineering/
export CAPSTONE_REPO_NAME=ata-capstone-project-bestjavaprogrammer

# Fill out the following value with the group name in all lowercase and no spaces or special characters.
export GITHUB_GROUP_NAME=REPLACE_WITH_YOUR_GROUP_NAME

# Step Two - configure your shell to always have these variables.
# For OSX / Linux
# Copy and paste ALL of the properties below into your .bash_profile in your home directly

# For Windows
# Copy and paste ALL of the properties below into your .bashrc file in your home directory

# In IntelliJ Terminal
# Type source ./setupEnvironment.sh

# Confirm set up by using echo $CAPSTONE_REPO_NAME and it should return what you typed in.



# Do not modify the rest of these unless you have been instructed to do so.
export CAPSTONE_PROJECT_NAME=capstone
export CAPSTONE_PIPELINE_STACK=$CAPSTONE_PROJECT_NAME-$GITHUB_GROUP_NAME
export CAPSTONE_ARTIFACT_BUCKET=$CAPSTONE_PROJECT_NAME-$GITHUB_GROUP_NAME-artifacts
export CAPSTONE_APPLICATION_STACK=$CAPSTONE_PROJECT_NAME-$GITHUB_GROUP_NAME-application
export CAPSTONE_SERVICE_STACK=$CAPSTONE_PROJECT_NAME-$GITHUB_GROUP_NAME-service
export CAPSTONE_SERVICE_STACK_DEV=$CAPSTONE_PROJECT_NAME-$GITHUB_GROUP_NAME-service-dev
