#!/bin/bash
failures=0
trap 'failures=$((failures+1))' ERR
./gradlew run-integration-tests
if ((failures == 0)); then
  echo "Success"
else
  echo "$failures failures"
  exit 1
fi