package com.zxk.zookeeper.remote;

import java.util.List;

/**
 * Describe: 远程服务操作接口
 *          封装了操作zk的接口，用于程序内部调用
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/20 10:24
 * Attention:
 * Modify:
 */
public interface RemoteOperationService {

    /**
     * 创建持久节点
     *
     * @param path 节点路径
     * @param data 数据
     */
    void createNode(String path, String data) throws Exception;

    /**
     * 删除节点
     *
     * @param path 节点路径
     */
    void deleteNode(String path) throws Exception;

    /**
     * 修改节点数据
     *
     * @param path 节点路径
     * @param data 数据
     * @throws Exception
     */
    void updateNodeDate(String path, String data) throws Exception;

    /**
     * 读取节点数据
     *
     * @param path 节点路径
     * @return 节点数据
     * @throws Exception
     */
    String readData(String path) throws Exception;

    /**
     * 获取指定节点下所有子节点
     *
     * @param path 节点路径
     * @return 子节点路径集合
     * @throws Exception
     */
    List<String> getAllChild(String path) throws Exception;

}
