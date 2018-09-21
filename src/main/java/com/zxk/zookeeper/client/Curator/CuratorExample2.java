package com.zxk.zookeeper.client.Curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describe:  Curator 创建会话有两种方式
 *      1. 使用静态工厂方法创建客户端
 *      2. 使用静态工厂方法的Fluent风格创建客户端
 *
 *      本例使用静态工厂方法的Fluent风格创建客户端
 *
 *      CuratorExample1 使用今天工厂方法创建客户端
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/21 15:32
 * Attention:
 * Modify:
 */
public class CuratorExample2 {

    private static final Logger logger = LoggerFactory.getLogger(CuratorExample1.class);

    /** 根目录 */
    private static final String NAMESPACE = "myCurator";

    /** ZK地址 */
    private static final String CONNECT_STRING = "127.0.0.1:2181";

    /** 创建连接超时时长。 Curator默认15秒*/
    private static final int CONNECTION_TIME_OUT = 3000;

    /** 会话超时时长。 Curator默认60秒 */
    private static final int SESSION_TIME_OUT = 4000;

    /** 重试策略：初式时间为1秒，重试5次 */
    private static final RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

    /**
     * 创建连接
     *      session超时时长不能小于创建连接超时时长
     * @return
     */
    public static CuratorFramework newClient(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .connectionTimeoutMs(CONNECTION_TIME_OUT)
                .sessionTimeoutMs(SESSION_TIME_OUT)
                .retryPolicy(retryPolicy)
                .namespace(NAMESPACE)
                .build();
        // 当创建会话成功，得到client的实例然后可以直接调用其start( )方法
        curatorFramework.start();
        return curatorFramework;
    }

    public static void main(String[] args) {
        CuratorFramework curClient = CuratorExample2.newClient();
        try {
            String path = "/myCurator2";
            //创建
            curClient.create().forPath(path, "".getBytes());
            logger.info("成功创建节点{}", path);
            //删除
            curClient.delete().deletingChildrenIfNeeded().forPath(path);
            logger.info("成功删除节点{}", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
