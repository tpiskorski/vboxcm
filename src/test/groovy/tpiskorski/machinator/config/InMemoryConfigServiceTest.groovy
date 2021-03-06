package tpiskorski.machinator.config

import tpiskorski.machinator.config.io.InternalConfigLoader
import spock.lang.Specification
import spock.lang.Subject

class InMemoryConfigServiceTest extends Specification {

    def internalConfigLoader = Mock(InternalConfigLoader)

    @Subject service = new InMemoryConfigService(internalConfigLoader)

    def 'should load internal config'() {
        when:
        service.loadConfig()

        then:
        1 * internalConfigLoader.loadInternalConfig()
    }
}
