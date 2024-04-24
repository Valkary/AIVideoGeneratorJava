package Ffmpeg;

import Exif.FileMetadata;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoBuilder {
    public static void BuildVideo(String outputPath, String outputName, List<FileMetadata> metadata, String subtitles_path) {
        String fileName = outputPath.replace("/", "\\") + outputName + ".mp4";

        try {
            List<String> filenames = new ArrayList<>();

            int counter = 1;
            for (FileMetadata meta : metadata) {
                String filename = ".\\tmp\\vid00" + counter + ".mp4";
                filenames.add(".\\vid00" + counter + ".mp4");

                if (Objects.equals(meta.getMimeType(), "mp4")) {
                    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "copy", meta.getPath().replace("/", "\\"), filename);

                    builder.start();
                } else {
                    ProcessBuilder builder = new ProcessBuilder("ffmpeg", "-loop", "1", "-r", "1", "-t", "5", "-i", meta.getPath().replace("/", "\\"), "-vf", "scale=1920:1080", filename);

                    System.out.println(String.join(" ", builder.command()));

                    builder.start();
                }
                counter++;
            }

            try {
                File myObj = new File("./tmp/tmp.txt");

                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());

                    FileWriter writer = new FileWriter("./tmp/tmp.txt");

                    for (String line : filenames) {
                        writer.write("file '" + line + "'\n");
                    }

                    writer.close();
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-f", "concat", "-safe", "0", "-i", ".\\tmp\\tmp.txt", "-c", "copy", fileName);

            System.out.println(String.join(" ", pb.command()));
            Process p = pb.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}