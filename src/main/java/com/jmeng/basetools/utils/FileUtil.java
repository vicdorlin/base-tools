package com.jmeng.basetools.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vicdor
 * @create 2018-09-19 18:12
 */
@Slf4j
public class FileUtil {
    public static boolean exist(String fileName) {
        return new File(fileName).exists();
    }

    /**
     * 判断指定的文件是否存在。
     *
     * @param filepath 要判断的文件的文件名
     * @return 存在时返回true，否则返回false。
     * @since 1.0
     */
    public static boolean isFile(String filepath) {
        return new File(filepath).isFile();
    }


    public static boolean isDirectory(String directoryPath) {
        return new File(directoryPath).isDirectory();

    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static File createNewFile(String path) {
        try {
            File file = new File(path);
            if (file.exists() && !file.isDirectory()) {
                if (file.delete()) {
                    log.info("FileUtil.createNewFile file:{} deleted", path);
                }
            }
            if (file.createNewFile()) {
                log.info("FileUtil.createNewFile file:{} created", path);
            }
            return file;
        } catch (IOException e) {
            log.error("FileUtil.createNewFile 文件创建失败 path:{}", path);
            return null;
        }
    }

    /**
     * 创建指定的目录。
     */
    public static boolean makeDirectory(String path) {
        return new File(path).mkdirs();
    }

    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     *
     * @param directory 要清空的目录
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
     * @since 1.0
     */
    public static boolean cleanThisDirectory(File directory) {
        boolean result = true;
        File[] entries = directory.listFiles();
        for (int i = 0; i < entries.length; i++) {
            if (!entries[i].delete()) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     *
     * @param directoryName 要清空的目录的目录名
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
     * @since 1.0
     */
    public static boolean cleanThisDirectory(String directoryName) {
        File dir = new File(directoryName);
        return cleanThisDirectory(dir);
    }

    /**
     * 删除指定目录及其中的所有内容。
     *
     * @param dirName 要删除的目录的目录名
     * @return 删除成功时返回true，否则返回false。
     * @since 1.0
     */
    public static boolean cleanDirectory(String dirName) {
        return cleanDirectory(new File(dirName));
    }

    /**
     * 删除指定目录及其中的所有内容。
     *
     * @param dir 要删除的目录
     * @return 删除成功时返回true，否则返回false。
     * @since 1.0
     */
    public static boolean cleanDirectory(File dir) {
        if ((dir == null) || !dir.isDirectory()) {
            throw new IllegalArgumentException("Argument " + dir +
                    " is not a directory. ");
        }

        File[] entries = dir.listFiles();
        int sz = entries.length;

        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                if (!cleanDirectory(entries[i])) {
                    return false;
                }
            } else {
                if (!entries[i].delete()) {
                    return false;
                }
            }
        }

        if (!dir.delete()) {
            return false;
        }
        return true;
    }

    /**
     * 从文件路径得到文件名。
     *
     * @param filePath 文件的路径，可以是相对路径也可以是绝对路径
     * @return 对应的文件名
     * @since 1.0
     */
    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    /**
     * 从文件名得到文件绝对路径。
     *
     * @param fileName 文件名
     * @return 对应的文件路径
     * @since 1.0
     */
    public static String getFilePath(String fileName) {
        File file = new File(fileName);
        return file.getAbsolutePath();
    }

    /**
     * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
     * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
     * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
     *
     * @param filePath 转换前的路径
     * @return 转换后的路径
     * @since 1.0
     */
    public static String toUnixPath(String filePath) {
        return filePath.replace('\\', '/');
    }

    /**
     * 从文件名得到UNIX风格的文件绝对路径。
     *
     * @param fileName 文件名
     * @return 对应的UNIX风格的文件路径
     * @see #toUnixPath(String filePath) toUNIXpath
     * @since 1.0
     */
    public static String getUnixFilePath(String fileName) {
        File file = new File(fileName);
        return toUnixPath(file.getAbsolutePath());
    }

    /**
     * 得到文件的类型。
     * 实际上就是得到文件名中最后一个“.”后面的部分。
     *
     * @param fileName 文件名
     * @return 文件名中的类型部分
     * @since 1.0
     */
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1).trim();
    }

    /**
     * 得到文件的名字部分。
     * 实际上就是路径中的最后一个路径分隔符后的部分。
     *
     * @param fileName 文件名
     * @return 文件名中的名字部分
     * @since 1.0
     */
    public static String getNamePart(String fileName) {
        int idx1 = fileName.lastIndexOf(File.separator);
        int idx2 = fileName.lastIndexOf(".");
        if (idx1 == -1 && idx2 == -1) {
            return fileName;
        } else if (idx2 == -1) {
            return fileName.substring(idx1 + 1);
        } else if (idx1 == -1) {
            return fileName.substring(0, idx2);
        } else {
            return fileName.substring(idx1 + 1, idx2);
        }
    }

    /**
     * 将文件名中的类型部分去掉。
     *
     * @param filename 文件名
     * @return 去掉类型部分的结果
     * @since 1.0
     */
    public static String trimType(String filename) {
        int index = filename.lastIndexOf(".");
        if (index != -1) {
            return filename.substring(0, index);
        } else {
            return filename;
        }
    }

    /**
     * 读取文件的内容
     * 读取指定文件的内容
     *
     * @param path 为要读取文件的绝对路径
     * @return 以行读取文件后的内容。
     * @since 1.0
     */
    public static final String getFileContent(String path) throws IOException {
        StringBuilder fileContentBuilder = new StringBuilder("");
        File f = new File(path);
        if (f.exists()) {
            try (FileReader fr = new FileReader(path);
                 BufferedReader br = new BufferedReader(fr);) {
                //从文件读取一行字符串
                String line = br.readLine();
                //判断读取到的字符串是否不为空
                while (line != null) {
                    fileContentBuilder.append(line);
                    fileContentBuilder.append(System.getProperty("line.separator"));
                    //从文件中继续读取一行数据
                    line = br.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return fileContentBuilder.toString();
    }

    /**
     * 将list按行写入文件
     *
     * @param list
     * @param filePath
     * @return
     */
    public static final boolean writeList2File(List<String> list, String filePath) throws IOException {
        //创建输出缓冲流对象
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            //遍历集合，得到每一个字符串元素，然后把该字符串元素作为数据写到文本文件
            for (int x = 0; x < list.size(); x++) {
                String s = list.get(x);
                bw.write(s);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    /**
     * 追加内容到文件
     *
     * @param filepath
     * @param lineContent
     * @return
     */
    public static final boolean appendLine2File(String filepath, String lineContent) {
        // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
        try (FileWriter writer = new FileWriter(filepath, true)) {
            writer.write(lineContent);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
            return true;
        } catch (IOException e) {
            log.error("FileUtil.appendLine2File 文件内容追加失败 filepath:{} lineContent:{}", filepath, lineContent);
            return false;
        }
    }


    /**
     * 拷贝文件
     *
     * @param in  输入文件
     * @param out 输出文件
     * @return
     * @throws Exception
     */
    public static final boolean copyFile(File in, File out) throws Exception {
        try {
            FileInputStream fis = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
            return true;
        } catch (IOException ie) {
            ie.printStackTrace();
            return false;
        }
    }

    /**
     * 拷贝文件
     *
     * @param infile  输入字符串
     * @param outfile 输出字符串
     * @return
     * @throws Exception
     */
    public static final boolean copyFile(String infile, String outfile) throws Exception {
        try {
            File in = new File(infile);
            File out = new File(outfile);
            return copyFile(in, out);
        } catch (IOException ie) {
            ie.printStackTrace();
            return false;
        }

    }

    /**
     * 把内容content写的path文件中
     *
     * @param content 输入内容
     * @param path    文件路径
     * @return
     */
    public static boolean saveFileAs(String content, String path) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(path), false);
            if (content != null) {
                fw.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static byte[] readFileByBytes(String fileName) throws IOException {
        FileInputStream in = new FileInputStream(fileName);
        byte[] bytes = null;
        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

            byte[] temp = new byte[1024];

            int size = 0;

            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }

            in.close();

            bytes = out.toByteArray();

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return bytes;
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static List<String> readFileByLines(String fileName, boolean needTrim) {
        List<String> returnString = new ArrayList<String>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                returnString.add(needTrim ? tempString.trim() : tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return returnString;
    }

    public static List<String> readFileByLines(String fileName) {
        return readFileByLines(fileName, false);
    }

    /**
     * 随机读取文件内容
     */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 读取文件内容
     *
     * @param fileName
     * @return
     */
    public static String readFileAll(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return "";
        }
    }

    public static void writeStream2File(InputStream inputStream, String filepath) {
        try {
            File file = new File(filepath);
            try {
                FileOutputStream fout = new FileOutputStream(file);
                int l;
                byte[] tmp = new byte[1024];
                while ((l = inputStream.read(tmp)) != -1) {
                    fout.write(tmp, 0, l);
                    // 注意这里如果用OutputStream.write(buff) 图片会失真
                }
                fout.flush();
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭低层流
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("file download error:{}", e);
        }
    }

    /**
     * 显示输入流中还剩的字节数
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}