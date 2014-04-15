package org.terracotta.demo;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by vch on 4/13/14.
 */
public class CacheAction {

    @Resource(name = "balanceCacheBean")
    private Cache cache;

    public Cache getCache() {
        return cache;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void put(String key, String value) throws Exception {
        cache.put(new Element(key, value));
        if (key.startsWith("raiseException")) {
            throw new Exception("cause rollback");
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object get(String key) {
        return cache.get(key);

    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 3)
    public void longPut(int sleepTime,String key) throws Exception {
        cache.put(new Element(key, "long-value-timesouts-1"));
        Thread.sleep(sleepTime);
    }


}
