package com.moxi.mogublog.picture.restapi;

import com.alibaba.fastjson.JSON;
import com.moxi.mogublog.commons.entity.NetworkDisk;
import com.moxi.mogublog.picture.entity.TreeNode;
import com.moxi.mogublog.picture.global.MessageConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.NetworkDiskService;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.vo.NetworkDiskVO;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.upload.FileOperation;
import com.moxi.mogublog.utils.upload.FileUtil;
import com.moxi.mogublog.utils.upload.PathUtil;
import com.moxi.mougblog.base.holder.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;

/**
 * 网盘管理RestApi
 *
 * @author 陌溪
 * @date 2020年10月8日08:38:33
 */
@RestController
@RequestMapping("/networkDisk")
@Api(value = "网盘服务相关接口", tags = {"网盘服务相关接口"})
public class NetworkDiskRestApi {

    /**
     * 是否开启共享文件模式
     */
    public static Boolean isShareFile = false;
    public static long treeid = 0;
    @Autowired
    private NetworkDiskService networkDiskService;
    @Autowired
    private FeignUtil feignUtil;


    /**
     * 创建文件
     *
     * @return
     */
    @ApiOperation(value = "创建文件", notes = "创建文件")
    @RequestMapping(value = "/createFile", method = RequestMethod.POST)
    public String createFile(@RequestBody NetworkDisk networkDisk) {
        String adminUid = RequestHolder.checkLogin();
        networkDisk.setAdminUid(adminUid);
        networkDiskService.insertFile(networkDisk);
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    /**
     * 获取文件列表
     *
     * @param networkDisk
     * @return
     */
    @ApiOperation(value = "获取文件列表", notes = "获取文件列表")
    @RequestMapping(value = "/getFileList", method = RequestMethod.POST)
    public String getFileList(@RequestBody NetworkDisk networkDisk) {
        RequestHolder.checkLogin();
        networkDisk.setFilePath(PathUtil.urlDecode(networkDisk.getFilePath()));
        List<NetworkDisk> fileList = networkDiskService.selectFileList(networkDisk);
        return ResultUtil.successWithData(fileList);
    }

    /**
     * 重命名文件
     *
     * @return
     */
    @ApiOperation(value = "重命名文件", notes = "重命名文件")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@RequestBody NetworkDiskVO networkDiskVO) {
        RequestHolder.checkLogin();
        networkDiskService.updateFilepathByFilepath(networkDiskVO);
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    /**
     * 批量删除文件
     *
     * @return
     */
    @ApiOperation(value = "批量删除文件", notes = "批量删除文件")
    @RequestMapping(value = "/batchDeleteFile", method = RequestMethod.POST)
    public String batchDeleteFile(@RequestBody List<NetworkDiskVO> networkDiskVOList) {
        RequestHolder.checkLogin();
        Map<String, String> qiNiuConfig = feignUtil.getSystemConfigMap(RequestHolder.getAdminToken());
        for (NetworkDiskVO networkDiskVO : networkDiskVOList) {
            networkDiskService.deleteFile(networkDiskVO, qiNiuConfig);
        }
        return ResultUtil.successWithMessage(MessageConf.BATCH_DELETE_SUCCESS);
    }

    /**
     * 删除文件
     *
     * @return
     */
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public String deleteFile(@RequestBody NetworkDiskVO networkDiskVO) {
        RequestHolder.checkLogin();
        Map<String, String> qiNiuConfig = feignUtil.getSystemConfigMap(RequestHolder.getAdminToken());
        networkDiskService.deleteFile(networkDiskVO, qiNiuConfig);
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    /**
     * 解压文件
     *
     * @return
     */
    @ApiOperation(value = "解压文件", notes = "解压文件")
    @RequestMapping(value = "/unzipFile", method = RequestMethod.POST)
    public String unzipFile(@RequestBody NetworkDisk networkDisk) {
        RequestHolder.checkLogin();
        String zipFileUrl = PathUtil.getStaticPath() + networkDisk.getFileUrl();
        File file = FileOperation.newFile(zipFileUrl);
        String unzipUrl = file.getParent();
        List<String> fileEntryNameList = FileOperation.unzip(file, unzipUrl);
        List<NetworkDisk> fileBeanList = new ArrayList<>();
        for (int i = 0; i < fileEntryNameList.size(); i++) {
            String entryName = fileEntryNameList.get(i);
            String totalFileUrl = unzipUrl + entryName;
            File currentFile = FileOperation.newFile(totalFileUrl);
            NetworkDisk tempFileBean = new NetworkDisk();
            tempFileBean.setCreateTime(new Date());
            tempFileBean.setAdminUid(SysConf.DEFAULT_UID);
            tempFileBean.setFilePath(FileUtil.pathSplitFormat(networkDisk.getFilePath() + entryName.replace(currentFile.getName(), "")));
            if (currentFile.isDirectory()) {
                tempFileBean.setIsDir(1);
                tempFileBean.setFileName(currentFile.getName());
            } else {
                tempFileBean.setIsDir(0);
                tempFileBean.setExtendName(FileUtil.getFileType(totalFileUrl));
                tempFileBean.setFileName(FileUtil.getFileNameNotExtend(currentFile.getName()));
                tempFileBean.setFileSize(currentFile.length());
                tempFileBean.setFileUrl(File.separator + (currentFile.getPath()).replace(PathUtil.getStaticPath(), ""));
            }
            fileBeanList.add(tempFileBean);
        }
        networkDiskService.batchInsertFile(fileBeanList);
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 文件移动
     *
     * @return 返回前台移动结果
     */
    @ApiOperation(value = "文件移动", notes = "文件移动")
    @RequestMapping(value = "/moveFile", method = RequestMethod.POST)
    public String moveFile(@RequestBody NetworkDiskVO networkDiskVO) {
        RequestHolder.checkLogin();
        networkDiskService.updateFilepathByFilepath(networkDiskVO);
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 批量移动文件
     *
     * @return 返回前台移动结果
     */
    @ApiOperation(value = "批量移动文件", notes = "批量移动文件")
    @RequestMapping(value = "/batchMoveFile", method = RequestMethod.POST)
    public String batchMoveFile(@RequestBody NetworkDiskVO networkDiskVO) {
        RequestHolder.checkLogin();
        String files = networkDiskVO.getFiles();
        String newFilePath = networkDiskVO.getNewFilePath();
        List<NetworkDiskVO> fileList = JSON.parseArray(files, NetworkDiskVO.class);

        for (NetworkDiskVO file : fileList) {
            file.setNewFilePath(newFilePath);
            file.setOldFilePath(file.getFilePath());
            networkDiskService.updateFilepathByFilepath(file);
        }
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 通过文件类型查询文件
     *
     * @return
     */
    @ApiOperation(value = "通过文件类型查询文件", notes = "通过文件类型查询文件")
    @RequestMapping(value = "/selectFileByFileType", method = RequestMethod.GET)
    public String selectFileByFileType(NetworkDisk networkDisk) {
        List<NetworkDisk> networkDiskList = networkDiskService.selectFileByExtendName(FileUtil.getFileExtendsByType(networkDisk.getFileType()), SysConf.DEFAULT_UID);
        return ResultUtil.successWithData(networkDiskList);
    }

    /**
     * 获取文件树
     *
     * @return
     */
    @ApiOperation(value = "获取文件树", notes = "获取文件树")
    @RequestMapping(value = "/getFileTree", method = RequestMethod.POST)
    public String getFileTree() {
        List<NetworkDisk> filePathList = networkDiskService.selectFilePathTree();
        TreeNode resultTreeNode = new TreeNode();
        resultTreeNode.setNodeName("/");
        for (int i = 0; i < filePathList.size(); i++) {
            String filePath = filePathList.get(i).getFilePath() + filePathList.get(i).getFileName() + "/";
            Queue<String> queue = new LinkedList<>();
            String[] strArr = filePath.split("/");
            for (int j = 0; j < strArr.length; j++) {
                if (!"".equals(strArr[j]) && strArr[j] != null) {
                    queue.add(strArr[j]);
                }
            }
            if (queue.size() == 0) {
                continue;
            }
            resultTreeNode = insertTreeNode(resultTreeNode, "/", queue);
        }
        return ResultUtil.successWithData(resultTreeNode);
    }

    /**
     * 插入节点
     *
     * @param treeNode
     * @param filepath
     * @param nodeNameQueue
     * @return
     */
    public TreeNode insertTreeNode(TreeNode treeNode, String filepath, Queue<String> nodeNameQueue) {
        List<TreeNode> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null) {
            return treeNode;
        }
        Map<String, String> map = new HashMap<>();
        filepath = filepath + currentNodeName + "/";
        map.put("filepath", filepath);
        //1、判断有没有该子节点，如果没有则插入
        if (!isExistPath(childrenTreeNodes, currentNodeName)) {
            //插入
            TreeNode resultTreeNode = new TreeNode();
            resultTreeNode.setAttributes(map);
            resultTreeNode.setNodeName(nodeNameQueue.poll());
            resultTreeNode.setId(treeid++);
            childrenTreeNodes.add(resultTreeNode);
        } else {  //2、如果有，则跳过
            nodeNameQueue.poll();
        }
        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                TreeNode childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())) {
                    childrenTreeNode = insertTreeNode(childrenTreeNode, filepath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildNode(childrenTreeNodes);
                }
            }
        } else {
            treeNode.setChildNode(childrenTreeNodes);
        }
        return treeNode;
    }

    /**
     * 判断路径是否存在
     *
     * @param childrenTreeNodes
     * @param path
     * @return
     */
    public boolean isExistPath(List<TreeNode> childrenTreeNodes, String path) {
        boolean isExistPath = false;

        try {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                if (path.equals(childrenTreeNodes.get(i).getLabel())) {
                    isExistPath = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExistPath;
    }
}
