package com.zxk.zookeeper.api;

import com.zxk.zookeeper.base.RemoteOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describe:
 *
 * @author : ZhuXiaokang
 * @mail : xiaokang.zhu@pactera.com
 * @date : 2018/9/19 16:02
 * Attention:
 * Modify:
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static RemoteOperationService remoteOperationService;

    /**
     * zookeeper 原生api操作
     * @throws Exception
     */
    private static void zkApi() throws Exception{
        remoteOperationService = com.zxk.zookeeper.factory.RemoteOperationFactory.createRemoteOperation(1);
        String path = "/myZk";
        String path1 = "/myZk/test1";
        String path2 = "/myZk/test2";
        String path3 = "/myZk/test2/tt";

        remoteOperationService.createNode(path, "");
        remoteOperationService.createNode(path1, "");
        remoteOperationService.createNode(path2, "");
        remoteOperationService.createNode(path3, "");
        remoteOperationService.getAllChild(path);
        remoteOperationService.deleteNode(path1);
        remoteOperationService.createNode(path1, "");
        remoteOperationService.updateNodeDate(path1, "我俏丽吗");
        remoteOperationService.readData(path1);

        remoteOperationService.deleteNode(path3);
        remoteOperationService.deleteNode(path2);
        remoteOperationService.deleteNode(path1);
        remoteOperationService.deleteNode(path);
    }

    public static void main(String[] args) throws Exception {
        System.err.println("开始操作............");
        //zk 原生 api操作
        zkApi();

        System.err.println("操作完成............");
    }
}
