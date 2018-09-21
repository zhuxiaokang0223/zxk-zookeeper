package com.zxk.zookeeper.factory;

import com.zxk.zookeeper.remote.RemoteOperationService;
import com.zxk.zookeeper.remote.impl.ZkClientRemoteImpl;
import com.zxk.zookeeper.remote.impl.ZookeeperApiRemoteImpl;

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
                remoteOperationService = new ZookeeperApiRemoteImpl();
                break;
            case 2:
                remoteOperationService = new ZkClientRemoteImpl();
                break;
            default:
                remoteOperationService = new ZookeeperApiRemoteImpl();
                break;
        }
        return remoteOperationService;
    }

}
