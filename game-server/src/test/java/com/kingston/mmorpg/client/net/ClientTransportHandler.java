package com.kingston.mmorpg.client.net;

import com.kingston.mmorpg.client.IoSession;
import com.kingston.mmorpg.framework.net.socket.message.Message;
import com.kingston.mmorpg.game.gm.message.ReqGmCommand;
import com.kingston.mmorpg.game.login.message.ReqAccontLogin;
import com.kingston.mmorpg.game.player.message.ReqCreateNewPlayer;
import com.kingston.mmorpg.game.player.message.ReqSelectPlayer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientTransportHandler extends ChannelInboundHandlerAdapter {

	public ClientTransportHandler(){

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx){
		IoSession session = new IoSession(ctx.channel());
		SessionManager.getInstance().registerSession(session);
		
		login();
		selectedPlayer(10000L);
		
		ReqGmCommand reqGm = new ReqGmCommand();
		reqGm.setParams("level 10");
		SessionManager.getInstance().sendMessage(reqGm);
	}
	
	public void createNew() {
		ReqCreateNewPlayer req = new ReqCreateNewPlayer();
		req.setName("Happy");
		SessionManager.getInstance().sendMessage(req);
	}

	public void login() {
		ReqAccontLogin req = new ReqAccontLogin();
		req.setPassword("kingston");
		req.setAccountId(123L);
		SessionManager.getInstance().sendMessage(req);
	}


	public void selectedPlayer(long playerId) {
		ReqSelectPlayer req = new ReqSelectPlayer();
		req.setPlayerId(playerId);
		SessionManager.getInstance().sendMessage(req);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Message packet = (Message)msg;
		System.err.println("收到响应-->" + packet);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("客户端关闭1");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.err.println("客户端关闭3");
		Channel channel = ctx.channel();
		cause.printStackTrace();
		if(channel.isActive()){
			System.err.println("simpleclient"+channel.remoteAddress()+"异常");
		}
	}
}