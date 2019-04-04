package tpiskorski.vboxcm.shutdown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import tpiskorski.vboxcm.core.server.ServerService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppStatePersister {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppStatePersister.class);

    private final ServerService serverService;

    private final Environment env;

    @Autowired public AppStatePersister(ServerService serverService, Environment env) {
        this.serverService = serverService;
        this.env = env;
    }

    public void persist() {
        if (List.of(env.getActiveProfiles()).contains("dev")) {
            LOGGER.info("Not persisting anything because spring dev profile is active");
            return;
        }

        try {
            LOGGER.info("Starting server persistance");
            serializeServers();
            LOGGER.info("Persisted servers");
        } catch (Exception ex) {
            LOGGER.error("Could not persist servers", ex);
        }
    }

    private void serializeServers() throws IOException {
        List<SerializableServer> toSerialize = serverService.getServers().stream()
            .map(SerializableServer::new)
            .collect(Collectors.toList());

        writeSerializedServers(toSerialize);
    }

    private void writeSerializedServers(List<SerializableServer> toSerialize) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("servers.dat");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(toSerialize);
        objectOutputStream.flush();
        objectOutputStream.close();
    }
}