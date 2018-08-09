package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Huangsky on 2018/8/9.
 */

@Service
public class SensitiveService implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    //敏感词过滤---不健康内容
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt=bufferedReader.readLine())!=null){
                addWord(lineTxt.trim());
            }
            read.close();

        }catch (Exception e){
            logger.error("读取敏感词文件失败："+e.getMessage());
        }
    }

    //增加关键词
    private void addWord(String lineTxt){
        TrieNode tempNode = rootNode;
        // 循环每个字节
        for (int i = 0;i < lineTxt.length(); i++){
            Character c = lineTxt.charAt(i);
            if (isSymbol(c)){
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null){  // 没初始化
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode = node;

            if (i == lineTxt.length()-1){
                // 关键词结束， 设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }

    }


    public class TrieNode{
        //是不是关键词的节点
        private boolean end =false;
        //当前节点下的所有节点
        private Map<Character,TrieNode> subNodes = new HashMap<>();
        //添加节点
        public void addSubNode(Character key,TrieNode node){
            subNodes.put(key,node);
        }
        //获取下一个节点
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        //是否是关键词的结尾
        boolean isKeywordEnd(){
            return end;
        }
        //设置关键词的结尾标记
        void setKeywordEnd(boolean end){
            this.end = end;
        }
    }

    //
    private boolean isSymbol(char c){
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic<0x2E80||ic>0x9FFF);//东亚文字范围0x2E80-0x9FFF
    }


    private TrieNode rootNode = new TrieNode();

    //过滤
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return text;
        }
        StringBuilder sb = new StringBuilder();
        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;// 回滚数
        int position = 0;// 当前比较的位置
        while (position<text.length()){
            char c = text.charAt(position);

            if (isSymbol(c)){
                if (tempNode == rootNode ){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            // 当前位置的匹配结束
            if (tempNode == null){
                // 以begin开始的字符串不存在敏感词
                sb.append(text.charAt(position));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                // 发现敏感词， 从begin到position的位置用replacement替换掉
                sb.append(replacement);
                position = position+1;
                begin = position;
                tempNode = rootNode;
            }else {
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();


    }


    public static void main(String[] args){
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.println(s.filter("hi  你好色 情"));

    }

}
