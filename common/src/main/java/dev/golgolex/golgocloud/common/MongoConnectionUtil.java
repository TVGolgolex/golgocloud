package dev.golgolex.golgocloud.common;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/*
 * MIT License
 *
 * Copyright (c) 2024 23:29 Mario Pascal K.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
@Getter
public class MongoConnectionUtil {

    /**
     * A cache for storing MongoDB connections.
     * <p>
     * This cache is used to store MongoDB connections in order to reuse them throughout the application.
     * It provides methods to open a connection and retrieve an existing connection based on a given key.
     * </p>
     */
    private static final Map<String, MongoClient> connectionCache = new HashMap<>();

    /**
     * Opens a new connection to a MongoDB database.
     *
     * @param key              the key to identify the connection in the cache (not null)
     * @param mongoUser        the username to authenticate with the MongoDB server (not null)
     * @param mongoPassword    the password to authenticate with the MongoDB server (not null)
     * @param mongoHost        the hostname or IP address of the MongoDB server (not null)
     * @param mongoPort        the port number of the MongoDB server
     * @param mongoDatabase    the name of the MongoDB database to connect to (not null)
     * @param userDatabaseName the name of the user database to connect to (not null)
     * @return the opened MongoDatabase instance
     */
    public static MongoDatabase open(
            @NotNull String key,
            @NotNull String mongoUser,
            @NotNull String mongoPassword,
            @NotNull String mongoHost,
            int mongoPort,
            @NotNull String mongoDatabase,
            @NotNull String userDatabaseName
    ) {
        var connectionString = String.format("mongodb://%s:%s@%s:%d/%s",
                mongoUser, mongoPassword, mongoHost, mongoPort, userDatabaseName);
        var uri = new MongoClientURI(connectionString);
        var mongoClient = new MongoClient(uri);
        connectionCache.put(key, mongoClient);
        return mongoClient.getDatabase(mongoDatabase);
    }

    /**
     * Retrieves a MongoDB database based on the provided key and user database name.
     *
     * @param key                The key to retrieve the MongoDB client from the cache. Must not be null.
     * @param userDatabaseName   The name of the user database to retrieve. Must not be null.
     * @return The MongoDB database associated with the provided key and user database name.
     * @throws IllegalArgumentException if no connection is found for the given key.
     */
    public static MongoDatabase get(
            @NotNull String key,
            @NotNull String userDatabaseName
    ) {
        var mongoClient = connectionCache.get(key);
        if (mongoClient != null) {
            return mongoClient.getDatabase(userDatabaseName);
        } else {
            throw new IllegalArgumentException("No connection found for the given key.");
        }
    }

    /**
     * Terminates all active MongoDB connections in the connection cache.
     */
    public static void terminate() {
        for (final MongoClient mongoClient : connectionCache.values()) mongoClient.close();
    }

}
