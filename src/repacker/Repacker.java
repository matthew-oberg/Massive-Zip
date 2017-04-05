package repacker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Repacker{
    public static final int TIER = 10, FILE_COUNT = 10;
    public static final byte[] FILE_DATA = new byte[1000];
    static{Arrays.fill(FILE_DATA, (byte)'_');}
    public static void main(String[] args) throws Exception{
        pack(TIER, new File("data"));
    }
    private static void pack(final int tier, File baseDir) throws IOException{
        baseDir.mkdirs();
        {
            File f = new File(baseDir, "0.txt");
            f.createNewFile();
            try(FileOutputStream out = new FileOutputStream(f)){
                out.write(FILE_DATA);
            }
        }
        for(int t=1; t<=tier; ++t){
            String suffix = t == 1 ? ".txt" : ".zip";
            File prev = new File(baseDir, (t-1) + suffix), f = new File(baseDir, t + ".zip");
            System.out.println(t + ": " + prev + " > " + f);
            if(f.exists()) continue;
            f.createNewFile();
            try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f))){
                for(int i=0; i<FILE_COUNT; ++i){
                    try(FileInputStream in = new FileInputStream(prev)){
                        byte[] buf = new byte[4096];
                        ZipEntry entry = new ZipEntry(i + suffix);
                        out.putNextEntry(entry);
                        int read;
                        while((read = in.read(buf)) >= 0){
                            out.write(buf, 0, read);
                        }
                        out.closeEntry();
                    }
                }
            }
        }
    }
}
