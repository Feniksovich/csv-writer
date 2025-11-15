package org.writer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Writable {
    void writeToFile(List<?> data, Path destination) throws IOException;
}
