package com.zxk.zookeeper.factory;

import com.zxk.zookeeper.base.RemoteOperationService;

/**
 * Describe:
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
                remoteOperationService = new com.zxk.zookeeper.client.impl.ZookeeperApiRemote1Impl();
                break;
            default:
                remoteOperationService = new com.zxk.zookeeper.client.impl.ZookeeperApiRemote1Impl();
                break;
        }
        return remoteOperationService;
    }

}
