package com.innovate.modules.points.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.innovate.modules.points.dao.InnovateStudentPointsDao;
import com.innovate.modules.points.entity.InnovateStudentPointsEntity;
import com.innovate.modules.points.service.InnovateStudentPointsService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InnovateStudentPointsEntityListener extends AnalysisEventListener<InnovateStudentPointsEntity> {

        private static final Logger LOGGER = LoggerFactory.getLogger(InnovateStudentPointsEntity.class);
        /**
         * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
         */
        private static final int BATCH_COUNT = 500;

        List<InnovateStudentPointsEntity> list = new ArrayList<InnovateStudentPointsEntity>();
        /**
         * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
         */
        private InnovateStudentPointsService innovateStudentPointsService;

    public InnovateStudentPointsEntityListener(InnovateStudentPointsService innovateStudentPointsService) {
        this.innovateStudentPointsService = innovateStudentPointsService;
    }

    /**
         * 这个每一条数据解析都会来调用
         *
         * @param innovateStudentPointsEntity
         *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
         * @param context
         */
        @Override
        public void invoke(InnovateStudentPointsEntity innovateStudentPointsEntity, AnalysisContext context) {
            LOGGER.info("解析到一条数据:{}", JSON.toJSONString(innovateStudentPointsEntity));
            list.add(innovateStudentPointsEntity);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (list.size() >= BATCH_COUNT) {
                saveData();
                // 存储完成清理 list
                list.clear();
            }
        }
        /**
         * 所有数据解析完成了 都会来调用
         *
         * @param context
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
            saveData();
            LOGGER.info("所有数据解析完成！");
        }
        /**
         * 加上存储数据库
         */
        private void saveData() {
            LOGGER.info("{}条数据，开始存储数据库！", list.size());
            innovateStudentPointsService.insertBatch(list);
            LOGGER.info("存储数据库成功！");
        }


    }
