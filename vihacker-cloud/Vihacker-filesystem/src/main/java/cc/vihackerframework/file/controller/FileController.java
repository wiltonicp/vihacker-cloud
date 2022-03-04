package cc.vihackerframework.file.controller;

import cc.vihackerframework.core.api.ViHackerResult;

import cc.vihackerframework.file.entity.FileEntity;
import cc.vihackerframework.file.entity.FolderEntity;
import cc.vihackerframework.file.properties.ViHackerFileProperties;
import cc.vihackerframework.file.service.IFileService;
import cc.vihackerframework.file.service.IFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangshanchen
 * @title: FileController
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/2/28下午4:41
 */
@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {


    private final IFileService fileService;
    private final ViHackerFileProperties properties;
    private final IFolderService folderService;

    /**
     * 根据文件夹id查询 文件和文件夹
     *
     * @param folderId
     * @return
     */
    @GetMapping("list")
    public ViHackerResult listPage(Long folderId){
        Map<String,List>fileMap=new HashMap<>();

        List<FileEntity> fileList = fileService.listPage(folderId);
        List<FolderEntity> folderList = folderService.list(folderId);
        fileMap.put("fileList",fileList);
        fileMap.put("folderList",folderList);
        return   ViHackerResult.data(fileMap);
    }


    @GetMapping("listToTable")
    public String list(Model model,Long folderId){
        List<FileEntity> fileList = fileService.listPage(folderId);
        List<FolderEntity> folderList = folderService.list(folderId);
        model.addAttribute("fileList",fileList);
        model.addAttribute("folderList",folderList);
        return "page-files::gridList";
    }

    @PostMapping("/update")
    public@ResponseBody
    ViHackerResult<Void> update(FileEntity fileEntity){
        FileEntity entity = fileService.getById(fileEntity.getId());
        BeanUtils.copyProperties(fileEntity,entity);
        entity.update();
        fileService.updateById(entity);
        return ViHackerResult.success();
    }

    /**
     * 删除文件
     * @param fileId
     * @return
     */
    @PostMapping("/delete")
    public@ResponseBody
    ViHackerResult<Void> delete(long fileId){
        boolean b = fileService.removeById(fileId);
        return ViHackerResult.success();
    }


}
