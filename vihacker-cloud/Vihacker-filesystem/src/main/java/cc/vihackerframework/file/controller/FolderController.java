package cc.vihackerframework.file.controller;

import cc.vihackerframework.core.api.ViHackerResult;

import cc.vihackerframework.file.entity.FolderEntity;
import cc.vihackerframework.file.service.IFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiangshanchen
 * @title: FolderController
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午4:03
 */


@RestController
@RequestMapping("/folder")
@RequiredArgsConstructor
public class FolderController {


    private final IFolderService folderService;

    /**
     * 根据 id 查询所有父节点
     * @param
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ViHackerResult listByParentId(@PathVariable Long id){
        List<FolderEntity> folderList = folderService.findParentById(id);
//        model.addAttribute("breadcrumb",folderList);
        return ViHackerResult.data(folderList);
    }

    /**
     * 新建文件夹
     * @param folderEntity
     * @return
     */
    @PostMapping("/add")
    public@ResponseBody
    ViHackerResult<FolderEntity> add(@Validated FolderEntity folderEntity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("authentication----》"+authentication.getPrincipal());

        System.out.println("authentication----》"+authentication.getName());

        System.out.println("authentication----》"+authentication.getDetails());

        System.out.println("authentication----》"+authentication.getAuthorities());
        folderEntity.created();
        boolean save = folderService.save(folderEntity);
        return ViHackerResult.data(folderEntity);
    }

    /**
     * 修改文件夹名称
     * @param folderEntity
     * @return
     */
    @PostMapping("/update")
    public@ResponseBody
    ViHackerResult<Void> update(FolderEntity folderEntity){
        FolderEntity entity = folderService.getById(folderEntity.getId());
        BeanUtils.copyProperties(folderEntity,entity);
        entity.update();
        folderService.updateById(entity);
        return ViHackerResult.success();
    }

    /**
     * 删除文件夹
     * @param folderId
     * @return
     */
    @PostMapping("/delete")
    public@ResponseBody
    ViHackerResult<Void> delete(long folderId){
        folderService.removeById(folderId);
        return ViHackerResult.success();
    }
}
