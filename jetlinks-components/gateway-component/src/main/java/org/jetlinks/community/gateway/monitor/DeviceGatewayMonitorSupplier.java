package org.jetlinks.community.gateway.monitor;

/**
 * 设备网关
 * @author weidian
 */
public interface DeviceGatewayMonitorSupplier {
      DeviceGatewayMonitor getDeviceGatewayMonitor(String id, String... tags);

}
