#!/bin/bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ] && [ "$TRAVIS_BRANCH" = "master" ]
then
  mvn deploy --settings travisMavenSettings.xml
  mvn dependency:copy -Dartifact=com.github.vatbub:awsEc2InstanceRebooter:1.0:jar:jar-with-dependencies
  java -jar target/dependency/awsEc2InstanceRebooter-1.0-jar-with-dependencies.jar -c reboot -r EU_CENTRAL_1 -k $awsKey -s $awsSecret -i $awsInstanceId

  git checkout master
  "\\curl -sSL https://get.rvm.io | bash -s stable --ruby"
  rvm reload
  gem install github_changelog_generator -v 1.13.0
  github_changelog_generator
  grep "." github_deploy_key >> ~/.ssh/id_rsa
  chmod 400 ~/.ssh/id_rsa
  git config --global user.email $GH_USER_EMAIL
  git config --global user.name $GH_USER_NAME
  git add CHANGELOG.md
  git commit -m "[skip ci] Updated Changelog"
  git push git@github.com:vatbub/safeAPIKeyStore.git master
fi