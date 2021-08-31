package com.hy.java.nio;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * HyFiles
 *
 * @author Jie.Hu
 * @date 8/31/21 2:19 PM
 */
public class HyFiles {

    public static void main(String[] args) throws IOException {

        Path tempPath = Files.createTempDirectory("hy-");
        System.out.println(tempPath);
        tempPath.toFile().deleteOnExit();
        File file = new File("test");
        URI uri = tempPath.toUri();
        String scheme = uri.getScheme();
        System.out.println(scheme);

    }
}
