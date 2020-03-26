package com.kaikeba.server.handler;

import com.kaikeba.content.HttpSession;
import com.kaikeba.content.HttpSessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.Iterator;
import java.util.Set;

public class HttpServerHandler extends ChannelInboundHandlerAdapter{
    private HttpRequest request;
    private DefaultFullHttpResponse response;
    private HttpSession session;
    /**
     * 依据掺入的标记内容进行是否向客户端中保存有session的数据操作
     * @param exists
     */
    private void setSessionId(boolean exists){
        if (exists == false){  //用户发送来的头信息里没有SessionId内容
            String encodeCookie = ServerCookieEncoder.STRICT.encode(HttpSession.SESSIONID, HttpSessionManager.createSession());
            this.response.headers().set(HttpHeaderNames.SET_COOKIE,encodeCookie);
        }
    }

    /**
     * 当前所发送的请求中是否存在有指定的SessionId数据
     * @return 如果有返回true，没有返回false
     */
    public boolean isHasSessionId(){
        //获取客户端发送来的cookie
       String cookieStr = this.request.headers().get(HttpHeaderNames.COOKIE);
       if (cookieStr == null || "".equals(cookieStr)){
           return false;
       }
        Set<Cookie> cookieSet = ServerCookieDecoder.STRICT.decode(cookieStr);
        Iterator<Cookie> iterator = cookieSet.iterator();
        while (iterator.hasNext()){
            Cookie cookie = iterator.next();
            if (HttpSession.SESSIONID.equals(cookie.name())){
                if (HttpSessionManager.isExists(cookie.value())){
                    this.session = HttpSessionManager.getSession(cookie.value());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){//实现了http请求的处理操作
            this.request = (HttpRequest) msg;
            System.out.println("【Netty-HTTP服务器】uri = " + this.request.uri() +
                    " Method = " + this.request.method() + " Headers= " + this.request.headers());
            String content =  "<html>" +
                                    "<head>" +
                                        "<title>Hello Netty</title>" +
                                    "</head>" +
                                    "<body>" +
                                        "<h1>好好学习，天天向上</h1>" +
                                    "</body>" +
                                "</html>"; //响应html代码
            this.responseWrite(ctx,content);
        }
    }

    private void responseWrite(ChannelHandlerContext ctx,String content){
        ByteBuf buf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
        this.response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,buf);
        this.response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=UTF-8");
        this.response.headers().set(HttpHeaderNames.CONTENT_LENGTH,String.valueOf(buf.readableBytes()));
        this.setSessionId(isHasSessionId());
        ctx.writeAndFlush(this.response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
