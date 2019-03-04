package com.github.tpiskorski.vboxcm.stub;

import com.github.tpiskorski.vboxcm.server.Server;
import com.github.tpiskorski.vboxcm.server.ServerService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Profile("stub")
@Component
public class StubServerMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StubServerMonitor.class);

    private final ServerService serverService;

    @Autowired public StubServerMonitor(ServerService serverService) {
        this.serverService = serverService;
    }

    @Scheduled(fixedRate = 10000L)
    public void monitor() {
        LOGGER.info("About to monitor...");
        ObservableList<Server> list = serverService.getServers();
        int randomElementIndex = ThreadLocalRandom.current().nextInt(list.size());

        Platform.runLater(() -> {
            Server server = list.get(randomElementIndex);
            System.out.println(server.getAddress().get());
            server.setReachable(!server.isReachable().get());
        });

        LOGGER.info("Finished monitor cycle");
    }
}

