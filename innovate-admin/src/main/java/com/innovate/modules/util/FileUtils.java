package com.innovate.modules.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.innovate.common.utils.OSSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Date;

public class FileUtils {

    private static String endpoint = "oss-cn-shenzhen.aliyuncs.com";

    private static String accessKeyId = "LTAI0X8ssJ8ezs4C";

    private static String accessKeySecret = "BMbWYHeCOaF8BMCXcgCY2c2TwyPHEs";

    private static String bucketName = "innovate-admin";

	private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 删除所有文件
     * @param path
     */
    public static void deleteTotal(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            if (file.isDirectory())
            {
                File[] files = file.listFiles();
                for (File myFile : files)
                {
                    if (myFile.isDirectory())
                        deleteTotal(myFile.getPath());
                    else
                        myFile.delete();
                }
            }
            file.delete();
        }
    }

    /**
     * 使用外网访问私有oss文件下载签名
     * @param
     * @return
     */
    public static String getUrl(String key){

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId,
                accessKeySecret);
        // 设置URL过期时间为10分钟
        Date expiration = new Date(new Date().getTime() + 600 * 1000);
        GeneratePresignedUrlRequest generatePresignedUrlRequest ;
        generatePresignedUrlRequest =new GeneratePresignedUrlRequest(bucketName, key);
        generatePresignedUrlRequest.setExpiration(expiration);
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
    public static void delFile(String key){
        OSSUtils.delFile(key);
    }

    /**
     * 文件下载核心
     *
     * @param response
     * @param realPath
     */
    public static void download(final HttpServletResponse response, String realPath) {
//        File file = new File(realPath);
//        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
//			try {
//				response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}

            File file = OSSUtils.downloadFileFromOSS(realPath);
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
//                InputStream inputStream = OSSUtils.downloadFromOSS(realPath);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();

                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }

//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                while (true) {
//                    String line = reader.readLine();
//                    if (line == null) break;
//                    System.out.println("\n" + line);
//                }


            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static String upLoad (String UPLOAD_FILES_PATH, String fileName, MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return "文件为空";
        }else{
            File dest = new File(UPLOAD_FILES_PATH  + "/" +  fileName );
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
        }
        return "true";
    }

}
