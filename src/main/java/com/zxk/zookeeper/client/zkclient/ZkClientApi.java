package com.zxk.zookeeper.client.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Describe:  ZkClientApi
 * ZkClient是一个开源客户端，在Zookeeper原生API接口的基础上进行了包装，更便于开发人员使用。
 * 内部实现了Session超时重连，Watcher反复注册等功能。像dubbo等框架对其也进行了集成使用。
 * <p>
 * 不推荐上线使用
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/21 10:00
 * Attention:
 * Modify:
 */
public class ZkClientApi {

    private static Logger logger = LoggerFactory.getLogger(ZkClientApi.class);

    /**
     * zk服务地址
     */
    private static final String SERVER_ID = "127.0.0.1:2181";

    /**
     * 会话超时时长（ms）
     */
    private static final int SESSION_TIME_OUT = 2000;

    /**
     * 连接ZK超时时长（ms）
     */
    private static final int CONNECTION_TIME_OUT = 3000;

    /**
     * zookeeper 客户端
     */
    ZkClient zookeeper;

    public ZkClientApi() {
        connectionZk();
    }

    /**
     * 创建zk连接
     */
    private void connectionZk() {
        try {
            zookeeper = new ZkClient(SERVER_ID, CONNECTION_TIME_OUT);
            logger.info("创建zk连接成功");
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
        zookeeper.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                logger.error("节点{}，值更改为：{}", s, o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                logger.error("节点{}，值更被删除", s);
            }
        });
    }


    /**
     * 创建节点
     * ZkClient封装了许多创建节点的方法。
     * 其实这些创建节点的方法都是对原生API的一层封装而已，底层实现基本相同。值得留意的一点是，原生API的参数通过byte[]来传递节点内容，而ZkClient支持自定义序列化，因此可以传输Object对象。
     * createParents参数决定了是否递归创建父节点。true表示递归创建，false表示不使用递归创建。这也正是ZkClient帮开发人员省去了不少繁琐的检查和创建父节点的过程。
     *
     * @param path
     * @param data
     * @throws Exception
     */
    public void createNode(String path, String data) throws Exception {
        if (path == null) {
            throw new RuntimeException("节点路径不能为空");
        }
        String[] pathArr = path.split("/");
        if (pathArr.length == 2) {
            zookeeper.createPersistent(path, data);
            logger.info("创建zk节点成功，路径为: {}, 内容为：{}", path, data);
        } else {
            zookeeper.createPersistent(path, true);
            logger.info("创建zk节点成功，路径为: {}", path);
        }

    }


    /**
     * 校验节点是否存在
     *
     * @param path 节点路径
     * @return true：存在， false：不存在
     */
    public boolean exixtsNode(String path) throws Exception {
        boolean result = zookeeper.exists(path);
        logger.info("节点路径{}是否存在{}", path, result);
        return result;
    }

    /**
     * 删除节点（递归删除）
     * 重点说一下deleteRecursive接口，这个接口提供了递归删除的功能。在原生API中，如果一个节点存在子节点，那么它将无法直接删除，必须一层层遍历先删除全部子节点，然后才能将目标节点删除。
     *
     * @param path 删除节点
     * @throws Exception
     */
    public void deleteNode(String path) throws Exception {
        zookeeper.deleteRecursive(path);
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
        zookeeper.writeData(path, data);
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
        String data = zookeeper.readData(path);
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
        List<String> childs = zookeeper.getChildren(path);
        logger.info("读取zk节点所有子节点成功，路径为: {}", childs.toString());
        return childs;
    }

    /**
     * 关闭zk连接
     */
    public void close() throws Exception {
        zookeeper.close();
    }
}
