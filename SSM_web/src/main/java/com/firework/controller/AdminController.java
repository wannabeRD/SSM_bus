package com.firework.controller;

import com.firework.bean.Admin;
import com.firework.service.AdminService;
import com.firework.util.UploadFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器--管理员信息
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    //注入业务对象
    @Autowired
    private AdminService adminService;

    //存储预返回页面的结果对象
    private Map<String, Object> result = new HashMap<>();

    /**
     * 跳转到管理员信息管理页面
     */
    @GetMapping("/goAdminListView")
    public String getAdminList() {
        return "admin/adminList";
    }

    /**
     * 分页查询  根据管理员姓名获取指定/所有管理员信息列表
     * page 当前页码
     * rows 列表行数
     * username 管理员姓名
     */
    @PostMapping("/getAdminList")
    @ResponseBody
    public Map<String, Object> getAdminList(Integer page, Integer rows, String username) {

        //获取查询的用户名
        Admin admin = new Admin();
        admin.setName(username);
        //设置每页的记录数
        PageHelper.startPage(page, rows);
        //根据姓名获取指定或所有管理员列表信息
        List<Admin> list = adminService.selectList(admin);
        //封装查询结果
        PageInfo<Admin> pageInfo = new PageInfo<>(list);
        //获取总记录数
        long total = pageInfo.getTotal();
        //获取当前页数据列表
        List<Admin> adminList = pageInfo.getList();
        //存储对象数据
        result.put("total", total);
        result.put("rows", adminList);

        return result;
    }


    /**
     * 添加管理员信息
     */
    @PostMapping("/addAdmin")
    @ResponseBody
    public Map<String, Object> addAdmin(Admin admin) {
        //判断用户名是否已存在
        Admin user = adminService.findByName(admin.getName());
        if (user == null) {
            if (adminService.insert(admin) > 0) {
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("msg", "添加失败! 服务器端发生异常!");
            }
        } else {
            result.put("success", false);
            result.put("msg", "该用户名已存在! 请修改后重试!");
        }
        return result;
    }


    /**
     * 根据id修改指定管理员信息
     */
    @PostMapping("/editAdmin")
    @ResponseBody
    public Map<String, Object> edityAdmin(Admin admin) {
        //若修改的用户名已存在，则修改失败
        Admin user = adminService.findByName(admin.getName());
        if (user != null) {
            if (!(admin.getId().equals(user.getId()))) {
                result.put("success", false);
                result.put("msg", "该用户名已存在! 请修改后重试!");
                return result;
            }
        }
        //添加操作
        if (adminService.update(admin) > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
            result.put("msg", "添加失败! 服务器端发生异常!");
        }
        return result;
    }

    /**
     * 删除指定id的管理员信息
     * ids 拼接后的id
     */
    @PostMapping("/deleteAdmin")
    @ResponseBody
    public Map<String, Object> deleteAdmin(@RequestParam(value = "ids[]", required = true) Integer[] ids) {

        if (adminService.deleteById(ids) > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }


    /**
     * 上传头像
     */
    @PostMapping("/uploadPhoto")
    @ResponseBody
    public Map<String, Object> uploadPhoto(MultipartFile photo, HttpServletRequest request) {
        //存储头像的目标目录
        final String dirPath = request.getServletContext().getRealPath("/upload/admin_portrait");
        //存储头像的项目发布目录
        final String portraitPath = request.getServletContext().getContextPath() + "/upload/admin_portrait";
        //返回头像的上传结果
        return UploadFile.getUploadResult(photo, dirPath, portraitPath);
    }

}