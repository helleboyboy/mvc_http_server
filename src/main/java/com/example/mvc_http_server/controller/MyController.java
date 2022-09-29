package com.example.mvc_http_server.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
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

    private  String getMsg(MultipartFile file){
        String ans = null;
        try{
            ans = new String(file.getBytes(), "utf-8");
        } catch (IOException e){
            e.printStackTrace();
            return e.getMessage();
        }
//        System.out.println("ans value: " + ans);
        return ans;
    }

    /**
     * 文件上传
     *  curl 'http://127.0.0.1:8080/post/file' -X POST -F 'file=@hello.txt'
     */
    @PostMapping("/fileUpload1")
    public String fileUpload1(@RequestParam("file")MultipartFile file, @RequestHeader Map<String, String> name){
        System.out.println("name: " + name);
        String res = getMsg(file);
        System.out.println(res);
        strToFile(res, "D:\\test\\res\\result.log");
        return res;
    }

    @PostMapping("/fileUpload2")
    public String fileUpload2(MultipartHttpServletRequest request){
        MultipartFile file = request.getFile("file");
        String res = getMsg(file);
        System.out.println(res);
        strToFile(res, "D:\\test\\res\\result.log");
        return res;
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
