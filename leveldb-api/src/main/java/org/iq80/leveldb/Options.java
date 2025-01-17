/*
 * Copyright (C) 2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iq80.leveldb;

/**
 * Options to control the behavior of a database (passed to DB::Open)
 */
public class Options
{
    /** If true, the database will be created if it is missing.*/
    private boolean createIfMissing = true;
    /** If true, an error is raised if the database already exists.*/
    private boolean errorIfExists;
    /**
     * Amount of data to build up in memory (backed by an unsorted log
     * on disk) before converting to a sorted on-disk file.
     *
     * Larger values increase performance, especially during bulk loads.
     * Up to two write buffers may be held in memory at the same time,
     * so you may wish to adjust this parameter to control memory usage.
     * Also, a larger write buffer will result in a longer recovery time
     * the next time the database is opened.
    */
    private int writeBufferSize = 4 << 20;
    /**
     * Number of open files that can be used by the DB.  You may need to
     * increase this if your database has a large working set (budget
     * one open file per 2MB of working set).
    */
    private int maxOpenFiles = 1000;
    /**
     * Number of keys between restart points for delta encoding of keys.
     * This parameter can be changed dynamically.  Most clients should
     * leave this parameter alone.
    */
    private int blockRestartInterval = 16;
    /**
     * Approximate size of user data packed per block.  Note that the
     * block size specified here corresponds to uncompressed data.  The
     * actual size of the unit read from disk may be smaller if
     * compression is enabled.  This parameter can be changed dynamically.
    */
    private int blockSize = 4 * 1024;
    /**
     * Compress blocks using the specified compression algorithm.  This
     * parameter can be changed dynamically.
     *
     * Default: kSnappyCompression, which gives lightweight but fast
     * compression.
     *
     * Typical speeds of kSnappyCompression on an Intel(R) Core(TM)2 2.4GHz:
     *    ~200-500MB/s compression
     *    ~400-800MB/s decompression
     * Note that these speeds are significantly faster than most
     * persistent storage speeds, and therefore it is typically never
     * worth switching to kNoCompression.  Even if the input data is
     * incompressible, the kSnappyCompression implementation will
     * efficiently detect that and will switch to uncompressed mode.
    */
    private CompressionType compressionType = CompressionType.SNAPPY;
    /**
     * If true, all data read from underlying storage will be
     * verified against corresponding checksums.
    */
    private boolean verifyChecksums = true;
    /**
     * If true, the implementation will do aggressive checking of the
     * data it is processing and will stop early if it detects any
     * errors.  This may have unforeseen ramifications: for example, a
     * corruption of one DB entry may cause a large number of entries to
     * become unreadable or for the entire DB to become unopenable.
     */
    private boolean paranoidChecks;
    /**
     * Comparator used to define the order of keys in the table.
     * Default: a comparator that uses lexicographic byte-wise ordering
     *
     * REQUIRES: The client must ensure that the comparator supplied
     * here has the same name and orders keys *exactly* the same as the
     * comparator provided to previous open calls on the same DB.
     */
    private DBComparator comparator;
    /**
     * Any internal progress/error information generated by the db will
     * be written to info_log if it is non-null, or to a file stored
     * in the same directory as the DB contents if info_log is null.
     */
    private Logger logger;
    /**
     * Control over blocks (user data is stored in a set of blocks, and
     * a block is the unit of reading from disk).

     * If non-null, use the specified cache for blocks.
     * If null, leveldb will automatically create and use an 8MB internal cache.
     */
    private long cacheSize;
    /**
     * If non-zero, use the specified filter policy to reduce disk reads.
     * Many applications will benefit from passing the result of
     * NewBloomFilterPolicy() here.
     * Return a new filter policy that uses a bloom filter with approximately
     * the specified number of bits per key.  A good value for bits_per_key
     * is 10, which yields a filter with ~ 1% false positive rate.
     *
     * Callers must delete the result after any database that is using the
     * result has been closed.
     *
     * Note: if you are using a custom comparator that ignores some parts
     * of the keys being compared, you must not use NewBloomFilterPolicy()
     * and must provide your own FilterPolicy that also ignores the
     * corresponding parts of the keys.  For example, if the comparator
     * ignores trailing spaces, it would be incorrect to use a
     * FilterPolicy (like NewBloomFilterPolicy) that does not ignore
     * trailing spaces in keys.
     */
    private int bitsPerKey;

    private int maxBatchSize = 52_000;

