package com.moxi.mougblog.base.holder;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * RequestAwareRunnable
 *
 * @author: 陌溪
 * @create: 2020-03-04-23:04
 */
public abstract class AbstractRequestAwareRunnable implements Runnable {

    private final RequestAttributes requestAttributes;
    private Thread thread;

    public AbstractRequestAwareRunnable() {
        this.requestAttributes = RequestContextHolder.getRequestAttributes();
        this.thread = Thread.currentThread();
    }

    @Override
    public void run() {
        try {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            onRun();
        } finally {
            if (Thread.currentThread() != thread) {
                RequestContextHolder.resetRequestAttributes();
            }
            thread = null;
        }
    }

    /**
     * 启动
     */
    protected abstract void onRun();
}
