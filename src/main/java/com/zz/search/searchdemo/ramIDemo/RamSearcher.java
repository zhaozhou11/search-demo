package com.zz.search.searchdemo.ramIDemo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author zhaozhou
 * @Time 2019/8/9 11:41
 * @DESC
 */
public class RamSearcher {

    private static int INDEX_NUM = 10;
    private static IndexReader[] indexReader = null;
    private static AtomicInteger index = new AtomicInteger(0);


    private static IndexReader getIndexReader(){
        int in = index.getAndIncrement() % indexReader.length ;
        return indexReader[in];
    }

    public static void buildIndexReader(String indexDir){
        indexReader = new IndexReader[INDEX_NUM];
        for (int i = 0; i < INDEX_NUM; i ++){
            try {
                // 得到读取索引文件的路径
                FSDirectory fsDir = FSDirectory.open(Paths.get(indexDir));

                RAMDirectory ramDirectory = new RAMDirectory(fsDir, IOContext.READ);
                Directory dir = ramDirectory;
                // 通过Dir得到路径下所有文件
                indexReader[i] = DirectoryReader.open(dir);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static void search(String q) throws IOException, ParseException {

        // 建立索引查询器
        IndexSearcher searcher = new IndexSearcher(getIndexReader());


        /********建立查询解析器********/

        // 第一个参数是要查询的字段； 第二个参数市分析器Analyzer
        String[] fields = {"name", "path"};
        Map<String, Float> fieldMap = new HashMap<>();
        fieldMap.put("name",100.0f);
        fieldMap.put("path",1.0f);
        QueryParser parser = new MultiFieldQueryParser(fields,new CJKAnalyzer());
        // 根据传进来的q查找
        Query query = parser.parse(q);
        // 计算索引开始时间
        long start = System.nanoTime();

        /********开始查询********/

        // 第一个参数是通过传过来的参数来查找得到的query； 第二个参数是要查询出的行数
        TopDocs hits = searcher.search(query,10);


        // 遍历hits.scoreDocs，得到scoreDoc
        // scoreDoc：得分文档，即得到的文档  scoreDocs：代表topDocs这个文档数组
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            //System.out.println("name:" + doc.get("name") + " ,path=" + doc.get("path"));

        }

        // 计算索引结束时间
        long end = System.nanoTime();
        System.out.println("匹配 "+ q + "，查询到 " + hits.totalHits + " 个记录， 用时：" + (end - start)/1000000.0);
    }
}
