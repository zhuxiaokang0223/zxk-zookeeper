package com.zxk.zookeeper.factory;

import com.zxk.zookeeper.base.RemoteOperationService;

/**
 * Describe: zookeeper客户端工厂
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/20 10:59
 * Attention:
 * Modify:
 */
public class RemoteOperationFactory {

    /**
     * 创建远程服务操作工厂
     *
     * @param type 服务类型
     * @return
     */
    public static RemoteOperationService createRemoteOperation(int type) {
        RemoteOperationService remoteOperationService;
        switch (type) {
            case 1:
                remoteOperationService = new com.zxk.zookeeper.client.impl.ZookeeperApiRemoteImpl();
                break;
            default:
                remoteOperationService = new com.zxk.zookeeper.client.impl.ZookeeperApiRemoteImpl();
                break;
        }
        return remoteOperationService;
    }

}
