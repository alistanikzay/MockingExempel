package org.example;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime getCurrentTime();

    Object now();

}


