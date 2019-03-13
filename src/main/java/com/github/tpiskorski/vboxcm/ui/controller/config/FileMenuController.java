package com.github.tpiskorski.vboxcm.ui.controller.config;

import com.github.tpiskorski.vboxcm.config.ConfigService;
import com.github.tpiskorski.vboxcm.stub.dynamic.ServerStubMonitor;
import com.github.tpiskorski.vboxcm.ui.core.ContextAwareSceneLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class FileMenuController {

    private final ContextAwareSceneLoader contextAwareSceneLoader;
    private final ConfigurableApplicationContext springContext;
    private final ConfigService configService;

    @Autowired private ServerStubMonitor serverStubMonitor;

    @FXML private Alert reloadAlert;
    @FXML private Alert monitorAlert;

    @Autowired
    public FileMenuController(ConfigService configService, ContextAwareSceneLoader contextAwareSceneLoader, ConfigurableApplicationContext springContext) {
        this.configService = configService;
        this.contextAwareSceneLoader = contextAwareSceneLoader;
        this.springContext = springContext;
    }

    public void reloadConfig() {
        configService.reload();
        reloadAlert.showAndWait();
    }

    public void modify() throws IOException {
        contextAwareSceneLoader.loadAndShow("/fxml/menu/config/settings/containerWindow.fxml");
    }

    public void exit() {
        springContext.close();
        Platform.exit();
    }

    public void freezeMonitoring() {
        serverStubMonitor.freeze();
        monitorAlert.setContentText("Monitoring: "+serverStubMonitor.getIsFreezed().get());
        monitorAlert.showAndWait();
    }
}
