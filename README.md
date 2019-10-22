# Solr Payload String Custom Function Query

This function query helps to read and sort payload field `delimited_payloads_string`.

Given the former example where I have a multivalue field payloadCurrency:

    payloadCurrency: [
        "store1|USD",
        "store2|EUR",
        "store3|GBP"
    ]

executing `spayload(payloadCurrency,store2)` returns `EUR`, and so on for the remaining key/value in the field.

You can use `spayload` even as sorting function.

   sort=spayload(payload,store2)%20asc

## Configuration steps

1. Add these lines in `solrconfig.xml`:

```
<lib dir="${solr.install.dir:../../../..}/dist/" regex="solr-payload-string-function-query-\d.*\.jar" />
<lib dir="${solr.solr.home}/lib/" regex="solr-payload-string-function-query-\d.*\.jar" />
```

2. And add this line in `solrconfig.xml`:

```
   <valueSourceParser name="spayload" class="it.damore.solr.payload.PayloadStringValueSourceParser" />
```
