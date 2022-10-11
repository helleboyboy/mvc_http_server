package com.example.mvc_http_server.other;

import java.io.*;

public class ReadBigFile {
    public static void main(String[] args) {
//        bufferedReader2bufferedWriter();
        bufferedInputStream2OutputStream();
    }

    public static void bufferedInputStream2OutputStream(){
        String fileName = "D:\\test\\fsimage.csv";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
            bos = new BufferedOutputStream(new FileOutputStream(new File(fileName + "_strem_out.log")));
            int count = 0;
            byte[] bytes = new byte[1024];
            while ((count = bis.read(bytes)) != -1){
                bos.write(bytes, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bufferedReader2bufferedWriter(){
        String fileName = "D:\\test\\fsimage.csv";
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            bw = new BufferedWriter(new FileWriter(new File(fileName + "_out.log")));
            int tempResult = 0;
            char ch;
            String res = null;
//            while ((tempResult = br.read()) != -1){
            while ((res = br.readLine()) != null){
//                ch = (char) tempResult;
//                System.out.println(ch);
//                System.out.println(res);
                bw.write(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
