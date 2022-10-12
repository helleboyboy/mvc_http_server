package com.example.mvc_http_server.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class MyController {
    /**
     * 增加slf4j日志功能
     */
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    /**
     * 读取properties配置文件的属性值！
     */
    @Value("${file.fileLocation}")
    private String filePathToPro;

    @Value("${file.suffix}")
    private String fileSuffix;

    /**
     * GET请求 http://localhost:9898/get_info
     * 返回 json字符串
     * @return "{\"a\", \"b\"}"
     */
    @GetMapping(value = "/get_info")
    public String getInfo(){
        String res = String.format("getInfo method executed !%n");
        logger.info(res);
        return "{\"a\", \"b\"}";
    }

    /**
     * GET请求 http://localhost:9898/hello
     * 返回普通字符串的请求方法
     * @return "Hello Spring Boot!"
     */
    @RequestMapping("/hello")
    public String hello() {
        String res = String.format("hello method executed !%n");
        logger.info(res);
        return "Hello WELCOME !";
    }


    /**
     * GET请求 http://localhost:9898/testTwo?param1=222
     * @param param1 第一个参数
     * @param param2 第二个参数
     * @return get in handle1 param1 is 222, param2 is null
     */
    @GetMapping("/testTwo")
    public String test(String param1, String param2) {
        String result = String.format("get in handle1 param1 is %s, param2 is %s", param1, param2);
        logger.info(result);
        return result;
    }


    /**
     * POST请求 http://localhost:9898/testClass?param1=222&param2=111
     * @param demo 参数封装类
     * @return post in handle1 param1 is 222, param2 is 111
     */
    @PostMapping("/testClass")
    public String test(ParamDemo demo){
        String result = String.format("post in handle2 param1 is %s, param2 is %s",
                demo.param1, demo.param2);
        logger.info(result);
        return result;
    }


    /**
     * 请求入参是一个实体,并且加上了 @RequestBody
     * 一般适用于前端Header中Content-Type 为 application/json的场景
     * 注意入参要是json格式
     * curl -X POST
     *      --header 'Content-Type: application/json'
     *      --header 'Accept: application/json'
     *      -d '{"param1": "javaAndBigdata","param2": "thinker"}'
     *      'http://10.9.16.21:9898/testPostJson' -v
     *
     * @param demo 请求体
     * @param headers 请求头
     * @return json串
     */
    @PostMapping("/testPostJson")
    public String testJson(@RequestBody ParamDemo demo, @RequestHeader Map<String, String> headers){
        headers.forEach(
                (key,value) -> logger.info(String.format("Headers key: '%s', value: '%s'",key ,value)));
        String result = String.format("in handle2 param1 is %s, param2 is %s", demo.param1, demo.param2);
        logger.info(result);
        String res = String.format("请求头数据：%s", demo.toString());
        logger.info(res);
        return result;
    }


    /**
     * curl -X GET 'http://10.9.16.21:9898/testGetJson'
     * @return {
     * errorMeg: "成功",
     * errorCode: 0
     * }
     */
    @GetMapping("/testGetJson")
    public Map<String,Object> testGetJson(){
        Map<String,Object> result = new HashMap<>();
        result.put("errorCode",0);
        result.put("errorMeg","成功");
        logger.info("testGetJson method executing !!!");
        return result;
    }



    /**
     * 请求入参是一个实体,并且加上了 @RequestBody
     * 一般适用于前端Header中Content-Type 为 application/json的场景
     * 注意入参要是json格式
     * curl -X POST --header 'Content-Type: application/json'
     *      --header 'Accept: application/json'
     *      -d '{"param1": "javaAndBigdata","param2": "thinker"}'
     *      'http://10.9.16.21:9898/testReturnJson'
     * @param demo 请求体
     * @return json
     */
    @PostMapping("/testReturnJson")
    public String testReturnJson(@RequestBody ParamDemo demo){
        // 将获取的json数据封装一层，然后在给返回
        JSONObject result = new JSONObject();
        result.put("msg", "ok");
        result.put("method", "POST");
        result.put("data", demo);
        logger.info("testReturnJson method executing !!!");
        return result.toJSONString();
    }

    /**
     *  将字符串转换为文件
     * @param msg 转存内容
     * @param filePath 文件存储路径
     */
    @SneakyThrows
    private void strToFile(String msg, String filePath){
        try (
                BufferedWriter bos = new BufferedWriter(
                        new FileWriter(
                                new File(filePath)))
        ) {
            String format = String.format("开始往 %s 文件写入", filePath);
            String res = String.format("%s文件写入成功", filePath);
            logger.info(format);
            bos.write(msg);
            logger.info(res);
        }
    }


    /**
     *  MultipartFile 文件转存到本地
     * @param file 上传的文件
     * @param filePath 文件存储路径
     */
    @SneakyThrows
    private  void save2Local(MultipartFile file, String filePath){
        String fileName = file.getOriginalFilename();
        String fileNameRes = String.format("上传的文件名为：%s", fileName);
        logger.info(fileNameRes);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fileSuffix);
        String format = simpleDateFormat.format(date);
        String dstFile = filePath + fileName + format;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        try (FileOutputStream fileOutputStream = new FileOutputStream(dstFile)) {
            is = file.getInputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(fileOutputStream);
            int count = 0;
            byte[] bytes = new byte[1024 * 8];
            String fileIn = String.format("开始写入 %s 文件", dstFile);
            logger.info(fileIn);
            while ((count = bis.read(bytes)) != -1){
                bos.write(bytes, 0, count);
            }
            String fileOut = String.format("写入 %s 文件成功", dstFile);
            logger.info(fileOut);
        }catch (IOException e){
            String err = String.format("写入%s文件出错", dstFile);
            logger.error(err);
            e.printStackTrace();
        }
//        finally {
//            if (bos != null) {
//                bos.close();
//            }
//            if (bis != null) {
//                bis.close();
//            }
//        }
    }


    /**
     * 文件上传
     *  curl 'http://127.0.0.1:8080/post/file' -X POST -F 'file=@hello.txt'
     *  通过@Value注解来获取配置文件的内容！
     */
    @PostMapping("/fileUpload1")
    public String fileUpload1(@RequestParam("file")MultipartFile file){
//        String filePathToPro = "/home/lgh/curl_data/res/";
//        String filePathToPro = "D:\\test\\res\\";
        save2Local(file, filePathToPro);
        JSONObject res = new JSONObject();
        res.put("msg", "ok");
        res.put("method", "POST");
        res.put("file", file.getOriginalFilename());
        return res.toJSONString();
    }


    /**
     *  较于fileUpload1，速度快
     *  curl 'http://10.9.16.21:9898/fileUpload3' -X POST -F 'file=@a.log'
     * @param file 上传的文件
     * @return res
     */
    @PostMapping("/fileUpload3")
    public String fileUpload3(@RequestParam("file")MultipartFile file){
//        String filePathToPro = "D:\\test\\res\\";
//        String filePathToPro = "/home/lgh/curl_data/res/";
        String fileName = file.getOriginalFilename();
        String fileNameRes = String.format("上传的文件名为：%s", fileName);
        logger.info(fileNameRes);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fileSuffix);
        String format = simpleDateFormat.format(date);
        String dstFile = filePathToPro + fileName + format;
        String out = String.format("文件 %s 传输成功！！！", dstFile);
        String err = String.format("文件 %s 传输失败！！！", dstFile);
        String sta = String.format("文件 %s 开始传输！！！", dstFile);
        try {
            File dst = new File(dstFile);
            logger.info(sta);
            file.transferTo(dst);
            logger.info(out);
        } catch (IOException e) {
            logger.error(err);
            e.printStackTrace();
        }
        JSONObject res = new JSONObject();
        res.put("msg", "ok");
        res.put("method", "POST");
        res.put("file", file.getOriginalFilename());
        return res.toJSONString();
    }

    /**
     * 使用静态内部类,简单定义一个入参实体类
     */
    static class ParamDemo{
        private String param1;
        private String param2;

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }

        @Override
        public String toString() {
            return "ParamDemo{" +
                    "param1='" + param1 + '\'' +
                    ", param2='" + param2 + '\'' +
                    '}';
        }
    }
}
