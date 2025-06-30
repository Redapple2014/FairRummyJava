package org.fcesur.messaging;

import java.io.Closeable;

public interface Publisher extends Closeable {

    boolean publishMessage(String message);
}
