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

I started to use payloads because I had the classical per-store pricing problem.
Thousands of stores across the world and different prices for each store.
I found payloads very useful for many reasons, like enabling/disabling the product for such store, save the stock availability, or save the other infos like buy/sell price, discount rates, and so on. 
But All those information are numbers, but stores can also be in different countries, I mean for example would be useful also have the currency and other attributes related to the store.

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