package com.example.faq.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

/**
 * @Author: lerry_li
 * @CreateDate: 2022/03/31
 * @Description 检索参数项配置
 */
@Configuration
@ConfigurationProperties(prefix = "retrieval")
@Data
public class RetrievalConfig {

    private Index index;
    private Search search;
    private String elasticsearchAPIPath;

    public static class Index {
        @Getter
        private String stdQStdA;
        @Getter
        private String stdQSimQ;
        @Getter
        @Setter
        private HashSet<String> indexNames;

        public Index() {
            this.indexNames = new HashSet<>();
        }

        public void setStdQStdA(String stdQStdA) {
            this.stdQStdA = stdQStdA;
            this.indexNames.add(this.stdQStdA);
        }

        public void setStdQSimQ(String stdQSimQ) {
            this.stdQSimQ = stdQSimQ;
            this.indexNames.add(this.stdQSimQ);
        }
    }

    @Data
    public static class Search {
        private Integer size;
    }
}