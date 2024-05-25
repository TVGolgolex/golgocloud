package dev.golgolex.golgocloud.logger;

/*
 * MIT License
 *
 * Copyright (c) 2024 ClayCloud contributors
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

import java.util.concurrent.BlockingQueue;

/**
 * A worker thread for the {@link AsyncPrintStream}.
 */
public class WorkerThread extends Thread {

    private final BlockingQueue<Runnable> blockingQueue;

    /**
     * Constructs an worker thread that takes work from {@code queue}.
     * Automatically started until interrupted.
     *
     * @param blockingQueue the blocking queue to take work from
     */
    WorkerThread(BlockingQueue<Runnable> blockingQueue) {
        this.blockingQueue = blockingQueue;
        setPriority(Thread.MIN_PRIORITY);
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                blockingQueue.take().run();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
