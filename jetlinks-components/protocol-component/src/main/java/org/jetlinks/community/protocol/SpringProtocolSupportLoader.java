package org.jetlinks.community.protocol;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.core.ProtocolSupport;
import org.jetlinks.core.event.EventBus;
import org.jetlinks.supports.protocol.management.ProtocolSupportDefinition;
import org.jetlinks.supports.protocol.management.ProtocolSupportLoader;
import org.jetlinks.supports.protocol.management.ProtocolSupportLoaderProvider;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用Spring管理协议加载器,实现{@link ProtocolSupportLoaderProvider}并注入到Spring即可。
 *
 * @author zhouhao
 * @since 1.0
 */
@AllArgsConstructor
@Generated
@Slf4j
public class SpringProtocolSupportLoader implements ProtocolSupportLoader {

    private final Map<String, ProtocolSupportLoaderProvider> providers = new ConcurrentHashMap<>();

    private final EventBus eventBus;

    public void register(ProtocolSupportLoaderProvider provider) {
        this.providers.put(provider.getProvider(), provider);
    }

    @Override
    public Mono<? extends ProtocolSupport> load(ProtocolSupportDefinition definition) {
        return Mono
            .justOrEmpty(this.providers.get(definition.getProvider()))
            .switchIfEmpty(Mono.error(() -> new UnsupportedOperationException("unsupported provider:" + definition.getProvider())))
            .flatMap((provider) -> {
                Mono<? extends ProtocolSupport> load = provider.load(definition);
                log.info("协议加载完成 load::load = {}", load.blockOptional().get().getDescription());
                return load;
            })
            .map(loaded -> new RenameProtocolSupport(definition.getId(), definition.getName(), definition.getDescription(), loaded, eventBus));
    }

}
