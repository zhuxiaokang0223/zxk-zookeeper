package com.zxk.zookeeper.api;

import com.zxk.zookeeper.factory.RemoteOperationFactory;
import com.zxk.zookeeper.remote.RemoteOperationService;
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
     *
     * @throws Exception
     */
    private static void zkApi() throws Exception {
        remoteOperationService = RemoteOperationFactory.createRemoteOperation(1);
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

    /**
     * ZkClient操作
     *
     * @throws Exception
     */
    private static void zkClient() throws Exception {
        remoteOperationService = RemoteOperationFactory.createRemoteOperation(2);
        String path = "/myZkClient";
        String path1 = "/myZkClient/test1";
        String path2 = "/myZkClient/test2";
        String path3 = "/myZkClient/test2/tt";

        remoteOperationService.createNode(path1, "");
        remoteOperationService.createNode(path2, "");
        remoteOperationService.createNode(path3, "");
        remoteOperationService.getAllChild(path);
        remoteOperationService.updateNodeDate(path, "我敲你啊");
        remoteOperationService.readData(path);

        remoteOperationService.deleteNode(path);
    }

    /**
     * Curator 操作
     *
     * @throws Exception
     */
    private static void curatorClient() throws Exception {
        remoteOperationService = RemoteOperationFactory.createRemoteOperation(3);
        String path = "/myCuratorClient";
        String path1 = "/myCuratorClient/test1";
        String path2 = "/myCuratorClient/test2";
        String path3 = "/myCuratorClient/test2/tt";

        remoteOperationService.createNode(path1, "");
        remoteOperationService.createNode(path2, "");
        remoteOperationService.createNode(path3, "");
        remoteOperationService.getAllChild(path);
        remoteOperationService.updateNodeDate(path, "我敲你啊");
        remoteOperationService.readData(path);

        remoteOperationService.deleteNode(path);
    }

    public static void main(String[] args) throws Exception {
        System.err.println("开始操作............");
        //zk 原生 api操作
        //zkApi();

        //zkClient api操作
        //zkClient();

        // Curator api操作
        curatorClient();

        System.err.println("操作完成............");
    }
}
