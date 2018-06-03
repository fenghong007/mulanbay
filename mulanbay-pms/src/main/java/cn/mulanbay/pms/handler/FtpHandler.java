package cn.mulanbay.pms.handler;

import cn.mulanbay.business.handler.BaseHandler;
import cn.mulanbay.common.util.FtpUtil;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * FTP传输实现
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
@Component
public class FtpHandler extends BaseHandler {

    @Value("${ftp.serverHost}")
    private String serverHost;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    private int port = 21;

    public FtpHandler() {
        super("FTP处理");
    }

    /**
     * 上传文件
     *
     * @param path
     * @param filename
     * @param input
     * @return
     */
    public boolean uploadFile(String path, String filename, InputStream input) {
        return FtpUtil.uploadFile(serverHost, port, username, password, path, filename, input);
    }

    /**
     * 下载文件
     * @param remotePath
     * @param fileName
     * @param localPath
     * @return
     */
    public boolean downFile(String remotePath, String fileName, String localPath) {
        return FtpUtil.downFile(serverHost, port, username, password, remotePath, fileName, localPath);
    }

    public FTPFile[] listFiles(String remotePath){
        return FtpUtil.listFiles(serverHost,port,username,password,remotePath);
    }
}
