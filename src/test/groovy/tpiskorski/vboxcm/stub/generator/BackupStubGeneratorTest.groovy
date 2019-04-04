package tpiskorski.vboxcm.stub.generator

import tpiskorski.vboxcm.core.backup.Backup
import tpiskorski.vboxcm.core.backup.BackupService
import tpiskorski.vboxcm.core.server.Server
import tpiskorski.vboxcm.core.vm.VirtualMachine
import tpiskorski.vboxcm.core.vm.VirtualMachineService
import javafx.collections.ObservableList
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class BackupStubGeneratorTest extends Specification {

    def backupService = Mock(BackupService)
    def virtualMachineService = Mock(VirtualMachineService)

    @Subject generator = new BackupStubGenerator(
            backupService, virtualMachineService
    )

    def 'should create backup for vm'() {
        given:
        def server = new Server('localhost', '10')
        def vm = new VirtualMachine(server, 'id1')

        when:
        def backup = generator.createBackupForVm(vm)

        then:
        backup.server == server
        backup.vm == vm
        backup.currentFiles <= backup.fileLimit
    }

    @Unroll
    def 'should generate backups for vms'() {
        given:
        def vms = [Mock(VirtualMachine)] * vmNumber as ObservableList

        when:
        generator.afterPropertiesSet()

        then:
        1 * virtualMachineService.getVms() >> vms
        expectedBackups * backupService.add(_ as Backup)

        where:
        vmNumber || expectedBackups
        10       || 5
        5        || 3
        1        || 1
    }
}