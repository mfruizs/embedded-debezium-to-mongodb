# Embedded Debezium to Mongo

Service that, using [Spring Boot](https://spring.io/projects/spring-boot) with **Embedded** [Debezium](https://debezium.io/), instead [Kafka Connect](https://docs.confluent.io/platform/current/connect/index.html), 
collects information about changes made to [Mongo](https://www.mongodb.com/es) collections through its logs, 
thus performing operations 'out of the box'.

To see more details about Mongo configuration on Debezium, follow this [link](https://debezium.io/documentation/reference/2.1/connectors/mongodb.html#mongodb-connector-properties) 

## Pay attention!

### Mongo
> When we create a local mongo it cannot be StandAlone, as Debezium does not accept this, it must have a replica set,
> that's why it is defined as a replica-set even if it is a single node.

#### Connection

* To connect we will need to add name to the local IP so that it locates the mongo

> $ nano /etc/hosts
> 127.0.0.1 mongo

* for the connection to Compass we will use the following **Connection String**.

> mongodb://mongo:30001/?replicaSet=my-replica-set


## Roadmap

* Using more than one pod with dedicated tables per pod.

> We suspect that the use of a single thread executor is on purpose to guarantee the order of changes, but if the 
> partition key is per table here you could use a pool and process changes in 
> parallel, resulting in more speed.
> 
> if the partition key is per table here you could use a pool and process changes in parallel, resulting in more speed.
> One solution could be to use `BlockingQueues`.

**Note:**
> We must take into account the following `tasks.max` property and the information given on the web:
>
> "The maximum number of tasks that should be created for this connector. The MongoDB connector will try to use a separate task for each replica set.
> task for each replica set, so the default value is acceptable when the connector is used with a single MongoDB replica set. When using
> the connector with a sharded MongoDB cluster, we recommend specifying a value that is equal to or greater than the number of shards
> in the cluster, so that the work for each replica set can be distributed by Kafka Connect."