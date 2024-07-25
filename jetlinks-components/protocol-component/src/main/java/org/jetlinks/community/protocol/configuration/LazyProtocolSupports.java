package org.jetlinks.community.protocol.configuration;

import org.jetlinks.core.ProtocolSupports;
import org.jetlinks.core.defaults.CompositeProtocolSupports;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 懒加载协议支持
 * 在bean后置处理中把所有的协议支持注册到CompositeProtocolSupports中
 * @author weidian
 */
public class LazyProtocolSupports extends CompositeProtocolSupports implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof ProtocolSupports){
              register(((ProtocolSupports) bean));
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
