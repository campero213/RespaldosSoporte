package com.telcel.crm.dssc.respaldos.respaldosSoporte.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SFTPDownloader {
	
	private Channel channel;
	private Session session;
	private ChannelSftp sftp;
	private JSch ssh;
	
	public void conectar(String hostname, String puerto, String username, String password) {
		try{
			this.ssh = new JSch();
			this.session = this.ssh.getSession(username, hostname, Integer.parseInt(puerto));
			this.session.setConfig("StrictHostKeyChecking", "no");
			this.session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			this.session.setPassword(password);
			this.session.connect();
			this.channel = this.session.openChannel("sftp");
			this.channel.connect();
			this.sftp = (ChannelSftp)this.channel;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ChannelSftp getSftp() {
		return sftp;
	}

	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}

	public JSch getSsh() {
		return ssh;
	}

	public void setSsh(JSch ssh) {
		this.ssh = ssh;
	}
	
}
