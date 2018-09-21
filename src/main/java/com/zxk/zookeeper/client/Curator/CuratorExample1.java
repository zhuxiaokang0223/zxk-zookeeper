package com.zxk.zookeeper.client.Curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describe:  Curator 创建会话有两种方式
 *      1. 使用静态工厂方法创建客户端
 *      2. 使用静态工厂方法的Fluent风格创建客户端
 *
 *      本例使用今天工厂方法创建客户端
 *
 *      CuratorExample2 使用静态工厂方法的Fluent风格创建客户端
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/21 14:19
 * Attention:
 * Modify:
 */
public class CuratorExample1 {

    private static final Logger logger = LoggerFactory.getLogger(CuratorExample1.class);

    /** ZK地址 */
    private static final String CONNECT_STRING = "127.0.0.1:2181";

    /** 创建连接超时时长。 Curator默认15秒*/
    private static final int CONNECTION_TIME_OUT = 3000;

    /** 会话超时时长。 Curator默认60秒 */
    private static final int SESSION_TIME_OUT = 4000;

    /** 重试策略：初式时间为1秒，重试5次 */
    private static final RetryPolicy retryPolicy = new org.apache.curator.retry.ExponentialBackoffRetry(1000, 5);

    /**
     * 创建连接
     *      session超时时长不能小于创建连接超时时长
     * @return
     */
    public static CuratorFramework newClient(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECT_STRING, SESSION_TIME_OUT, CONNECTION_TIME_OUT, retryPolicy);
        // 当创建会话成功，得到client的实例然后可以直接调用其start( )方法
        curatorFramework.start();
        return curatorFramework;
    }

    public static void main(String[] args) {
        CuratorFramework client = CuratorExample1.newClient();
        try {
            String path = "/myCurator1";
            //创建
            client.create().forPath(path);
            logger.info("创建节点{}成功", path);
            //删除
            client.delete().forPath(path);
            logger.info("删除节点{}成功", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
