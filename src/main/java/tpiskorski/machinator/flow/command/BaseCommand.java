package tpiskorski.machinator.flow.command;

public enum BaseCommand {
    LIST_ALL_VMS("VBoxManage list vms"),
    IS_VBOX_INSTALLED("VBoxManage --version"),
    SHOW_VM_INFO("VBoxManage showvminfo --machinereadable \"%s\""),

    EXPORT_VM("VBoxManage export --ovf20 --output \"%s\" \"%s\""),
    IMPORT_VM("VBoxManage import \"%s\""),
    DELETE_VM("VBoxManage unregistervm --delete \"%s\""),

    START_VM("VBoxManage startvm --type headless \"%s\""),
    RESET_VM("VBoxManage controlvm \"%s\" reset"),
    ACPI_SHUTDOWN("VBoxManage controlvm \"%s\" acpipowerbutton"),
    POWER_OFF("VBoxManage controlvm \"%s\" poweroff"),

    RM_FILES("rm -rf %s");

    private final String baseCommand;

    BaseCommand(String baseCommand) {
        this.baseCommand = baseCommand;
    }

    public String asString() {
        return baseCommand;
    }
}