    //M
    private int maxManifestSize = 128;
    /**
     * EXPERIMENTAL: If true, append to existing MANIFEST and log files
     * when a database is opened.  This can significantly speed up open.
     *
     * Default: currently false, but may become true later.
     */
    private boolean reuseLogs = false;
    /**
     * Leveldb will write up to this amount of bytes to a file before
     * switching to a new one.
     * Most clients should leave this parameter alone.  However if your
     * filesystem is more efficient with larger files, you could
     * consider increasing the value.  The downside will be longer
     * compactions and hence longer latency/performance hiccups.
     * Another reason to increase this parameter might be when you are
     * initially populating a large database.
     */
    private long maxFileSize = 2 * 1024 * 1024;

    static void checkArgNotNull(Object value, String name)
    {
        if (value == null) {
            throw new IllegalArgumentException("The " + name + " argument cannot be null");
        }
    }

    public boolean createIfMissing()
    {
        return createIfMissing;
    }

    public Options createIfMissing(boolean createIfMissing)
    {
        this.createIfMissing = createIfMissing;
        return this;
    }

    public boolean errorIfExists()
    {
        return errorIfExists;
    }

    public Options errorIfExists(boolean errorIfExists)
    {
        this.errorIfExists = errorIfExists;
        return this;
    }

    public int writeBufferSize()
    {
        return writeBufferSize;
    }

    public Options writeBufferSize(int writeBufferSize)
    {
        this.writeBufferSize = writeBufferSize;
        return this;
    }

    public int maxOpenFiles()
    {
        return maxOpenFiles;
    }

    public Options maxOpenFiles(int maxOpenFiles)
    {
        this.maxOpenFiles = maxOpenFiles;
        return this;
    }

    public int blockRestartInterval()
    {
        return blockRestartInterval;
    }

    public Options blockRestartInterval(int blockRestartInterval)
    {
        this.blockRestartInterval = blockRestartInterval;
        return this;
    }

    public int blockSize()
    {
        return blockSize;
    }

    public Options blockSize(int blockSize)
    {
        this.blockSize = blockSize;
        return this;
    }

    public CompressionType compressionType()
    {
        return compressionType;
    }

    public Options compressionType(CompressionType compressionType)
    {
        checkArgNotNull(compressionType, "compressionType");
        this.compressionType = compressionType;
        return this;
    }

    public boolean verifyChecksums()
    {
        return verifyChecksums;
    }

    public Options verifyChecksums(boolean verifyChecksums)
    {
        this.verifyChecksums = verifyChecksums;
        return this;
    }

    public long cacheSize()
    {
        return cacheSize;
    }

    public Options cacheSize(long cacheSize)
    {
        this.cacheSize = cacheSize;
        return this;
    }

    public DBComparator comparator()
    {
        return comparator;
    }

    public Options comparator(DBComparator comparator)
    {
        this.comparator = comparator;
        return this;
    }

    public Logger logger()
    {
        return logger;
    }

    public Options logger(Logger logger)
    {
        this.logger = logger;
        return this;
    }

    public boolean paranoidChecks()
    {
        return paranoidChecks;
    }

    public Options paranoidChecks(boolean paranoidChecks)
    {
        this.paranoidChecks = paranoidChecks;
        return this;
    }

    public int bitsPerKey()
    {
        return bitsPerKey;
    }

    public Options bitsPerKey(int bitsPerKey)
    {
        this.bitsPerKey = bitsPerKey;
        return this;
    }

    public int maxBatchSize()
    {
        return maxBatchSize;
    }

    public Options maxBatchSize(int maxBatchSize)
    {
        if (maxBatchSize < 0) {
            maxBatchSize = Integer.MAX_VALUE;
        }
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    public int maxManifestSize()
    {
        return maxManifestSize;
    }

    public Options maxManifestSize(int maxManifestSize)
    {
        if (maxManifestSize < 0) {
            maxManifestSize = -1;
            maxBatchSize(-1);
        }
        this.maxManifestSize = maxManifestSize;
        return this;
    }

    public boolean reuseLogs()
    {
        return reuseLogs;
    }

    public Options reuseLogs(boolean reuseLogs)
    {
        this.reuseLogs = reuseLogs;
        return this;
    }

    public long maxFileSize()
    {
        return maxFileSize;
    }

    public Options maxFileSize(long maxFileSize)
    {
        this.maxFileSize = maxFileSize;
        return this;
    }
}
