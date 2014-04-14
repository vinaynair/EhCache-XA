package org.terracotta.demo.test;

import net.sf.ehcache.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.terracotta.demo.CacheAction;

import javax.annotation.Resource;

/**
 * Created by vch on 4/13/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/simple-ehcache-context.xml"})
public class EhCacheXATest {

    @Resource(name = "cacheActionBean")
    private CacheAction cacheActionBean;
    @Resource(name = "transactionManager")
    private PlatformTransactionManager txManager;

    @Before
    public void testCacheExists() {
        Assert.assertNotNull(cacheActionBean);
    }

    @Test
    public void testXACacheFailsWhenCalledOutsideJTAContext() {
        try {
            cacheActionBean.getCache().put(new Element("key1", "value1"));
            Assert.fail("XA cache can only operate under JTA");
        } catch (Exception e) {

        }
    }

    @Test
    public void testXACacheWorksUnderJTA() {
        performXACacheOperationWithOUTAnnotation();
        Assert.assertNotNull(cacheActionBean.get("key-tx-1"));
        try {
            cacheActionBean.put("raiseException-key-tx-2", "value-2");
        } catch (Exception e) {
            //ignore
        }

        Assert.assertNull("Rollback should not have updated the cache", cacheActionBean.get("raiseException-key-tx-2"));
    }


    private void performXACacheOperationWithOUTAnnotation() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // explicitly setting the transaction name is something that can only be done programmatically
        def.setName("CommitEhcachePut");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = txManager.getTransaction(def);
        cacheActionBean.getCache().put(new Element("key-tx-1", "value-tx-1"));
        txManager.commit(status);
    }


    @Test
    public void testBulk() throws Exception {
//        for (int i = 0; i < 1000000; i++) {
////            Thread.sleep(10);
//            cacheActionBean.put("key-" + i, "value-" + i);
//        }
//        Cache cache= cacheActionBean.getCache();
//        cache.setNodeBulkLoadEnabled(true);
//        cache.put(new Element("bulk-key-1","bulk-value-1"));
//        cache.setNodeBulkLoadEnabled(false);
    }

}
