# log-collection

It exposes a `/lines` GET endpoint that serves lines from log files.

## Start the server
`java -jar target/uberjar/log-collection-1.0-standalone.jar`

## `/lines` endpoint

Returns a collection of lines from a log file.
It requires the following GET parameters:
- `filename`: The log file name. A file with that name must exist under the `/var/log` folder in the server.
- `lines`: The number of lines to be fetched. It must be a positive integer.

It optionally accepts the following parameters:
- `keyword`: If a keyword is given, only lines that contain the given keyword will be returned.

The response is given in json format as an array of lines under the `lines` key in the body
```json
{"lines": [<line-1> <line-2> ... <line-n>]
```

It returns the lines of the log on a reverse order: it looks for lines matching the given
keyword starting at the beginning of the file, and it traverses the file in reverse order 
until it has looked for N lines, or it has reached the beginning of the log file.

## Examples
The following requests were tested against a server that had a `big-log` file under /var/log with a size of 1.7GB.

### Successful requests

## Request without a keyword
```shell
curl "localhost:3000/lines?filename=big-log&lines=4"
{"lines":["82174603 - a log line","82174602 - a log line","82174601 - a log line","82174600 - a log line"]}
```
It took ~0.009 seconds
```shell
curl -o /dev/null -s -w 'Total: %{time_total}s\n' "localhost:3000/lines?filename=big-log&lines=4"
Total: 0.009853s
```

## Request with a keyword
```shell
curl "localhost:3000/lines?filename=big-log&lines=4&keyword=3"
{"lines":["82174603 - a log line","82174593 - a log line","82174583 - a log line","82174573 - a log line"]}
```

## Requesting a huge amount of lines with no keyword given
Took ~0.268 seconds requesting 100,000 lines 
```shell
curl -o /dev/null -s -w 'Total: %{time_total}s\n' "localhost:3000/lines?filename=big-log&lines=100000"
Total: 0.268709s
```

## Requesting a huge amount of lines with a keyword
Took 0.5 seconds requesting 100,000 lines
```shell
curl -o /dev/null -s -w 'Total: %{time_total}s\n' "localhost:3000/lines?filename=big-log&lines=100000&keyword=6"
Total: 0.520430s
```