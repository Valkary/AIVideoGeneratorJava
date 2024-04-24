import Exif.FileMetadata;
import Ffmpeg.VideoBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileMetadata img_meta1 = new FileMetadata("./Media/images/chicago.jpg");
        FileMetadata img_meta2 = new FileMetadata("./Media/images/pau.jpg");
        FileMetadata img_meta4 = new FileMetadata("./Media/videos/output.mp4");

        List<FileMetadata> images_metadata = new ArrayList<>();
        images_metadata.add(img_meta1);
        images_metadata.add(img_meta2);
        images_metadata.add(img_meta4);

        Collections.sort(images_metadata);
        System.out.println(images_metadata);

        VideoBuilder.BuildVideo("./", "yeahbuddy", images_metadata, null);
    }
}
