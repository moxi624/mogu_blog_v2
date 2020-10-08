package com.moxi.mogublog.picture.restapi;

import com.alibaba.fastjson.JSON;
import com.moxi.mogublog.picture.entity.NetworkDisk;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/createFile", method = RequestMethod.POST)
    @ResponseBody
    public String createFile(@RequestBody NetworkDisk networkDisk) {
        String adminUid = RequestHolder.checkLogin();
        networkDisk.setAdminUid(adminUid);
        networkDiskService.insertFile(networkDisk);
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    /**
     * 获取文件列表
     *
     * @param request
     * @param networkDisk
     * @return
     */
    @RequestMapping(value = "/getFileList", method = RequestMethod.POST)
    @ResponseBody
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
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
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
    @RequestMapping(value = "/batchDeleteFile", method = RequestMethod.POST)
    @ResponseBody
    public String batchDeleteFile(@RequestBody List<NetworkDiskVO> networkDiskVOList) {
        RequestHolder.checkLogin();
        Map<String, String> qiNiuConfig = feignUtil.getQiNiuConfig(RequestHolder.getAdminToken());
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
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    @ResponseBody
    public String deleteFile(@RequestBody NetworkDiskVO networkDiskVO) {
        RequestHolder.checkLogin();
        Map<String, String> qiNiuConfig = feignUtil.getQiNiuConfig(RequestHolder.getAdminToken());
        networkDiskService.deleteFile(networkDiskVO, qiNiuConfig);
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    /**
     * 解压文件
     *
     * @return
     */
    @RequestMapping(value = "/unzipFile", method = RequestMethod.POST)
    @ResponseBody
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
    @RequestMapping(value = "/moveFile", method = RequestMethod.POST)
    @ResponseBody
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
    @RequestMapping(value = "/batchMoveFile", method = RequestMethod.POST)
    @ResponseBody
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
     * 通过文件类型选择文件
     *
     * @return
     */
    @RequestMapping(value = "/selectFileByFileType", method = RequestMethod.GET)
    @ResponseBody
    public String selectFileByFileType(NetworkDisk networkDisk) {
        List<NetworkDisk> networkDiskList = networkDiskService.selectFileByExtendName(FileUtil.getFileExtendsByType(networkDisk.getFileType()), SysConf.DEFAULT_UID);
        return ResultUtil.successWithData(networkDiskList);
    }

    /**
     * 获取文件树
     *
     * @return
     */
    @RequestMapping(value = "/getFileTree", method = RequestMethod.POST)
    @ResponseBody
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
