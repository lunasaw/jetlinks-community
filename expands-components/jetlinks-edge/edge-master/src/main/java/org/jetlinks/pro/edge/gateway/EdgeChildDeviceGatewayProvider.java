package org.jetlinks.pro.edge.gateway;

import lombok.extern.slf4j.Slf4j;
import org.jetlinks.community.gateway.AbstractDeviceGateway;
import org.jetlinks.community.gateway.DeviceGateway;
import org.jetlinks.community.gateway.supports.DeviceGatewayProperties;
import org.jetlinks.community.gateway.supports.DeviceGatewayProvider;
import org.jetlinks.core.message.codec.Transport;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

//边缘端子设备网关提供器，参看 ChildDeviceGatewayProvider
@Slf4j
@Component
public class EdgeChildDeviceGatewayProvider implements DeviceGatewayProvider {

    @Override
    public String getId() {
        return "edge-child-device";
    }

    @Override
    public String getName() {
        return "边缘网关子设备接入";
    }

    @Override
    public String getDescription() {
        return "适用于通过官方边缘网关代理接入的设备";
    }

    //接入方式：子设备
    @Override
    public String getChannel() {
        return "edge-child-device";
    }

    /**
     * 传输协议
     * @return 返回传输协议
     */
    @Override
    public Transport getTransport() {
//        return DefaultTransport.MQTT;
        return Transport.of("EdgeGateway");
    }

    @Override
    public Mono<? extends DeviceGateway> createDeviceGateway(DeviceGatewayProperties properties) {
        return Mono.just(new EdgeChildDeviceGateway(properties.getId()));
    }

    //边缘端子设备网关
    static class EdgeChildDeviceGateway extends AbstractDeviceGateway {

        public EdgeChildDeviceGateway(String id) {
            super(id);
        }

        @Override
        protected Mono<Void> doShutdown() {
            return Mono.empty();
        }

        @Override
        protected Mono<Void> doStartup() {
            return Mono.empty();
        }
    }
}
