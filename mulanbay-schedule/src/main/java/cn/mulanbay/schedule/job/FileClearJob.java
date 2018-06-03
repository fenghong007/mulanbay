package cn.mulanbay.schedule.job;

import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.FileUtil;
import cn.mulanbay.common.util.ZipUtil;
import cn.mulanbay.schedule.ParaCheckResult;
import cn.mulanbay.schedule.ScheduleErrorCode;
import cn.mulanbay.schedule.TaskResult;
import cn.mulanbay.schedule.enums.JobExecuteResult;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Date;

/**
 *
 * 文件清理
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class FileClearJob extends AbstractBaseJob {

	private static Logger logger = Logger.getLogger(FileClearJob.class);

	public static String fileSeparate = "/";

	//源文件夹，即需要清理的文件夹
	private String sourcePath;

	//被清理文件的备份文件夹
	private String backupPath;

	//清理时是否需要备份
	private boolean isBackup;

	//备份时是否要压缩
	private boolean isZip;

	//压缩文件夹的时间格式
	private String backupDateFormat;

	private int keepDays=7;

	@Override
	public TaskResult doTask() {
		TaskResult tr = new TaskResult();
		String desPath = null;
		if (isBackup) {
			// 生成备份文件夹
			String datePath = DateUtil.getFormatDate(new Date(),
					backupDateFormat);
			desPath = backupPath + fileSeparate + datePath;
			FileUtil.checkPathExits(desPath);
		}
		Date bussDay = this.getBussDay();
		logger.debug("开始清理文件夹[" + sourcePath + "]");
		Date compareDate = DateUtil.getDate(0-keepDays,bussDay);
		clearFile(compareDate .getTime(), desPath, new File(sourcePath));
		logger.debug("清理文件夹[" + sourcePath + "]结束");

		if (isBackup && isZip) {
			logger.debug("开始压缩文件夹[" + desPath + "]");
			File zip = new File(desPath + ".zip");
			ZipUtil.ZipFiles(zip, "", new File(desPath));
			logger.debug("压缩文件夹[" + desPath + "]结束");
			// 删除文件夹
			FileUtil.deleteFolder(new File(desPath));
		}
		tr.setExecuteResult(JobExecuteResult.SUCESS);
		return tr;
	}

	/**
	 * 清理文件
	 * 
	 * @param expireDay
	 *            过期时间
	 * @param desPath
	 *            备份的目标文件夹
	 * @param file
	 *            待清理的文件或文件夹
	 */
	private void clearFile(long expireDay, String desPath, File file) {
		if (file.isFile()) {
			// 清理
			try {
				if (file.lastModified() < expireDay) {
					// 通过文件最后修改时间判断
					if (isBackup) {
						File des = new File(backupPath + fileSeparate
								+ file.getName());
						FileUtil.moveFile(file, des, true);
					} else {
						boolean b = file.delete();
						if (!b) {
							logger.error("删除文件[" + file.getAbsolutePath()
									+ "]异常");
						}
					}
				}

			} catch (Exception e) {
				logger.error("清理文件[" + file.getAbsolutePath() + "]异常", e);
			}
		} else {
			for (File f : file.listFiles()) {
				clearFile(expireDay, desPath, f);
			}

		}

	}

	@Override
	public ParaCheckResult checkTriggerPara(String triggerDetail) {
		ParaCheckResult rb = new ParaCheckResult();
		rb.setMessage("参数格式为：1. 待清理的路径 ,2. 保留天数, 3. 备份的路径,4. 是否要备份（true/false）,5. 备份时是否要打包（true/false）,6. 备份文件时间格式");
		try {
			if (triggerDetail == null || triggerDetail.isEmpty()) {
				rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_NULL);
				return rb;
			}
			/**
			 * 配置参数 <br>
			 * 1. 待清理的路径 <br>
			 * 2. 保留天数 <br>
			 * 3. 备份的路径<br>
			 * 4. 是否要备份（true/false）<br>
			 * 5. 备份时是否要打包（true/false）<br>
			 * 6. 备份文件时间格式
			 */
			String[] para = triggerDetail.split(",");
			if (para.length < 6) {
				rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_LENGTH_ERROR);
				return rb;
			}
			sourcePath = para[0];
			File sf = new File(sourcePath);
			if (!sf.exists()) {
				rb.setErrorCode(ScheduleErrorCode.FILE_PATH_NOT_EXIT);
				return rb;
			}
			keepDays = Integer.valueOf(para[1]);
			backupPath = para[2];
			isBackup = Boolean.parseBoolean(para[3]);
			isZip = Boolean.parseBoolean(para[4]);
			backupDateFormat = para[5];
		} catch (Exception e) {
			logger.error("检查参数异常", e);
			rb.setErrorCode(ScheduleErrorCode.TRIGGER_PARA_FORMAT_ERROR);
			rb.setMessage(rb.getMessage() + "," + e.getMessage());
		}
		return rb;
	}

}
