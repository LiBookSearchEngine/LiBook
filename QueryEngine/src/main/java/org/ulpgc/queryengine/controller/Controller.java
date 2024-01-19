package org.ulpgc.queryengine.controller;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.ulpgc.queryengine.controller.readDatalake.CleanerAPIClient;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastStats;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastWords;
import org.ulpgc.queryengine.view.API;

import java.io.IOException;

public class Controller {

    public Controller(String ip, int port) throws IOException {
        CleanerAPIClient cleanerAPIClient = new CleanerAPIClient(ip);
        HazelcastInstance hazelcastInstance = initializeHaZelcast();
        ReadHazelcastWords readDatamartFiles= new ReadHazelcastWords(hazelcastInstance, cleanerAPIClient);
        ReadHazelcastStats readDatamartStats = new ReadHazelcastStats(hazelcastInstance);
        API.runAPI(readDatamartFiles, readDatamartStats, port, cleanerAPIClient);

    }

    private static HazelcastInstance initializeHaZelcast(){
        Config config = new Config();
        config.setInstanceName("instance");
        return Hazelcast.newHazelcastInstance(config);
    }

}
