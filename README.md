[![CircleCI](https://circleci.com/gh/filipelenfers/authJwt.svg?style=svg)](https://circleci.com/gh/filipelenfers/authJwt)

# AuthJWT Service

## Deploy

1. Compile and Assembly
```bash
sbt compile
sbt assembly
```
2. Run terraform to create the dynamodb table needed:
```bash
cd terraform
terraform apply
cd ..
```
3. Deploy serverless function and service. 
```bash
sls deploy
```
A resume with the new api-key and service url will be outputed.
4. Test it:
```bash
curl -H "x-api-key: GET_APIKEY_GENERATE_ON_SLS_DEPLOY_AND_PASTE_HERE"  -X POST  https://YOU_API_URL.execute-api.us-east-1.amazonaws.com/dev/login -d  "{\"email\":\"testUser@fakeEmail.com\",\"password\":\"mypassword\"}"

#COPY THE TOKEN GENERATED ABOVE

curl -H "Authorization: PASTE_TOKEN_HERE" https://YOU_API_URL.execute-api.us-east-1.amazonaws.com/dev/helloWorld
```

## TODO

* Make brief intro and architecture diagram
* sls invoke local to test... see notes.txt
* smm for all parameters
* terraform control usage plans
* unit testing
* functional testing