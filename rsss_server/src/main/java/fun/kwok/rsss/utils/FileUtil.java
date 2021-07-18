package fun.kwok.rsss.utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class FileUtil {public static File createFile(String filePath){

    File path = null;
    try {
        path = new File(ResourceUtils.getURL("classpath:").getPath());
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    if (!path.exists()) {
        path = new File("");
    }
    File upload = new File(path.getAbsolutePath(), filePath.substring(0,filePath.lastIndexOf("/")+1));
    if (!upload.exists()) {
        upload.mkdirs();
    }
    return new File(path.getAbsolutePath(), filePath);
}

}
