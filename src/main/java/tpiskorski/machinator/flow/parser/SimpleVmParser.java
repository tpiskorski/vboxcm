package tpiskorski.machinator.flow.parser;

import tpiskorski.machinator.flow.command.CommandResult;
import tpiskorski.machinator.model.vm.VirtualMachine;
import tpiskorski.machinator.model.vm.VirtualMachineState;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleVmParser {

    private static final String NEW_LINE = "\n";
    private static final Pattern SPLIT_PATTERN = Pattern.compile(NEW_LINE);

    public List<VirtualMachine> parse(CommandResult commandResult) {
        String std = commandResult.getStd();
        return SPLIT_PATTERN.splitAsStream(std)
            .filter(Predicate.not(String::isEmpty))
            .map(this::mapToVm)
            .collect(Collectors.toList());
    }

    private VirtualMachine mapToVm(String vmString) {
        String vmName = vmString.substring(vmString.indexOf("\"") + 1, vmString.lastIndexOf("\""));
        String vmId = vmString.substring(vmString.indexOf("{") + 1, vmString.lastIndexOf("}"));

        VirtualMachine vm = new VirtualMachine();
        vm.setId(vmId);
        vm.setVmName(vmName);
        vm.setState(VirtualMachineState.RUNNING);

        return vm;
    }
}