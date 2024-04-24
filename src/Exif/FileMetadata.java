package Exif;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class FileMetadata implements Comparable<FileMetadata> {
    private static final String CREATED_AT_KEY = "File Modification Date/Time";
    private static final String LATITUDE_KEY = "GPS Latitude";
    private static final String LONGITUDE_KEY = "GPS Longitude";
    private static final String FILENAME_KEY = "File Name";
    private static final String WIDTH_KEY = "Image Width";
    private static final String HEIGHT_KEY = "Image Height";
    private static final String MIME_TYPE = "File Type Extension";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    private String filename;
    private LocalDateTime createdAt;
    private String longitude;
    private String latitude;
    private String width;
    private String height;
    private String path;
    private String mimeType;

    public FileMetadata(String path) {
        try {
            ProcessBuilder builder = new ProcessBuilder("exiftool", path);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }

                int splitIndex = line.indexOf(":");
                String key = line.substring(0, splitIndex - 1).trim();
                String value = line.substring(splitIndex + 1).trim();

                switch (key) {
                    case CREATED_AT_KEY -> createdAt = LocalDateTime.parse(value.split("-")[0].trim(), formatter);
                    case LONGITUDE_KEY -> longitude = value;
                    case LATITUDE_KEY -> latitude = value;
                    case FILENAME_KEY -> filename = value;
                    case WIDTH_KEY -> width = value;
                    case HEIGHT_KEY -> height = value;
                    case MIME_TYPE -> mimeType = value;
                }
            }
            this.path = path;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public String toString() {
        return "Image metadata {\n"
                + "  filename: " + getFilename() + ",\n"
                + "  width: " + getWidth() + ",\n"
                + "  height: " + getHeight() + ",\n"
                + "  created_at: " + getCreatedAt() + ",\n"
                + "  longitude: " + getLongitude() + ",\n"
                + "  latitude: " + getLatitude() + ",\n"
                + "  path: " + getPath() + ",\n"
                + "  mime: " + getMimeType() + ",\n"
                + "}";
    }

    @Override
    public int compareTo(FileMetadata o) {
        if (getCreatedAt() == null) return -1;
        return getCreatedAt().compareTo(o.getCreatedAt());
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFilename() {
        return filename;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getPath() {
        return path;
    }
}
