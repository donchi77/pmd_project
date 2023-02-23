package com.pmdproject.utils;

public interface Argon2dParameters {
    int MEMORY = 1024 * 100; // 100 MB di memoria
    int ITERATIONS = 10;
    int PARALLELISM = 4;
    int DEFAULT_HASH_LENGTH = 64;
    int DEFAULT_SALT_LENGTH = 16;
}
