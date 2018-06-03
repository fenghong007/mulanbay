package cn.mulanbay.pms.handler.job;

import cn.mulanbay.common.util.BeanFactoryUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.pms.handler.FtpHandler;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.ScheduleErrorCode;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import cn.mulanbay.schedule.job.AbstractBaseJob;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 向远程FTP文件夹同步
 * 把本地文件夹下的文件全部同步到对应的远程文件夹下
 * @author fenghong
 * @create 2017-07-10 21:44
 */
public class FtpSyncToRemoteJob extends AbstractBaseJob {

    private static Logger logger = Logger.getLogger(FtpSyncToRemoteJob.class);

    /**
     * 远程ftp目录
     */
    private String remotePath;

    /**
     * 本地需要同步的目录
     */
    private String loalPath;

    @Override
    public TaskResult doTask() {
        TaskResult result = new TaskResult();
        FtpHandler ftpHandler = BeanFactoryUtil.getBean(FtpHandler.class);
        FTPFile[] ftpFiles = ftpHandler.listFiles(remotePath);
        File localFilePath = new File(loalPath);
        File[] localFiles = localFilePath.listFiles();
        if(localFiles==null||localFiles.length==0){
            result.setExecuteResult(JobExecuteResult.SKIP);
            result.setComment("本地路径["+loalPath+"]无文件，不需要同步");
        }else{
            int syncs=0;
            for(File f : localFiles){
                if(f.isDirectory()){
                    logger.warn("还无法同步文件夹，路径："+f.getAbsolutePath());
                }else{
                    boolean ex = isRemoteExit(ftpFiles,f);
                    if(!ex){
                        try {
                            //同步
                            InputStream input = new FileInputStream(f);
                            ftpHandler.uploadFile(remotePath,f.getName(),input);
                            syncs++;
                        } catch (Exception e) {
                            logger.error("向远程文件夹["+remotePath+"]同步文件["+f.getAbsolutePath()+"]异常",e);
                        }

                    }
                }
            }
            result.setExecuteResult(JobExecuteResult.SUCESS);
            result.setComment("一共同步了"+syncs+"个文件");
        }
        return result;
    }

    /**
     * 判断远程文件夹下是否已经有该文件
     *
     * @param ftpFiles
     * @param localFile
     * @return
     */
    private boolean isRemoteExit(FTPFile[] ftpFiles,File localFile){
        if(ftpFiles==null||ftpFiles.length==0){
            return false;
        }else{
            for(FTPFile f : ftpFiles){
                if(f.getName().equals(localFile.getName())){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 参数格式：远程文件夹目录,本地文件夹地址
     * @param triggerDetail
     * @return
     */
    @Override
    public ParaCheckResult checkTriggerPara(String triggerDetail) {
        ParaCheckResult pcr = new ParaCheckResult();
        if(StringUtil.isEmpty(triggerDetail)){
            pcr.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_LENGTH_ERROR);
        }
        String[] ss = triggerDetail.split(",");
        if(ss.length<2){
            pcr.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_LENGTH_ERROR);
        }else{
            remotePath=ss[0];
            loalPath=ss[1];
        }
        return pcr;
    }
}
