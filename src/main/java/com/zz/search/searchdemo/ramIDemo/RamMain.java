package com.zz.search.searchdemo.ramIDemo;



import javax.xml.ws.Holder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author zhaozhou
 * @Time 2019/8/5 21:20
 * @DESC
 */
public class RamMain {

    public static  String INDEX_PATH = "D:\\index";


    public static int SEARCH_TASK_NUM = 10000;
    public static void main(String[] args){
        try {
            Indexer indexer = new Indexer(INDEX_PATH);
            indexer.index("D:\\book");
            indexer.close();

            List<Future> futures = new LinkedList<>();
            ExecutorService executors = Executors.newFixedThreadPool(100);
            RamSearcher.buildIndexReader(INDEX_PATH);
            for (int i =0 ; i< SEARCH_TASK_NUM; i ++){
                futures.add(executors.submit(new SearchTask()));
            }

            Holder<Long> tsHolder = new Holder<>(0L);
            futures.forEach(future ->{
                try {
                tsHolder.value += (long)future.get();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            System.out.println("totalTs = " + tsHolder.value / 1000000.0 + "ms");
            System.out.println("avgTs = " + tsHolder.value/(double)SEARCH_TASK_NUM/1000000.0 + "ms");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getCause());
        }

    }

}
