package si.skycoin.discovery.queue;

import java.io.File;
import java.io.Serializable;

public class DiscoveryMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private File file;
    
    public File getFile() {
        return file;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
}
