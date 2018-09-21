package com.zxk.zookeeper.client.Curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * Describe: Curator API
 * Curator是Netflix公司开源的一套Zookeeper客户端框架，和ZkClient一样，解决了非常底层的细节开发工作，
 * 包括连接重连、反复注册Watcher和NodeExistsException异常等。目前已经成为Apache的顶级项目。
 * 外还提供了一套易用性和可读性更强的Fluent风格的客户端API框架。
 * 除此之外，Curator中还提供了Zookeeper各种应用场景（Recipe，如共享锁服务、Master选举机制和分布式计算器等）的抽象封装。
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/21 11:37
 * Attention:
 * Modify:
 */
public class CuratorApi {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CuratorExample1.class);

    /**
     * 根目录
     */
    private static final String NAMESPACE = "myCurator";

    /**
     * ZK地址
     */
    private static final String CONNECT_STRING = "127.0.0.1:2181";

    /**
     * 创建连接超时时长。 Curator默认15秒
     */
    private static final int CONNECTION_TIME_OUT = 3000;

    /**
     * 会话超时时长。 Curator默认60秒
     */
    private static final int SESSION_TIME_OUT = 4000;

    /**
     * 重试策略：初式时间为1秒，重试5次
     */
    private static final RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

    /**
     * zookeeper 客户端
     */
    CuratorFramework zookeeper;

    public CuratorApi() {
        connectionZk();
        zkListener(null);
        start();
        logger.info("创建zk连接成功");
    }

    /**
     * 创建zk连接
     */
    private void connectionZk() {
        try {
            zookeeper = org.apache.curator.framework.CuratorFrameworkFactory.builder()
                    .connectString(CONNECT_STRING)
                    .connectionTimeoutMs(CONNECTION_TIME_OUT)
                    .sessionTimeoutMs(SESSION_TIME_OUT)
                    .retryPolicy(retryPolicy)
                    .namespace(NAMESPACE).build();

        } catch (Exception e) {
            throw new RuntimeException("创建zk连接失败, exception: {}", e);
        }
    }

    /**
     * 监听节点变更
     *
     * @param path
     */
    public void zkListener(String path) {
        zookeeper.getCuratorListenable().addListener((client1,event) -> {logger.info("CuratorEvent: " + event.getType().name());});
    }


    /**
     * 创建节点
     *     创建一个完全开放权限的持久节点。  且自动递归创建父节点
     * @param path
     * @param data
     * @throws Exception
     */
    public void createNode(String path, String data) throws Exception {
        zookeeper.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path, data.getBytes());
        logger.info("创建zk节点成功，路径为: {}, 内容为：{}", path, data);
    }


    /**
     * 校验节点是否存在
     *
     * @param path 节点路径
     * @return true：存在， false：不存在
     */
    public boolean exixtsNode(String path) throws Exception {
        Stat stat;
        try {
            stat = zookeeper.checkExists().forPath(path);
        } catch (org.apache.zookeeper.KeeperException e) {
            logger.error("zk exixtsNode，exception: {}", e);
            throw new Exception(e.getMessage());
        } catch (InterruptedException e) {
            logger.error("zk exixtsNode，exception: {}", e);
            throw new Exception(e.getMessage());
        }
        return stat != null;
    }

    /**
     * 删除节点（递归删除）
     *   guaranteed 强制删除， 只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功。
     * @param path 删除节点
     * @throws Exception
     */
    public void deleteNode(String path) throws Exception {
        zookeeper.delete()
                .guaranteed()
                .deletingChildrenIfNeeded()
                .withVersion(-1)
                .forPath(path);
        logger.info("删除zk节点成功，路径为: {}", path);
    }

    /**
     * 修改节点内容
     *
     * @param path 节点路径
     * @param data 数据
     * @throws Exception
     */
    public void updateNode(String path, String data) throws Exception {
        zookeeper.setData().forPath(path, data.getBytes());
        logger.info("修改zk节点数据成功，路径为: {}, 内容为：{}", path, data);
    }

    /**
     * 读取节点内容
     *
     * @param path
     * @return
     * @throws Exception
     */
    public String readNode(String path) throws Exception {
        String data = new String(zookeeper.getData().forPath(path));
        logger.info("读取zk节点数据成功，路径为: {}, 内容为：{}", path, data);
        return data;
    }

    /**
     * 查询所有子节点
     *
     * @param path 目标节点路径
     * @return
     * @throws Exception
     */
    public List<String> getAllChilds(String path) throws Exception {
        List<String> childs = zookeeper.getChildren().forPath(path);
        logger.info("读取zk节点所有子节点成功，路径为: {}", childs.toString());
        return childs;
    }

    /**
     * 事务
     *    TODO 此方法执行失败， 有时间再看看原因
     * @throws Exception
     */
    public void transcationCreateNode() throws Exception {
        CuratorOp checkOp = zookeeper.transactionOp().check().forPath("/a/path");
        CuratorOp createOp = zookeeper.transactionOp().create().forPath("/a/path", "some data".getBytes());
        CuratorOp setDataOp = zookeeper.transactionOp().setData().forPath("/another/path", "other data".getBytes());
        CuratorOp deleteOp = zookeeper.transactionOp().delete().forPath("/yet/another/path");

        Collection<CuratorTransactionResult> results = zookeeper.transaction().forOperations(checkOp, createOp, setDataOp, deleteOp);

        for ( CuratorTransactionResult result : results )
        {
           logger.info(result.getForPath() + " - " + result.getType());
        }
    }

    /**
     * 开启连接
     */
    public void start() {
        zookeeper.start();
    }

    /**
     * 关闭zk连接
     */
    public void close() throws Exception {
        zookeeper.close();
    }
}
