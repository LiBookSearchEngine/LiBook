<h1 align="center">LiBook: Book Search Engine 🔍</h1>

In this repository, you can find the source code for building up an inverted index based search engine for books obtained from both Project Gutenberg and registered users' accounts directly. We also implemented both relational and non-relational datamarts to be able to make queries on the available books. This is a micro-service-oriented application that consists of the next modules:
- <b>Crawler</b>: Obtains books directly from [Project Gutemberg](https://www.gutenberg.org/) book platform and stores them into our datalake.
- <b>Cleaner</b>: Processes the books and prepares them to be indexed.
- <b>Indexer</b>: Indexes the books into our inverted index structure in Hazelcast.
- <b>MetadataDatamartBuilder</b>: Creates a metadata datamart for queries.
- <b>QueryEngine</b>: Offers an API for users to be able to query our inverted index.
- <b>UserService</b>: Handles users' accounts in MongoDB, and session tokens through a distributed Hazelcast datamart.
- <b>UserBookProcessor</b>: Processes the books uploaded by users and sends them to the cleaner.
- <b>ApiGateway</b>: Serves an API merging all the public APIs of the final application, improving security on petitions.

Crucially, this project employs three distinct datamart technologies—Hazelcast, MongoDB, and Rqlite. Rqlite, based on SQLite and adapted for clustered usage, is particularly notable for its role in distributed relational database management within the application. The integration of these datamarts enhances the overall scalability, efficiency, and versatility of the search engine, accommodating both centralized and distributed data processing needs.

<br>

<img src="https://github.com/ricardocardn/LiBook/blob/master/resources/arq_final.png" alt="Image for Dark Mode">

<br>

<p align="center"><img src="https://skills.thijs.gg/icons?i=docker,java,nginx,mongodb,sqlite,git&theme=dark"></p>

<br>
<h2>1) <b>How to run</b> (Docker and Docker Compose)</h2>

For each module, you should generate the corresponding docker image. In our case, we will deploy in a Google Cloud virtual machine instance the Crawler, Cleaner, API Gateway, User Service and User Books Processor services. To do so, after connecting to the Google Cloud server, we execute the ```docker-compose up``` command, for the docker compose file given in this repository. Now, we have to run the micro-services that are left to the in-premise processing.

<h3><b>Indexer</b></h3>
To execute the indexer, we should run the docker image as follows:

```
docker run --network host ricardocardn/indexer
```

Make sure you should specify the option ```--network host```, or some problems related to Hazelcast may raise.

<h3><b>Metadata Datamart Builder</b></h3>

To run this service, we will have to run both rqlite and metadata datamart builder images. Thus, execute the rqlite image in one single computer of your cluster:

```
docker run -p4001:4001 -p4002:4002 rqlite/rqlite
```

And, for the Metadata Datamart builder, execute:

```
docker run -e "SERVER_MQ_PORT={port}"
            -e "SERVER_API_URL=http://{server's IP}"
            -e "SERVER_CLEANER_PORT={cleaner's port on server}"
            -e "LOCAL_MDB_API=http://{rqlite's API address}"
        ricardocardn/metadata-datamart-builder
```

<h3><b>Query Engine</b></h3>
```
docker run -p 8080:8080 --network host susanasrez/queryengine
```

<h3><b>User Service</b></h3>

The user service will be connected to both Hazelcast and MongoDB datamarts, so make sure to use the ```--network=host``` and you have a MongoDB Atlas account. So:
```
docker run -p 8082:8080
            -e ""MONGO_ATLAS_PASSWORD={your password}"
            -e "SERVER_API_URL=http://{server's IP}"
          ricardocardn/user-service
```

<h3><b>Local API Gateway</b></h3>

Local API Gateway will be used so that computers in the cluster will be able to deploy several services. Thus, when routing with the load balancer, each user petition can be attended by any computer.

```
docker run -p 8080:8080
            -e "USER_SERVICE_API=http://localhost:{local user service's port}"
            -e "QUERY_ENGINE_SERVICE_API=http://localhost:{query engine service's port}"
            -e "CLEANER_SERVICE_API=http://{server's ip}:{cleaner's port}"
          ricardocardn/local-api-gateway
```



<br>
<h2>Credits</h2>


- [Adam Brez](https://github.com/breznada/)
- [Susana Suárez](https://github.com/susanasrez)
- [Ricardo Cárdnes](https://github.com/ricardocardn)
- [Mara Pareja](https://github.com/marapareja17)
- [Joaquín](https://github.com/JoaquinIP)
