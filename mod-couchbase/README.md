# Couchbase Persistor

This module allows data to be saved, retrieved, searched for, and deleted in a Couchbase instance. This project use the  event bus of vertx for managing the datas.  

####To use this module you must have a Couchbase DB instance running on your network.

This is a multi-threaded worker module.

## Dependencies

This module requires a MongoDB Couchbase DB to be available on the network.

## Name

The module name is `org.skarb.vertx~mod-couchbase`.

## Configuration
For using this module, you must set a specific configuration:

    {
        "address": <address>,
        "host": <host>,
        "bucket": <bucket>,
        "password": <password>,
        "query-limit": <query-limit>,
        "store-timeout": <store-timeout>
    }
    
For example:

    {
        "address": "test.my_persistor",
        "host": "http://192.168.1.100:8091/pools",
        "bucket": "myBucket",
        "password": "passwordOfTheBucket",
        "query-limit": 200,
        "store-timeout": 0
    }
    
Let's take a look at each field in turn:

* `address` The main address for the module. Every module has a main address. Defaults to `vertx.couchbasepersistor`.
* `host` The url of the couchbase server. Defaults to `http://127.0.0.1:8091/pools`.
* `bucket` Name of the bucket in the couchbase instance to use. Defaults to `default`.
* `password` The password of the bucket. Defaults none.
* `query-limit` The number of the results to retreive by a view query. Default is 200.
* `store-timeout` The time of the datas must be store in couchbase db. Default is 0 (no deletion of the datas).

## Operations

The module supports the following operations

