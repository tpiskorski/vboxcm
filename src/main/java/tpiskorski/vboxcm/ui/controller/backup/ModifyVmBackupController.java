package tpiskorski.vboxcm.ui.controller.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import tpiskorski.vboxcm.core.backup.Backup;
import tpiskorski.vboxcm.core.backup.BackupService;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class ModifyVmBackupController {

    private final BackupService backupService;

    @FXML private Button modifyButton;
    @FXML private Button cancelButton;
    @FXML private TextField fileLimit;
    @FXML private TextField backupTime;
    @FXML private TextField frequency;
    @FXML private DatePicker firstBackup;
    @FXML private TextField vmComboBox;
    @FXML private TextField serverComboBox;

    private Backup savedBackup;

    @Autowired public ModifyVmBackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @FXML
    public void modify() {
        Backup backup = new Backup(savedBackup.getServer(), savedBackup.getVm());

        backup.setFirstBackupDay(LocalDate.parse(firstBackup.getEditor().getText()));
        backup.setFrequency(Integer.parseInt(frequency.getText()));
        backup.setBackupTime(LocalTime.parse(backupTime.getText()));
        backup.setFileLimit(Integer.parseInt(fileLimit.getText()));

        backupService.update(backup);

        ((Stage) modifyButton.getScene().getWindow()).close();
    }

    @FXML
    public void close() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    void clear() {
        serverComboBox.clear();
        vmComboBox.clear();
        firstBackup.getEditor().clear();
        frequency.clear();
        backupTime.clear();
        fileLimit.clear();
    }

    void prepareFor(Backup backup) {
        savedBackup = backup;

        serverComboBox.setText(backup.getServer().toString());
        vmComboBox.setText(backup.getVm().toString());
        firstBackup.getEditor().setText(backup.getFirstBackupDay().toString());
        frequency.setText("" + backup.getFrequency());
        backupTime.setText(backup.getBackupTime().toString());
        fileLimit.setText("" + backup.getFileLimit());
    }
}
