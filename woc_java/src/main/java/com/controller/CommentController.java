package com.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.*;
import com.enums.ExceptionEnum;
import com.service.impl.CommentServiceImpl;
import com.service.impl.CommentZanServiceImpl;
import com.service.impl.DynamicServiceImpl;
import com.service.impl.UsersServiceImpl;
import com.util.Code;
import com.util.MapUnite;
import com.util.Result;
import com.util.Util;
import com.vo.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author simon
 * @since 2021-06-21
 */
@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private DynamicServiceImpl dynamicService;
    @Autowired
    private CommentZanServiceImpl zanService;
    @Autowired
    private UsersServiceImpl usersService;

    /**
     * 添加和修改评论
     * @param request
     * @return
     */
    @RequestMapping("/setComment")
    public Object setComment(HttpServletRequest request){
        Comment data = (Comment) MapUnite.getEntity(Util.getJSONParam(request), Comment.class);
        data.setContent(Util.base64Plus(data.getContent()));
        if (data.getId().equals(Code.zero)) {
            Dynamic dynamic = dynamicService.getOne(new QueryWrapper<Dynamic>().lambda()
                    .eq(Dynamic::getId, data.getDynamicId()).select(Dynamic::getUserId));
            data.setDynamicUserId(dynamic.getUserId());
            data.setCreateTime(LocalDateTime.now());
            boolean b = commentService.save(data);
            if (b) {
                return Result.ok();
            }
            throw new MyException(ExceptionEnum.ADD_INFO);
        }
        boolean b = commentService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.UPDATE_INFO);
    }

    /**
     * 查询动态下的评论
     * @param dynamicId
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getComment")
    public Object getComment(Integer dynamicId, Integer page, Integer pagesize,Integer userId){
        Page<Comment> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Comment> queryWrapper = new QueryWrapper<Comment>().lambda();
        queryWrapper.eq(Comment::getDynamicId,dynamicId);
        queryWrapper.eq(Comment::getRid,Code.zero);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        IPage iPage = commentService.page(uPage, queryWrapper);
        List<Comment> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Comment data : dataList) {
            data.setContent(Util.base64Untie(data.getContent()));
            Map map = MapUnite.getMap(data);
            List<Comment> commentList = commentService.list(new QueryWrapper<Comment>().lambda()
                    .eq(Comment::getRid, data.getId()).orderByDesc(Comment::getCreateTime));
            List cList = new ArrayList();
            for (Comment comment : commentList) {
                comment.setContent(Util.base64Untie(comment.getContent()));
                Map cMap = MapUnite.getMap(comment);
                Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                        .eq(Users::getId, comment.getUserId())
                        .select(Users::getAvatar, Users::getNickname));
                cMap.put("users", users);
                if (!Code.zero.equals(comment.getBeUserId())){
                    Users beUsers = usersService.getOne(new QueryWrapper<Users>().lambda()
                            .eq(Users::getId, comment.getBeUserId())
                            .select(Users::getAvatar, Users::getNickname));
                    cMap.put("beUsers", beUsers);
                    Comment comment1 = commentService.getById(comment.getRid());
                    if(comment1 != null){
                        cMap.put("beTitle", Util.base64Untie(comment1.getContent()));
                    }else {
                        cMap.put("beTitle", "");
                    }
                }
                cList.add(cMap);
            }
            map.put("commentList",cList);
            map.put("commentCount",commentList.size());
            // 点赞数量
            int zanCount = zanService.count(new QueryWrapper<CommentZan>().lambda()
                    .eq(CommentZan::getCommenId, data.getId())
                    .eq(CommentZan::getState, Code.ONE)
                    .eq(CommentZan::getDynamicId, data.getDynamicId()));
            map.put("zanCount",zanCount);
            // 获取用户
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                    .eq(Users::getId, data.getUserId())
                    .select(Users::getAvatar, Users::getNickname));
            map.put("users", users);


            // 判断用户有没有点赞
            CommentZan isZan = zanService.getOne(new QueryWrapper<CommentZan>().lambda()
                    .eq(CommentZan::getUserId, userId)
                    .eq(CommentZan::getCommenId, data.getId())
                    .eq(CommentZan::getState, Code.ONE)
                    .eq(CommentZan::getDynamicId, data.getDynamicId()));
            if (isZan != null) {
                map.put("isZan", Code.one);
            } else {
                map.put("isZan", Code.zero);
            }

            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 查询动态下的评论
     * @param dynamicId
     * @param page
     * @param pagesize
     * @return
     */
    @RequestMapping("/getCommentAndAdmin")
    public Object getCommentAndAdmin(Integer dynamicId, Integer page, Integer pagesize){
        Page<Comment> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Comment> queryWrapper = new QueryWrapper<Comment>().lambda();
        queryWrapper.eq(Comment::getDynamicId,dynamicId);
        IPage iPage = commentService.page(uPage, queryWrapper);
        List<Comment> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Comment data : dataList) {
            data.setContent(Util.base64Untie(data.getContent()));
            Map map = MapUnite.getMap(data);
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                    .eq(Users::getId, data.getUserId())
                    .select(Users::getAvatar, Users::getNickname));
            map.put("users", users);
            if (!Code.zero.equals(data.getBeUserId())){
                Users beUsers = usersService.getOne(new QueryWrapper<Users>().lambda()
                        .eq(Users::getId, data.getBeUserId())
                        .select(Users::getAvatar, Users::getNickname));
                map.put("beUsers", beUsers);
                Comment comment1 = commentService.getById(data.getRid());
                if(comment1 != null){
                    map.put("beTitle", Util.base64Untie(comment1.getContent()));
                }else {
                    map.put("beTitle", "");
                }
            }
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @RequestMapping("/delCommentById")
    public Object delCommentById(@NotNull(message = "评论ID不能为空") Integer id){
        boolean b = commentService.removeById(id);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.DELETE_INFO);
    }

    /**
     * 点赞
     * @param request
     * @return
     */
    @RequestMapping("/setCommentZan")
    public Object setCommentZan(HttpServletRequest request){
        CommentZan data = (CommentZan) MapUnite.getEntity(Util.getJSONParam(request), CommentZan.class);
        CommentZan zan = zanService.getOne(new QueryWrapper<CommentZan>().lambda()
                .eq(CommentZan::getUserId, data.getUserId())
                .eq(CommentZan::getDynamicId,data.getDynamicId())
                .eq(CommentZan::getCommenId,data.getCommenId()));
        if (zan == null){
            data.setState(Code.ONE);
            data.setCreateTime(LocalDateTime.now());
            boolean b = zanService.save(data);
            if (b) {
               return Result.ok();
            }
            throw new MyException(ExceptionEnum.OPERATE_INFO);
        }
        data.setId(zan.getId());
        if (Code.ZERO.equals(zan.getState())){
            data.setState(Code.ONE);
        }else{
            data.setState(Code.ZERO);
        }
        boolean b = zanService.updateById(data);
        if (b) {
            return Result.ok();
        }
        throw new MyException(ExceptionEnum.OPERATE_INFO);
    }

    /**
     * 查看我收到的评论
     * @param userId
     * @param page
     * @param pagesize
     * @return
     * @throws ParseException
     */
    @RequestMapping("/getDynamicComment")
    public Object getDynamicConment(Integer userId,Integer page,Integer pagesize) throws ParseException {
        Page<Comment> uPage = new Page<>(page, pagesize);
        LambdaQueryWrapper<Comment> query = new QueryWrapper<Comment>().lambda();
        query.orderByDesc(Comment::getCreateTime);
        IPage iPage = commentService.page(uPage, query);
        List<Comment> dataList = iPage.getRecords();
        List<Object> list = new ArrayList<>();
        for (Comment data : dataList) {
            data.setContent(Util.base64Untie(data.getContent()));
            Map map = MapUnite.getObjMap(data);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            map.put("time",doTime(df.format(data.getCreateTime())));
            Dynamic dynamic = dynamicService.getOne(new QueryWrapper<Dynamic>().lambda()
                    .eq(Dynamic::getId, data.getDynamicId())
                    .select(Dynamic::getUserId,Dynamic::getImgs,Dynamic::getTitle));
            map.put("dynamic",MapUnite.getObjMap(dynamic));
            Users users = usersService.getOne(new QueryWrapper<Users>().lambda()
                    .eq(Users::getId, data.getUserId())
                    .select(Users::getAvatar, Users::getNickname));
            map.put("users",users);
            list.add(map);
        }
        return Result.ok(list, (int) iPage.getTotal());
    }

    /**
     *  判断几分钟前时间
     * @param data
     * @return
     * @throws ParseException
     */
    public String doTime(String data) throws ParseException {
        String createDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = format.parse(data);
        long differenceValue = new Date().getTime() - date.getTime();
        if (differenceValue <= 60000) {
            createDate = (differenceValue / 1000 / 60 / 60) + "秒前";
        } else if(60000 < differenceValue && differenceValue <= 3600000) {
            createDate = (differenceValue / 1000 / 60) + "分钟前";
        }  else {
            if (differenceValue < 86400000) {
                createDate = (differenceValue / 1000 / 60 / 60) + "小时前";
            } else {
                createDate = data;
            }
        }
        return createDate;
    }
}

