package org.jetlinks.community.gateway.monitor;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
@Slf4j
public class GatewayMonitors {


    private static final List<DeviceGatewayMonitorSupplier> deviceGatewayMonitorSuppliers = new CopyOnWriteArrayList<>();

    static final NoneDeviceGatewayMonitor nonDevice = new NoneDeviceGatewayMonitor();


    public static void register(DeviceGatewayMonitorSupplier supplier) {
        deviceGatewayMonitorSuppliers.add(supplier);
    }

    private static DeviceGatewayMonitor doGetDeviceGatewayMonitor(String id, String... tags) {
        log.warn("设备接入创建网关消息监听器 doGetDeviceGatewayMonitor::id = {}, tags = {}", id, tags);
        List<DeviceGatewayMonitor> all = deviceGatewayMonitorSuppliers.stream()
            .map(supplier -> supplier.getDeviceGatewayMonitor(id, tags))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (all.isEmpty()) {
            return nonDevice;
        }
        if (all.size() == 1) {
            return all.get(0);
        }
        CompositeDeviceGatewayMonitor monitor = new CompositeDeviceGatewayMonitor();
        monitor.add(all);
        return monitor;
    }

    public static DeviceGatewayMonitor getDeviceGatewayMonitor(String id, String... tags) {
        return new LazyDeviceGatewayMonitor(() -> doGetDeviceGatewayMonitor(id, tags));
    }
}
