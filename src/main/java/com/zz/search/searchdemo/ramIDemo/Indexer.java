package com.zz.search.searchdemo.ramIDemo;

import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @Author zhaozhou
 * @Time 2019/8/5 21:20
 * @DESC
 */
public class Indexer {

    private IndexWriter indexWriter;

    public Indexer(String indexPath) throws IOException {
        //索引初始化
        Directory directory = FSDirectory.open(Paths.get(indexPath));
        //配置分词器为CJK中文分词器
        IndexWriterConfig writerConfig = new IndexWriterConfig(new CJKAnalyzer());
        indexWriter = new IndexWriter(directory, writerConfig);
        //删除所有索引，以防重复索引
        indexWriter.deleteAll();
    }

    public void close() throws IOException{
        indexWriter.close();
    }

    //将dataPath下的所有文件及文件夹进行索引
    public int index(String dataPath) throws IOException{
        File file = new File(dataPath);
        if(!file.isFile()){
            File[] fileNames = file.listFiles();
            indexFile(file);
            if(fileNames != null){
                for (File f: fileNames){
                    index(f.getCanonicalPath());
                }
            }
            return 1;
        }else{
            indexFile(file);
            return 1;
        }
    }


    //对文件进行索引
    private void indexFile(File f) throws IOException{
        //System.out.println("index file:" + f.getCanonicalPath());
        Document d = new Document();

        //文件名
        d.add(new TextField("name", f.getName(), Field.Store.YES));
        //路径
        d.add(new TextField("path", f.getCanonicalPath(), Field.Store.YES));
        int suffixIndex = f.getName().indexOf('.');

        indexWriter.addDocument(d);
    }
}
