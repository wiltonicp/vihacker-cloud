package cc.vihackerframework.file.service.impl;

import cc.vihackerframework.file.constant.FileConstant;
import cc.vihackerframework.file.entity.FileEntity;
import cc.vihackerframework.file.properties.ViHackerFileProperties;
import cc.vihackerframework.file.service.IFileService;
import cc.vihackerframework.file.service.IUploadFileService;


import cc.vihackerframework.file.starter.util.FileUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author jiangshanchen
 * @title: UploadServiceImpl
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/2上午9:26
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements IUploadFileService {

    private final IFileService fileService;
    private final ViHackerFileProperties properties;

    @Override
    public JSONObject checkMd5(String chunk, Integer chunkSize, String guid) {
        JSONObject jsonObject = new JSONObject();
        /**
         * 分片上传路径
         */
        String tempPath = properties.path + File.separator + FileConstant.TEMP_PATH;
        File checkFile = new File(tempPath + File.separator + guid + File.separator + chunk);
        /**
         * 如果当前分片存在，并且长度等于上传的大小
         */
        if (checkFile.exists() && checkFile.length() == chunkSize) {
            jsonObject.put("ifExist",1);
        } else {
            jsonObject.put("ifExist",0);
        }
        return jsonObject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadFile(MultipartFile file, Long folderId, Integer chunk, String guid) throws IOException {
        String filePath = properties.getUserPath() + FileConstant.TEMP_PATH + File.separator + guid;
        File tempPath = new File(filePath);
        if (!tempPath.exists()) {
            tempPath.mkdirs();
        }
        RandomAccessFile raFile = null;
        BufferedInputStream inputStream = null;
        if (chunk == null) {
            chunk = 0;
        }

        System.out.println("filePath---->"+filePath);
        File dirFile = new File(filePath, String.valueOf(chunk));
        //以读写的方式打开目标文件
        raFile = new RandomAccessFile(dirFile, "rw");
        raFile.seek(raFile.length());
        inputStream = new BufferedInputStream(file.getInputStream());
        byte[] buf = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buf)) != -1) {
            raFile.write(buf, 0, length);
        }
        /**
         * 如果下标为 0，初始化到数据库
         */
        if(chunk == 0){
            FileEntity fileByMd5 = this.fileService.getByFileMd5(guid);
            String fileName = file.getOriginalFilename();
            if(fileByMd5 == null){
                fileByMd5 = new FileEntity();
                fileByMd5.setFolderId(folderId);
                fileByMd5.setFileName(fileName);
                fileByMd5.setFileType(FileUtil.getFileType(FileUtil.getExtensionName(fileName)));
                fileByMd5.setIco(FileUtil.getExtensionName(fileName));
                fileByMd5.setFileSize(file.getSize());
                fileByMd5.setStoreName(guid + fileName);
                fileByMd5.setFileMd5(guid);
                fileByMd5.setOpen(true);
                this.fileService.save(fileByMd5);
            }
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (raFile != null) {
            raFile.close();
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void combineBlock(String guid, String fileName) {
        //分片文件临时目录
        File tempPath = new File(properties.getUserPath()
                + FileConstant.TEMP_PATH + File.separator + guid);
        String realFilePath = properties.getUserPath()
                + FileConstant.REAL_PATH + File.separator + guid + fileName;
        File realFile = new File(realFilePath);
        /**
         * 文件追加写入
         */
        FileOutputStream os = null;
        FileChannel fcin = null;
        FileChannel fcout = null;
        try {
            log.info("合并文件——开始 [ 文件名称：" + fileName + " ，MD5值：" + guid + " ]");
            os = new FileOutputStream(realFile, true);
            fcout = os.getChannel();
            if (tempPath.exists()) {
                //获取临时目录下的所有文件
                File[] tempFiles = tempPath.listFiles();
                //按名称排序
                Arrays.sort(tempFiles, (o1, o2) -> {
                    if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                        return -1;
                    }
                    if (Integer.parseInt(o1.getName()) == Integer.parseInt(o2.getName())) {
                        return 0;
                    }
                    return 1;
                });
                ByteBuffer buffer = ByteBuffer.allocate(10 * 1024 * 1024);
                for (int i = 0; i < tempFiles.length; i++) {
                    FileInputStream fis = new FileInputStream(tempFiles[i]);
                    fcin = fis.getChannel();
                    if (fcin.read(buffer) != -1) {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            fcout.write(buffer);
                        }
                    }
                    buffer.clear();
                    fis.close();
                    //删除分片
                    tempFiles[i].delete();
                }
                os.close();
                //删除临时目录
                if (tempPath.isDirectory() && tempPath.exists()) {
                    System.gc(); // 回收资源
                    tempPath.delete();
                }
                /**
                 * 更新文件大小
                 */
                FileEntity entity = this.fileService.getByFileMd5(guid);
                entity.setFileSize(realFile.length());
                this.fileService.updateById(entity);
                log.info("文件合并——结束 [ 文件名称：" + fileName + " ，MD5值：" + guid + " ]");
            }
        } catch (Exception e) {
            log.error("文件合并——失败 " + e.getMessage());
        }
    }
}
