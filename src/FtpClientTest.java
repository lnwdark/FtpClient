import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

public class FtpClientTest {

    public static void main(final String[] args) throws SocketException, IOException {
        String Host = "xxx.xxx.xxx.xxx";
        String Username = "xxxxx";
        String Pass = "xxxx";
        String remoteFilePath = "/";

        FTPSClient ftpClient = new FTPSClient();
        ftpClient.connect(Host);
        ftpClient.enterLocalPassiveMode();
        ftpClient.login(Username, Pass);

        ftpClient.execPBSZ(0);
        ftpClient.execPROT("P");
        ftpClient.type(FTP.BINARY_FILE_TYPE);
        int reply = ftpClient.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("FTP server refused connection, reply code: " + reply);
        }

        // print Connected
        System.out.println("Connected to " + Host + "." + ftpClient.getReplyString());

        // select path
        FTPFile[] ftpFile = ftpClient.listFiles(remoteFilePath);

        for (FTPFile file : ftpFile) {
            System.out.println(file.getName());
            try (FileOutputStream fos = new FileOutputStream("/files/" + file.getName())) {
                ftpClient.retrieveFile(remoteFilePath, fos);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        // Logout
        ftpClient.logout();
        System.out.println(ftpClient.getReplyString());

    }
}
