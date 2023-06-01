# YAMockServer

Yet another MockServer

Provides a fake API based on json configuration files.

## Features
- TODO 

## Roadmap

- auto reload of configuration changes https://docs.oracle.com/javase/tutorial/essential/io/examples/WatchDir.java
- Make config path based on a canonical path
- configuration validation
- add different request,response types xml ,json, text/html
- unit tests
- Readme
- extend functionality to update/create entries with a post call
- dockerize

# Starting the server

## Kotlin

With default values

- src/main/resources/config.json will be used as path for the configuration
- port 9090

```
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        MockServer().start()
    }
}
```

Changed port and path for the configuration

```
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        MockServer(port = 8080 , configFilePath="your/specific/path/to/config.json).start()
    }
}
```

## Jar/executable

TODO

## Docker

```
docker build -t yamockserver .

docker run -e CONFIG_PATH=/app/custom-config.json yamockserver````
```

# Configuration

If no configuration is provied for any kind of requst the http status code 200 is returned.

In general the method, path and body of the request is logged to the console for every request.

config.json

```
{
  "paths": [
    {
      "path": "/v1/api",
      "requestMethod": "GET",
      "requestBody": "api_request.json",
      "responseStatus": 500,
      "responseBody": "{\"response\" : \"error message\"}"
    },
    {
      "path": "/v3/api",
      "requestMethod": "GET",
      "requestBody": "api_request.json",
      "responseStatus": 200,
      "responseBodyFileReference": "response/api_response_object.json"
    },
    {
      "path": "/v2/api",
      "requestMethod": "GET",
      "requestBody": "api_request.json",
      "responseStatus": 200
    }
  ]
}
```

api_response_object.json

```
{
  "api": "content"
}
```

### License 

Apache License Version 2.0

