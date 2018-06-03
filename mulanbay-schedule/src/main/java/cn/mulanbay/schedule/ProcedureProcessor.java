package cn.mulanbay.schedule;

import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2017-10-30 9:36
 **/
public interface ProcedureProcessor {

    /**
     * 执行存储过程
     * @param procedureName
     * @param startDate
     * @param endDate
     */
    void executeProcedure(String procedureName, Date startDate, Date endDate);

    /**
     * 执行存储过程
     * @param procedureName
     * @param date
     */
    void executeProcedure(String procedureName, Date date);

    /**
     * 执行存储过程
     * @param procedureName
     */
    void executeProcedure(String procedureName);

}
