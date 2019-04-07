package tpiskorski.machinator.quartz.monitor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import tpiskorski.machinator.core.server.Server;
import tpiskorski.machinator.core.server.ServerService;
import tpiskorski.machinator.core.server.ServerType;
import tpiskorski.machinator.core.vm.VirtualMachine;

import java.util.List;

@Component
public class ServerRefreshJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRefreshJob.class);

    private ServerMonitoringService serverMonitoringService;
    private ServerService serverService;

    @Autowired
    public ServerRefreshJob(ServerMonitoringService serverMonitoringService, @Lazy ServerService serverService) {
        this.serverMonitoringService = serverMonitoringService;
        this.serverService = serverService;
    }

    @Override protected void executeInternal(JobExecutionContext jobExecutionContext) {
        LOGGER.info("Servers scan started...");
        try {
            ObservableList<Server> serversView = FXCollections.observableArrayList(serverService.getServers());
            for (Server server : serversView) {
                if (server.getServerType() == ServerType.LOCAL) {
                    List<VirtualMachine> vms = serverMonitoringService.monitor(server);
                    Platform.runLater(() -> serverService.upsert(server, vms));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Server scan error", e);
        }
        LOGGER.info("Servers scan finished...");
    }
}