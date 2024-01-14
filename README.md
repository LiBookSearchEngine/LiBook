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
- [Ricardo Cárdnes](https://github.com/ricardocardn)
- [Mara Pareja](https://github.com/marapareja17)
- [Joaquín](https://github.com/JoaquinIP)
