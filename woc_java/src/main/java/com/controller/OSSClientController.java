package com.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.enums.ExceptionEnum;
import com.util.Result;
import com.vo.MyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/***
 * @Description: //TODO oss文件上传类
 * @author: songxinyong
 * @date: 2020/11/5 09:54
 */
@RestController
@CrossOrigin
@RequestMapping("/oss")
public class OSSClientController {
  //阿里云OSS地址，这里看根据你的oss选择
  protected static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
  //阿里云OSS账号
  protected static String accessKeyId = "LTAI4G6HpGTMTHurKtvLWGBS";
  //阿里云OSS密钥
  protected static String accessKeySecret = "ltto6VeJhZyRbemGssKZUnVD23hZaQ";
  //阿里云OSS上的存储块bucket名字
  protected static String bucketName = "ziyue-baron";
  //阿里云图片文件存储目录
  protected static String homeimagedir = "images/";

//  //阿里云OSS地址，这里看根据你的oss选择
//  protected static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
//  //阿里云OSS账号
//  protected static String accessKeyId = "LTAI4G3Zn8tiT527gSS2Ya55";
//  //阿里云OSS密钥
//  protected static String accessKeySecret = "4lWo8ULfgwRkWpM2x43hrgBt0QXcRH";
//  //阿里云OSS上的存储块bucket名字
//  protected static String bucketName = "simon-img-upload";
//  //阿里云图片文件存储目录
//  protected static String homeimagedir = "images/";
//  protected static String homeMap4dir = "map4/";

  private OSSClient ossClient;

  public OSSClientController() {
    ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
  }

  /**
   * 初始化
   */
  public void init() {
    ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
  }

  /**
   * 销毁
   */
  public void destory() {
    ossClient.shutdown();
  }

  /**
   * 图片 上传阿里云oss
   *
   * @param file
   * @return
   */
  public String uploadHomeImageOSS(MultipartFile file) throws Exception {
    if (file.getSize() > 1024 * 1024 * 50) {
      throw new MyException(ExceptionEnum.FILE_MAX_50);
    }
    String originalFilename = file.getOriginalFilename();
    String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
    Random random = new Random();
    String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
    try {
      InputStream inputStream = file.getInputStream();
      this.uploadHomeImageFileOSS(inputStream, name);
      return name;
    } catch (Exception e) {
      throw new MyException(ExceptionEnum.FILE_ERROR);
    }
  }

  /**
   * 获得图片路径
   *
   * @param fileUrl
   * @return
   */
  public String getHomeImageUrl(String fileUrl) {
    if (!StringUtils.isEmpty(fileUrl)) {
      String[] split = fileUrl.split("/");
      return this.getUrl(getFilename(fileUrl.substring(fileUrl.lastIndexOf("."))) + split[split.length - 1]);
    }
    return null;
  }

  /**
   * 图片上传到OSS服务器  如果同名文件会覆盖服务器上的
   *
   * @param instream 文件流
   * @param fileName 文件名称 包括后缀名
   * @return 出错返回"" ,唯一MD5数字签名
   */
  public String uploadHomeImageFileOSS(InputStream instream, String fileName) throws Exception {
    String ret;
    try {
      //创建上传Object的Metadata
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(instream.available());
      objectMetadata.setCacheControl("no-cache");
      objectMetadata.setHeader("Pragma", "no-cache");
      objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
      objectMetadata.setContentDisposition("inline;filename=" + fileName);
      //上传文件
      PutObjectResult putResult = ossClient.putObject(bucketName, getFilename(fileName.substring(fileName.lastIndexOf("."))) + fileName, instream, objectMetadata);
      ret = putResult.getETag();
    } catch (IOException e) {
      throw new Exception(e.getMessage());
    } finally {
      try {
        if (instream != null) {
          instream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return ret;
  }

  /**
   * 判断OSS服务文件上传时文件的类型contentType
   *
   * @param FilenameExtension 文件后缀
   * @return String
   */
  public static String getcontentType(String FilenameExtension) {
    if (FilenameExtension.equalsIgnoreCase(".bmp")) {
      return "image/bmp";
    }
    if (FilenameExtension.equalsIgnoreCase(".gif")) {
      return "image/gif";
    }
    if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
            FilenameExtension.equalsIgnoreCase(".jpg") ||
            FilenameExtension.equalsIgnoreCase(".png")) {
      return "image/jpeg";
    }
    if (FilenameExtension.equalsIgnoreCase(".html")) {
      return "text/html";
    }
    if (FilenameExtension.equalsIgnoreCase(".txt")) {
      return "text/plain";
    }
    if (FilenameExtension.equalsIgnoreCase(".vsd")) {
      return "application/vnd.visio";
    }
    if (FilenameExtension.equalsIgnoreCase(".pptx") ||
            FilenameExtension.equalsIgnoreCase(".ppt")) {
      return "application/vnd.ms-powerpoint";
    }
    if (FilenameExtension.equalsIgnoreCase(".docx") ||
            FilenameExtension.equalsIgnoreCase(".doc")) {
      return "application/msword";
    }
    if (FilenameExtension.equalsIgnoreCase(".xml")) {
      return "text/xml";
    }
    if (FilenameExtension.equalsIgnoreCase(".mp4")) {
      return "video/mpeg4";
    }
    if (FilenameExtension.equalsIgnoreCase(".avi")) {
      return "video/avi";
    }
    return "text/plain";
  }

  public static String getFilename(String FilenameExtension) {
    if (FilenameExtension.equalsIgnoreCase(".bmp")) {
      return "images/";
    }
    if (FilenameExtension.equalsIgnoreCase(".gif")) {
      return "images/";
    }
    if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
            FilenameExtension.equalsIgnoreCase(".jpg") ||
            FilenameExtension.equalsIgnoreCase(".png")) {
      return "images/";
    }
    if (FilenameExtension.equalsIgnoreCase(".html")) {
      return "text/";
    }
    if (FilenameExtension.equalsIgnoreCase(".txt")) {
      return "text/";
    }
    if (FilenameExtension.equalsIgnoreCase(".xml")) {
      return "text/";
    }
    if (FilenameExtension.equalsIgnoreCase(".mp4")) {
      return "video/";
    }
    if (FilenameExtension.equalsIgnoreCase(".avi")) {
      return "video/";
    }
    return "text/";
  }

  /**
   * 获得url链接
   *
   * @param key
   * @return
   */
  public String getUrl(String key) {
    // 设置URL过期时间为10年  3600l* 1000*24*365*10
    Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
    // 生成URL
    URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
    if (url != null) {
      return url.toString();
    }
    return null;
  }

  /**
   * 判断图片
   *
   * @param file
   * @return
   * @throws Exception
   */
  public String updateHomeImage(MultipartFile file) throws Exception {
    OSSClientController ossClient = new OSSClientController();
    if (file == null || file.getSize() <= 0) {
      throw new MyException(ExceptionEnum.FILE_NULL);
    }
    String name = ossClient.uploadHomeImageOSS(file);
    String imgUrl = ossClient.getHomeImageUrl(name);
    String[] imgUrls = imgUrl.split("\\?");
    return imgUrls[0];
  }

  /***
   * @Description: //TODO 处理文件上传
   * @param: [file]
   * @author: songxinyong
   * @date: 2020/11/5 09:54
   * @return: java.lang.Object
   */
  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public Object homeImageUpload(MultipartFile file) {
    try {
      String url = updateHomeImage(file);//此处是调用上传服务接口
      return Result.ok(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new MyException(ExceptionEnum.OPERATE_INFO);
  }
}