package com.zxk.zookeeper.remote.impl;

import com.zxk.zookeeper.client.zkclient.ZkClientApi;
import com.zxk.zookeeper.remote.RemoteOperationService;

import java.util.List;

/**
 * Describe: zkClient 远程操作zookeeper
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/21 11:21
 * Attention:
 * Modify:
 */
public class ZkClientRemoteImpl implements RemoteOperationService {

    /**
     * zk 原生API 客户端操作
     */
    private ZkClientApi zkClient = null;

    public ZkClientRemoteImpl() {
        try {
            this.zkClient = new ZkClientApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建持久节点
     *
     * @param path 节点路径
     * @param data 数据
     * @throws Exception
     */
    @Override
    public void createNode(String path, String data) throws Exception {
        // 校验节点是否存在
        boolean exists = zkClient.exixtsNode(path);
        if (exists) {
            throw new Exception("节点{" + path + "}已存在");
        }
        // 创建节点
        zkClient.createNode(path, data);
    }

    /**
     * 删除节点
     *
     * @param path 节点路径
     * @throws Exception
     */
    @Override
    public void deleteNode(String path) throws Exception {
        // 删除节点
        zkClient.deleteNode(path);
    }

    /**
     * 修改节点数据
     *
     * @param path 节点路径
     * @param data 数据
     * @throws Exception
     */
    @Override
    public void updateNodeDate(String path, String data) throws Exception {
        // 校验节点是否存在
        boolean exists = zkClient.exixtsNode(path);
        if (!exists) {
            throw new Exception("节点{" + path + "}不存在");
        }
        // 修改节点内容
        zkClient.updateNode(path, data);
    }

    /**
     * 读取节点数据
     *
     * @param path 节点路径
     * @return
     * @throws Exception
     */
    @Override
    public String readData(String path) throws Exception {
        // 校验节点是否存在
        boolean exists = zkClient.exixtsNode(path);
        if (!exists) {
            throw new Exception("节点{" + path + "}不存在");
        }
        // 获取节点数据
        return zkClient.readNode(path);
    }

    /**
     * 获取目标节点的所有子节点
     *
     * @param path 节点路径
     * @return
     * @throws Exception
     */
    @Override
    public List<String> getAllChild(String path) throws Exception {
        return zkClient.getAllChilds(path);
    }
}
