package ar.edu.pod.tp2.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Client {
    private String path;

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        logger.info("tpe2-g14 Client Starting ...");
        ClientConfig clientConfig = new ClientConfig();

        GroupConfig groupConfig = new GroupConfig()
                .setName("g14").setPassword("g14-pass");
        clientConfig.setGroupConfig(groupConfig);

        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        String[] addresses = {"192.168.0.30:5701"};
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        String mapName = "query1";

        IMap<Integer, String> testMapFromMember = hazelcastInstance.getMap(mapName);


        HazelcastClient.shutdownAll();

    }
}