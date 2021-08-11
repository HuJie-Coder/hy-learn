package com.hy.netty.start;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * DiscardServerHandler
 * <p>
 * server
 *
 * @author Jie.Hu
 * @date 8/6/21 10:25 PM
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {

//        try {
//        ByteBuf byteBuf = (ByteBuf) msg;
//            while (byteBuf.isReadable()) {
//                System.out.print((char) byteBuf.readByte());
//                System.out.flush();
//            }
//            System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }

        channelHandlerContext.write(msg);
        channelHandlerContext.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
