package ar.edu.pod.tp2.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("hz-config Server Starting ...");

        Config config = new Config();

        GroupConfig groupConfig = new GroupConfig().setName("g14").setPassword("g14-pass");
        config.setGroupConfig(groupConfig);

        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setEnabled(true)
                .setInterfaces(Collections.singletonList("127.0.0.*"));

        NetworkConfig networkConfig = new NetworkConfig().setInterfaces(interfacesConfig)
                .setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

//        ManagementCenterConfig managementCenterConfig  = new ManagementCenterConfig()
//                .setUrl("http://localhost:32768/mancenter/")
//                .setEnabled(true);

//        config.setManagementCenterConfig(managementCenterConfig);
        Hazelcast.newHazelcastInstance(config);


    }
}