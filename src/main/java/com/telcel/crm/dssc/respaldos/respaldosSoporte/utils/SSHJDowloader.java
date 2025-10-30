package com.telcel.crm.dssc.respaldos.respaldosSoporte.utils;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

public class SSHJDowloader {
	private SSHClient ssh;
	private SFTPClient sftpClient;
	
	public SSHClient getSsh() {
		return ssh;
	}
	public void setSsh(SSHClient ssh) {
		this.ssh = ssh;
	}
	
	public SFTPClient getSftpClient() {
		return sftpClient;
	}

	public void setSftpClient(SFTPClient sftpClient) {
		this.sftpClient = sftpClient;
	}

	public void conectar(String host, String puerto, String username, String password) {
		try {
			ssh = new SSHClient();
			ssh.addHostKeyVerifier(new PromiscuousVerifier());
			ssh.connect(host);
			ssh.authPassword(username, password);
			sftpClient = ssh.newSFTPClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
