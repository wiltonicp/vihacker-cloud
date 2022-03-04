package cc.vihackerframework.file.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author jiangshanchen
 * @title: IUploadFile
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/2上午9:24
 */
public interface IUploadFileService {
    /**
     * 检查分片
     * @param chunk 当前分片
     * @param chunkSize 分片大小
     * @param guid 当前文件的MD5值
     * @return
     */
    JSONObject checkMd5(String chunk, Integer chunkSize, String guid);

    /**
     * 上传分片
     * @param file 文件
     * @param folderId 文件夹 id
     * @param chunk 当前分片
     * @param guid 当前文件的MD5值
     */
    void uploadFile(MultipartFile file, Long folderId, Integer chunk, String guid)throws IOException;

    /**
     * 合并分片
     * @param guid 当前文件的MD5值
     * @param fileName 文件名称
     */
    void combineBlock(String guid, String fileName);
}
