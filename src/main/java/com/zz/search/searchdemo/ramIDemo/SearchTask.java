package com.zz.search.searchdemo.ramIDemo;

import java.util.concurrent.Callable;

/**
 * @Author zhaozhou
 * @Time 2019/8/9 14:08
 * @DESC
 */
public class SearchTask implements Callable<Long> {


    @Override
    public Long call() throws Exception {
        try {
            long start = System.nanoTime();
            RamSearcher.search("lucene");
            return System.nanoTime() - start;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0L;
    }
}
