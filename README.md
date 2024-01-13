<h1 align="center">LiBook: Book Search Engine 🔍</h1>

In this repository, you can find the source code for building up an inverted index based search engine for books obtained from both Project Gutenberg and registered users' accounts directly. We also implemented both relational and non-relational datamarts to be able to make queries on the available books. This is a micro-service-oriented application that consists of the next modules:
- <b>Crawler</b>: Obtains books directly from [Project Gutemberg](https://www.gutenberg.org/) book platform and stores them into our datalake.
- <b>Cleaner</b>: Processes the books and prepares them to be indexed.
- <b>Indexer</b>: Indexes the books into our inverted index structure in Hazelcast.
- <b>QueryEngine</b>: Offers an API for users to be able to query our inverted index.
- <b>UserService</b>: Handles users' accounts in MongoDB, and session tokens through a distributed Hazelcast datamart.
- <b>UserBookProcessor</b>: Processes the books uploaded by users and sends them to the cleaner.
- <b>ApiGateway</b>: Serves an API merging all the public APIs of the final application, improving security on petitions.

The underlying communication between modules is given by internal APIs and the use of ActiveMQ, which will be removed in the future and substituted by Hazelcast or Kafka due to its vulnerabilities. However, ActiveMQ doesn't access user's data but stands for book notifications.

<br>

<img src="https://github.com/ricardocardn/LiBook/blob/master/resources/arq_3.png" alt="Image for Dark Mode">

<br>

For the future, we will also have a datamart to directly query our books based on metadata, instead of the content. To do so, we should build MetadataDatamartBuilder, a micro-service that will be dedicated to the translation of incoming books' metadata, which could be obtained through Cleaner's API, into the datamart. It will also have a connection to ActiveMQ, or Kafka, to be able to now when the Cleaner application has inserted new metadata into the datalake, thanks to the notification system already in use.

<br>

<img src="https://github.com/ricardocardn/LiBook/blob/master/resources/arq_future.png" alt="Image for Dark Mode">

<br>

<br>
<h2>1) <b>How to run</b> (Docker and Docker Compose)</h2>

For each module, you should generate the corresponding docker image. If we take the indexer as a reference, a command like the following should be executed

```
docker build -t ricardocardn/indexer path_to_repo/Indexer/.
```

Or whether pull our own image directly

```
docker run -p 8081:8081 --network host ricardocardn/indexer
```

(*) The specification of the option ```--network host``` is crucial, and some problems related to hazelcast could raise if omitted. The query-engine image itself could be obtained in the following way

```
docker run -p 8080:8080 --network host susanasrez/queryengine
```

Other modules, Crawler and CLeaner, are already running on the server which ip is specified on the dockerfiles among the project, but could be refactored to execute it locally. If so, take a look at the docker compose file, and make sure that both modules are running in the same computer. Make also sure that active mq is running before starting the app.

<br>
<h2>Credits</h2>


- [Adam Brez](https://github.com/breznada/)
- [Susana Suárez](https://github.com/susanasrez)
- [Mara Pareja](https://github.com/marapareja17)
- [Joaquín](https://github.com/JoaquinIP)
- [Ricardo Cárdnes](https://github.com/ricardocardn)
