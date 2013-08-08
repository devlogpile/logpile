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

The module supports the following operations :

### Store Commands

#### Add

create a new document in the database.

To create a new document send a JSON message to the module main address:

    {
        "action": "add",
        "key": "1234",
        "timeout": 0,
        "document": <document>
    }     
    
Where:
* `key` is the key of the document. Optional parameter. If the key is not set then an key is generated. 
* `timeout` is the time of the data is store in couchbase. if not set, then the default value of the persistor is used.
* `document` is the JSON document that you wish to save.

The response contains the following informations :

    {
        "key": "1234",
        "count": 1,
        "message": <message> 
    }     

Where:
* `key` The key value which is stored. Optional parameter.
* `count` the number of the data stored. if the value is equal to 1, then the document is stored in the couchbase server. Otherwise an error occured and the data was not saved to couchbase.
* `message` The error message.

#### Replace

Replace a  document in the database.

To replace a new document send a JSON message to the module main address:

    {
        "action": "replace",
        "key": <key>,
        "timeout": <timeout>,
        "document": <document>
    }     

The datas in input and the response are identical to call the `add` method.

#### Set

set a  document in the database. if the document does not exist, then the document is created. if the document already exists, then the document is replaced

    {
        "action": "set",
        "key": <key>,
        "timeout": <timeout>,
        "document": <document>
    }     

The datas in input and the response are identical to call the `add` method.

### Find Commands

#### Get

get a specific document in the database.

To get a new document send a JSON message to the module main address:

    {
        "action": "get",
        "key": "1234"
    }  

Where:
* `key` is the key of the document to retrieve. Mandatory parameter. 

 The response to this message contains the following informations :

    {
        "count": 1,
        "document": <document>
    }  

Where:
* `count` The number of document which match to this key. if the document was found, the value is 1. Otherwise, it's equal to 0.
* `document` The document.

