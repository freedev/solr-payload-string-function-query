#!/bin/bash

curl "localhost:8983/solr/admin/collections?action=CREATE&name=payloadtest&numShards=1&replicationFactor=1&maxShardsPerNode=1&collection.configName=payloadtest"
curl -X POST "localhost:8983/solr/payloadtest/update" --data-binary @data.json -H 'Content-type:application/json'
curl "http://localhost:8983/solr/payloadtest/update?commit=true"
