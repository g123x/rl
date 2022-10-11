package com.sg.rl.framework.components.db.datasource;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源
 *
 * @author steven
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String curDataSource = DynamicDataSourceContextHolder.getDataSourceType();
        if(!super.getResolvedDataSources().containsKey(curDataSource)){
            log.warn("dataSource  {} is not exist ,need use Default DS {} !!!",curDataSource,super.getResolvedDefaultDataSource());
        }
        return curDataSource;
    }
}