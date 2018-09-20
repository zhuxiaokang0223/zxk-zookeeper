package com.zxk.zookeeper.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Describe: 使用zookeeper原生API连接zk
 * 需要实现Watcher
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/18 11:44
 * Attention:
 * Modify:
 */
public class ZookeeperApi implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperApi.class);

    /**
     * zk服务地址
     */
    private static final String SERVER_ID = "127.0.0.1:2181";

    /**
     * 会话超时时长（ms）
     */
    private static final int SESSION_TIME_OUT = 2000;

    /**
     * zookeeper 客户端
     */
    private static ZooKeeper zooKeeper = null;

    public ZookeeperApi() {
        connectionZk();
    }

    /**
     * 创建zk连接
     */
    private void connectionZk() {
        try {
            zooKeeper = new ZooKeeper(SERVER_ID, SESSION_TIME_OUT, this);
            Thread.sleep(3000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (zooKeeper == null || ZooKeeper.States.CONNECTING == zooKeeper.getState()) {
            throw new RuntimeException("创建zk连接失败");
        }
    }


    /**
     * 创建节点
     * create(path<节点路径>, data[]<节点内容>, List(ACL访问控制列表), CreateMode<zNode创建类型>) </p><br/>
     * <pre>
     *     节点创建类型(CreateMode)
     *     1、PERSISTENT:持久化节点
     *     2、PERSISTENT_SEQUENTIAL:顺序自动编号持久化节点，这种节点会根据当前已存在的节点数自动加 1
     *     3、EPHEMERAL:临时节点客户端,session超时这类节点就会被自动删除
     *     4、EPHEMERAL_SEQUENTIAL:临时自动编号节点
     * </pre>
     *
     * @param path
     * @param data
     * @throws Exception
     */
    public void createNode(String path, String data) throws Exception {
        try {
            // 创建开放权限的持久节点
            zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("创建zk节点成功，路径为: {}, 内容为：{}", path, data);
        } catch (KeeperException e) {
            logger.error("zk createNode，exception: {}", e);
            throw new Exception(e.getMessage());
        } catch (InterruptedException e) {
            logger.error("zk createNode, exception: {}", e);
            throw new Exception(e.getMessage());
        }
    }


    /**
     * 校验节点是否存在
     * exists(path<节点路径>, watch<并设置是否监控这个目录节点，这里的 watcher 是在创建 ZooKeeper 实例时指定的 watcher>)
     *
     * @param path 节点路径
     * @return true：存在， false：不存在
     */
    public boolean exixtsNode(String path) throws Exception {
        Stat stat;
        try {
            stat = zooKeeper.exists(path, false);
        } catch (KeeperException e) {
            logger.error("zk exixtsNode，exception: {}", e);
            throw new Exception(e.getMessage());
        } catch (InterruptedException e) {
            logger.error("zk exixtsNode，exception: {}", e);
            throw new Exception(e.getMessage());
        }
        return stat != null;
    }

    /**
     * 删除节点
     * delete(path<节点路径>, stat<数据版本号>)
     * 1、版本号不一致,无法进行数据删除操作.
     * 2、如果版本号与znode的版本号不一致,将无法删除,是一种乐观加锁机制;如果将版本号设置为-1,不会去检测版本,直接删除.
     *
     * @param path 删除节点
     * @throws Exception
     */
    public void deleteNode(String path) throws Exception {
        try {
            zooKeeper.delete(path, -1);
            logger.info("删除zk节点成功，路径为: {}", path);
        } catch (Exception e) {
            logger.error("zk deleteNode，exception: {}", e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 修改节点内容
     * setData(path<节点路径>, data[]<节点内容>, stat<数据版本号>)
     * 设置某个znode上的数据时如果为-1，跳过版本检查
     *
     * @param path 节点路径
     * @param data 数据
     * @throws Exception
     */
    public void updateNode(String path, String data) throws Exception {
        try {
            zooKeeper.setData(path, data.getBytes(), -1);
            logger.info("修改zk节点数据成功，路径为: {}, 内容为：{}", path, data);
        } catch (Exception e) {
            logger.error("zk updateNode，exception: {}", e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 读取节点内容
     * getData(path<节点路径>, watcher<监视器>, stat<数据版本号>)
     *
     * @param path
     * @return
     * @throws Exception
     */
    public String readNode(String path) throws Exception {
        String data;
        try {
            data = new String(zooKeeper.getData(path, false, null));
            logger.info("读取zk节点数据成功，路径为: {}, 内容为：{}", path, data);
        } catch (Exception e) {
            logger.error("zk readNode，exception: {}", e);
            throw new Exception(e.getMessage());
        }
        return data;
    }

    /**
     * 查询所有子节点
     * getChildren(path<节点路径>, watcher<监视器>)该方法有多个重载
     *
     * @param path 目标节点路径
     * @return
     * @throws Exception
     */
    public List<String> getAllChilds(String path) throws Exception {
        List<String> childs;
        try {
            childs = zooKeeper.getChildren(path, false);
            logger.info("读取zk节点所有子节点成功，路径为: {}", childs.toString());
        } catch (Exception e) {
            logger.error("zk getAllChilds，exception: {}", e);
            throw new Exception(e.getMessage());
        }
        return childs;
    }

    /**
     * 关闭zk连接
     */
    public void close() throws Exception {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                logger.error("zk close, exception: {}", e);
                throw new Exception(e.getMessage());
            }
        }
    }

    /**
     * 监控所有被触发的事件
     */
    @Override
    public void process(WatchedEvent event) {
        logger.info("收到事件通知：" + event.getState());
    }
}
