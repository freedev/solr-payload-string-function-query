# Solr Payload String Custom Function Query

This custom function query helps to read and sort payload field `delimited_payloads_string`.

Example document:

```
{
    "id":"my sample doc",
    "currencyPayload":[
      "store1|EUR",
      "store1|USD",
      "store3|GBP"
    ]
}
```

Querying with `fl=` for `spayload(currencyPayload,store3)` would generate a response like the following:

```
{
  "response":{
    "docs":[
      {
        "id":"my sample doc",
        "spayload(currencyPayload,store3)":"GBP"
      }
    ]
  }
}     
```

And executing `spayload(payloadCurrency,store2)` returns `EUR`, and so on.

You can use `spayload` even as sorting function.

   sort=spayload(payloadField,value) asc

## Why?
This project was originally conceived as a solution for storing bounding boxes with terms for OCR highlighting.

See it in action at http://github.com/o19s/pdf-discovery-demo.

## Requirements
- Solr 7.x
- A field type that utilizes payloads string

## Building
Building requires JDK 8 and Maven.  Once you're setup just run:

`mvn package` to generate the latest jar in the target folder.

## Todo
- Add test unit

## Usage - Configuration steps

- copy the `solr-payload-string-function-query.jar` into your solr_home/dist directory 
- add these lines in your `solrconfig.xml`:

```
<lib dir="${solr.install.dir:../../../..}/dist/" regex="solr-payload-string-function-query-\d.*\.jar" />
<lib dir="${solr.solr.home}/lib/" regex="solr-payload-string-function-query-\d.*\.jar" />
<valueSourceParser name="spayload" class="it.damore.solr.payload.PayloadStringValueSourceParser" />
```