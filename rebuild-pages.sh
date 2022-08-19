#/bin/bash
curl  -X POST  -H "Accept: application/vnd.github+json"  -H "Authorization: token $TOKEN"  https://api.github.com/repos/bfreuden/vertx-mongodb-gen/pages/builds

