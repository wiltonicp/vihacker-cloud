package cc.vihackerframework.file.controller;

import cc.vihackerframework.core.api.ViHackerResult;

import cc.vihackerframework.file.constant.FileConstant;
import cc.vihackerframework.file.properties.ViHackerFileProperties;
import cc.vihackerframework.file.service.IUploadFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author jiangshanchen
 * @title: UploadController
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/2上午9:52
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {





    private final IUploadFileService uploadService;
    private final ViHackerFileProperties properties;

    /**
     * 查看当前分片是否上传
     * @param request
     */
    @PostMapping("checkblock")
    public ViHackerResult checkMd5(HttpServletRequest request) {
        String chunk = request.getParameter("chunk");
        String chunkSize = request.getParameter("chunkSize");
        String guid = request.getParameter("guid");
        return ViHackerResult.data(uploadService.checkMd5(chunk, Integer.parseInt(chunkSize), guid));
    }

    /**
     * 上传分片
     * @param file 文件
     * @param folderId 文件夹id
     * @param chunk 分片下标
     * @param guid 上传文件的MD5值
     * @return
     * @throws IOException
     */
    @PostMapping("save")
    //@PreAuthorize("hasAuthority('view2')")
    public ViHackerResult<Void> upload(@RequestParam MultipartFile file, Long folderId, Integer chunk, String guid) throws IOException {
        this.uploadService.uploadFile(file,folderId,chunk,guid);
        return ViHackerResult.success();
    }

    /**
     * 合并文件
     * @param guid 上传文件的MD5值
     * @param fileName 文件名称
     */
    @PostMapping("combine")
    public ViHackerResult<Void> combineBlock(String guid, String fileName) {
        this.uploadService.combineBlock(guid,fileName);
        return ViHackerResult.success();
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param response HttpServletResponse
     */
    @GetMapping("downloadFile")
    public void downLoadFile(String fileName, HttpServletResponse response) {
        File file = new File(properties.path + File.separator + FileConstant.REAL_PATH + File.separator + fileName);
        if (file.exists()) {
            InputStream is = null;
            OutputStream os = null;
            try {
                response.reset();
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                //设置下载文件名
                response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
                response.addHeader("Content-Length", "" + file.length());
                //定义输入输出流
                os = new BufferedOutputStream(response.getOutputStream());
                is = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    os.write(buffer, 0, len);
                    os.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("文件下载成功——文件名：" + fileName);
            }
        }
    }
}
