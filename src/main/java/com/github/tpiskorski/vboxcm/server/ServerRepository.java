package com.github.tpiskorski.vboxcm.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Repository;

@Repository
public class ServerRepository {

    private final ObservableList<Server> serverObservableList = FXCollections.observableArrayList(Server.extractor());

    void add(Server server) {
        serverObservableList.add(server);
    }

    ObservableList<Server> getServersList() {
        return serverObservableList;
    }

    void remove(Server server) {
        serverObservableList.remove(server);
    }
}
