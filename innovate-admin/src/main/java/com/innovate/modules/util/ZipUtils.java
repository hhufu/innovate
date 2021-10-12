package com.innovate.modules.util;

import com.innovate.common.utils.OSSUtils;
import com.innovate.modules.declare.entity.DeclareAttachEntity;
import com.innovate.modules.finish.entity.FinishAttachEntity;
import com.innovate.modules.match.entity.MatchAttachEntity;
import org.apache.shiro.session.Session;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @ProjectName gdm
 * @Author 麦奇
 * @Email biaogejiushibiao@outlook.com
 * @Date 8/16/19 8:08 AM
 * @Version 1.0
 * @Description:
 **/
@SuppressWarnings("unchecked")
public class ZipUtils {
    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩成ZIP 方法1
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 压缩成ZIP 方法2
     *
     * @param out 压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip2(List<?> m, OutputStream out, Session session) throws Exception {
        ZipOutputStream zos = null;
        session.setAttribute("totalFile", m.size());

        zos = new ZipOutputStream(out);
        int i = 0;
        Type t = m.get(0).getClass();
        if (t.equals(MatchAttachEntity.class)) {
            List<MatchAttachEntity> matchAttachEntities = (List<MatchAttachEntity>) m;
            HashMap<String,Integer> hashMap = new HashMap<>();

            //原本的代码
            for (MatchAttachEntity mm : matchAttachEntities) {

                String path = mm.getInstituteName() + "/" + forTy(mm.getMatchGroupType()) + "/" + mm.getMatchName() + "/" + mm.getAttachName();

                if( hashMap.get(path)!=null )
                    continue;

                hashMap.put(path,1);

                session.setAttribute("percent", i);
                File file = OSSUtils.downloadFileFromOSS(mm.getAttachPath(), mm.getAttachName());
                i++;
                byte[] buf = new byte[BUFFER_SIZE];

                zos.putNextEntry(new ZipEntry(path));
                int len;
                FileInputStream in = new FileInputStream(file);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
        } else if (t.equals(DeclareAttachEntity.class)) {
            List<DeclareAttachEntity> declareAttachEntities = (List<DeclareAttachEntity>) m;

            HashMap<String,Integer> hashMap = new HashMap<>();

            for (DeclareAttachEntity mm : declareAttachEntities) {

                String path = mm.getInstituteName() + "/" + groupType(mm.getDeclareGroupType()) + "/" + mm.getDeclareName() + Math.floor(Math.random() * 10) + "/" + mm.getAttachName();

                if( hashMap.get(path)!=null )
                    continue;

                hashMap.put(path,1);

                session.setAttribute("percent", i);
                File file = OSSUtils.downloadFileFromOSS(mm.getAttachPath(), mm.getAttachName());
                i++;
                byte[] buf = new byte[BUFFER_SIZE];


                zos.putNextEntry(new ZipEntry(path));
                int len;
                FileInputStream in = new FileInputStream(file);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
        } else if (t.equals(FinishAttachEntity.class)) {
            List<FinishAttachEntity> finishAttachEntities = (List<FinishAttachEntity>) m;

            HashMap<String,Integer> hashMap = new HashMap<>();

            for (FinishAttachEntity mm : finishAttachEntities) {

                String path = groupType(mm.getDeclareGroupType()) + "/" + mm.getDeclareName() + Math.floor(Math.random() * 10) + "/" + mm.getAttachName();
                if( hashMap.get(path)!=null )
                    continue;

                hashMap.put(path,1);

                session.setAttribute("percent", i);
                File file = OSSUtils.downloadFileFromOSS(mm.getAttachPath(), mm.getAttachName());
                i++;
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(path));
                int len;
                FileInputStream in = new FileInputStream(file);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
        }
        try {
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String groupType(Integer type) {
        String name = "";
        switch (type) {
            case 1:
                name = "创新训练项目";
                break;
            case 2:
                name = "创业训练项目";
                break;
            case 3:
                name = "创业实践项目";
                break;
            default:
                break;
        }
        return name;
    }

    public static String forTy(Integer type) {
        String name = "";
        switch (type) {
            case 1:
                name = "创意组";
                break;
            case 2:
                name = "初创组";
                break;
            case 3:
                name = "成长组";
                break;
            case 4:
                name = "师生共创组";
                break;
            case 5:
                name = "\"青年红色梦之旅\"赛道";
                break;
            case 6:
                name = "其它";
                break;
            default:
                break;
        }
        return name;
    }

    /**
     * 压缩成ZIP 方法2
     *
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }
}
