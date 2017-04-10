package Tp2.Ex01.Common;

import java.io.Serializable;

/**
 * User: juan
 * Date: 10/04/17
 * Time: 16:24
 */
public class TextFile implements Serializable{
    private String name;
    private String content;

    public TextFile(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
