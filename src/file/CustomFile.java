package file;

import java.io.File;
import java.util.ArrayList;

public class CustomFile extends File{

    private String name;
    private String path;
    private ArrayList<byte[]> content;
    private String chgYn;

    public CustomFile (String path) {
        super(path);
        content = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getChgYn() {
        return chgYn;
    }

    public void setChgYn(String chgYn) {
        this.chgYn = chgYn;
    }

    public ArrayList<byte[]> getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content.add(content);
    }
}