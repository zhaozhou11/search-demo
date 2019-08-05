package com.zz.search.searchdemo.simpleDemo;

import java.io.IOException;

/**
 * @Author zhaozhou
 * @Time 2019/8/5 21:20
 * @DESC
 */
public class Main {

    private static  String INDEX_PATH = "D:\\index";

    public static void main(String[] args){
        try {
            Indexer indexer = new Indexer(INDEX_PATH);
            indexer.index("D:\\book");
            indexer.close();

            Searcher.search(INDEX_PATH,"可视化");

        }catch (Exception e){
            System.out.println(e.getCause());
        }

    }
}
