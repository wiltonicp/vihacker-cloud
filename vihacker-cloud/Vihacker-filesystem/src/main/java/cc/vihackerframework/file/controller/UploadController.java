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
     * 给定文件下的路径，将文件夹下的文件和文件夹落地到数据库
     *
     * @param file 给定的目录
     */
    @PostMapping("uploadFolder")
    @ApiOperation("上传文件夹")
    public ViHackerResult UploadFolder(@RequestParam("file") MultipartFile file, String filename, Long folderId) throws Exception {

        String name = file.getOriginalFilename();
        log.info(name + "-----name");
        log.info("file------>"+file);


        String[] dir = filename.split("/");

        log.info("znjx-file---->"+filename);
        System.out.println("znjx-file---->"+filename);
        //如果文件夹中有文件，则根据文件类型的索引(.)进行分析
//       if (filename.lastIndexOf(".") > 0) {

        log.info("dir---->"+dir.length);

        log.info("Arrays.toString(dir)---->"+Arrays.toString(dir));
        for (int i = 0; i < dir.length - 1; i++) {


            log.info("i---->" + i);
            log.info("文件夹----》"+dir[i]);
            folderId = ReadFolder(dir[i], folderId);

            log.info("dir.length - 2---->"+(dir.length - 2));
            if (i == dir.length - 2) {

                log.info("0000000*******");
                if (folderId != -1) {
                    uploadService.uploadFile(file, folderId);
                }
            }

        }
//       }
        if (folderId == -1L) {
            throw new Exception("目录不存在" + folderId);
        }
        return ViHackerResult.success("文件夹上传成功");
    }


    public Long ReadFolder(String dir, Long folderId) {
        //判断folder目录是否存在
        FolderEntity folderlist = this.folderService.getById(folderId);
        System.out.println("folderlist--->" + folderlist);
        if (folderlist == null) {
            return -1L;
        }


        //不包含dir，创建dir
        //如果dir不存在则创建目录

        return createDir(dir, folderId,folderlist);

    }

    public synchronized Long createDir(String dir, Long folderId,FolderEntity  folderlist) {

        //判断floderid目录下是否有dir文件或目录

        Long exist = isContian(dir,folderId);
        if (exist != -1L) {
            return exist;
        }
        System.out.println("------");
        FolderEntity folderEntity1 = new FolderEntity();
        SysUser sysUser = userFeign.getUserByUsername(SecurityUtil.getUsername());
        SysUser user = userFeign.getUserById(sysUser.getUserId());
        System.out.println(user);
        folderEntity1.setParentId(folderId);
        folderEntity1.setParentName(folderlist.getFolderName());
        folderEntity1.setCreatedBy(folderlist.getCreatedBy());
        folderEntity1.setModifyBy(folderlist.getModifyBy());
        folderEntity1.setCreatedByName(user.getUsername());
        folderEntity1.setType(0L);
        folderEntity1.setFolderName(dir);
        // folderEntity.created();
        //插入数据库
        System.out.println("folderEntity----》"+folderEntity1);

        log.info("准备写入");
        folderService.save(folderEntity1);
        log.info("写入成功");
        return folderEntity1.getId();
    }

    private long isContian(String dir, Long folderId) {
        List<FolderEntity> fileList = folderService.list(folderId);
        for (FolderEntity folderEntity : fileList) {

            log.info(dir+"文件对比--->"+folderEntity.getFolderName());

            //log.info(""+dir);
            log.info("folderEntity.getFolderName().equals(dir)--->"+folderEntity.getFolderName().equals(dir));
            if (folderEntity.getFolderName().equals(dir)) {
                log.info(System.currentTimeMillis()+"---->"+folderEntity.getId());
                log.info("ooooooo");
                log.info("folderEntity.getId()---->"+folderEntity.getId());
                return folderEntity.getId();
            }
        }
        return -1L;
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
