package com.example.mvc_http_server.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@RestController
public class MyController {

    @RequestMapping(value = "/get_info", method = RequestMethod.GET)
    public String getInfos(){
        return "{\"a\", \"b\"}";
    }

    /**
     * ===========================================================================================================
     * ===========================================================================================================
     * ===========================================================================================================
     * ===========================================================================================================
     * ===========================================================================================================
     * ===========================================================================================================
     * @return
     */
    // url请求，支持GET和POST，返回值固定
    @RequestMapping("/hello")
    public String hello() {
        System.out.println("hello method executing !!!");
        return "Hello Spring Boot!";
    }


    /**
     * GET请求 http://127.0.0.1:8080/test?param1=222
     * @param param1
     * @param param2
     * @return get in handle1 param1 is 222, param2 is null
     */
    @GetMapping("/test")
    public String test(String param1, String param2) {
        String result = String.format("get in handle1 param1 is %s, param2 is %s", param1, param2);
        System.out.println("test method executing !!!");
        return result;
    }


    /**
     * POST请求 http://127.0.0.1:8080/test?param1=222&param2=111
     * @param demo
     * @return post in handle1 param1 is 222, param2 is 111
     */
    @PostMapping("/test")
    public String test(ParamDemo demo){
        String result = String.format("post in handle2 param1 is %s, param2 is %s", demo.param1, demo.param2);
        System.out.println("test method executing !!!");
        return result;
    }


    /**
     * 请求入参是一个实体,并且加上了 @RequestBody
     * 一般适用于前端Header中Content-Type 为 application/json的场景
     * 注意入参要是json格式
     * curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"param1": "javaAndBigdata","param2": "thinker"}' 'http://10.9.16.17:8080/testPostJson' -v
     * @param demo
     * @return
     */
    @PostMapping("/testPostJson")
    public String testJson(@RequestBody ParamDemo demo, @RequestHeader Map<String, String> headers){
        System.out.println("====");
        headers.forEach(
                (key,value) ->{
                    System.out.println(String.format("Headers key: '%s', value: '%s'",key ,value));
                });
        System.out.println("=====");
        String result = String.format("in handle2 param1 is %s, param2 is %s", demo.param1, demo.param2);
        System.out.println("testJson method executing !!!");
        System.out.println("post data are: " + result);
        System.out.println(String.format("请求头数据：%s", demo.toString()));
        return result;
    }


    /**
     * GET 请求http://127.0.0.1:8080/testGetJson
     * @return {
     * errorMeg: "成功",
     * errorCode: 0
     * }
     */
    @GetMapping("/testGetJson")
    public Map<String,Object> testGetJson(){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("errorCode",0);
        result.put("errorMeg","成功");
        System.out.println("testGetJson method executing !!!");
        return result;

    }



    /**
     * 请求入参是一个实体,并且加上了 @RequestBody
     * 一般适用于前端Header中Content-Type 为 application/json的场景
     * 注意入参要是json格式
     * @param demo
     * @return json
     */
    @PostMapping("/testReturnJson")
    public String testReturnJson(@RequestBody ParamDemo demo){
        // 将获取的json数据封装一层，然后在给返回
        JSONObject result = new JSONObject();
        result.put("msg", "ok");
        result.put("method", "POST");
        result.put("data", demo);
        System.out.println("testReturnJson method executing !!!");
        return result.toJSONString();
    }


    private void strToFile(String msg, String filePath){
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(new File(filePath)));
            bos.write(msg);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void multipartFileToLocalFile(MultipartFile file, String filePath){
        String fileName = file.getOriginalFilename();
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            InputStream is = file.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            int read = bis.read();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void save2Local(MultipartFile file, String filePath){
//        String filePath = "";
        String fileName = file.getOriginalFilename();
        String ans = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        try{
            is = file.getInputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(new File(filePath + fileName)));
            int count = 0;
            byte[] bytes = new byte[1024];
            while ((count = bis.read(bytes)) != -1){
                bos.write(bytes, 0, count);
            }
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] byteArr = new byte[1024];
//            int length;
//            while ((length = is.read(byteArr)) != -1){
//                baos.write(byteArr, 0, length);
//            }
//            ans = baos.toString(StandardCharsets.UTF_8.name());
//            ans = new String(file.getBytes(), "utf-8");
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("ans value: " + ans);
//        return ans;
    }

//    private  String getMsg(MultipartFile file){
//        String ans = null;
//        try{
//            InputStream inputStream = file.getInputStream();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] byteArr = new byte[1024];
//            int length;
//            while ((length = inputStream.read(byteArr)) != -1){
//                baos.write(byteArr, 0, length);
//            }
//            ans = baos.toString(StandardCharsets.UTF_8.name());
////            ans = new String(file.getBytes(), "utf-8");
//        } catch (IOException e){
//            e.printStackTrace();
//            return e.getMessage();
//        }
////        System.out.println("ans value: " + ans);
//        return ans;
//    }

    /**
     * 文件上传
     *  curl 'http://127.0.0.1:8080/post/file' -X POST -F 'file=@hello.txt'
     */
    @PostMapping("/fileUpload1")
    public String fileUpload1(@RequestParam("file")MultipartFile file){
//        System.out.println("上传的文件名" + file.getOriginalFilename());
//        String filePath = "/home/lgh/get_curldata/";
        String filePath = "/home/lgh/curl_data/res/";
//        String filePath = "D:\\test\\res\\";
        String fileName = file.getOriginalFilename();
        save2Local(file, filePath);
        return "finshed";
    }

//    @PostMapping("/fileUpload2")
//    public String fileUpload2(MultipartHttpServletRequest request){
//        MultipartFile file = request.getFile("file");
//        String res = getMsg(file);
//        System.out.println(res);
//        strToFile(res, "D:\\test\\res\\result.log");
//        return res;
//    }

    /**
     *  较于fileUpload1，速度有
     * @param file
     * @return
     */
    @PostMapping("/fileUpload3")
    public String fileUpload3(@RequestParam("file")MultipartFile file){
        String filePath = "D:\\test\\res\\";
//        String filePath = "/home/lgh/curl_data/res/";
        String fileName = file.getOriginalFilename();
        try {
            File dst = new File(filePath + fileName);
            file.transferTo(dst);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
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
