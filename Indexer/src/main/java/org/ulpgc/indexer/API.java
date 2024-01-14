package org.ulpgc.indexer;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;

import static spark.Spark.get;
import static spark.Spark.port;

public class API {
    private final MultiMap<Object, Object> invertedIndex;

    public API() {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        invertedIndex = hazelcastInstance.getMultiMap("invertedIndex");
    }

    public void run() {
        port(8082);
        get("word/:word", (req, res) -> invertedIndex.get(req.params("word")));
        get("indexed/count", (req, res) -> Main.INDEXED_BOOKS);
    }
}
