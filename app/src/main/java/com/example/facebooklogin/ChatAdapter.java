package com.example.facebooklogin;

import android.content.Context;

import com.chad.library.adapter.base.BaseDelegateMultiAdapter;
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.facebooklogin.bean.AudioMsgBody;
import com.example.facebooklogin.bean.FileMsgBody;
import com.example.facebooklogin.bean.ImageMsgBody;
import com.example.facebooklogin.bean.Message;
import com.example.facebooklogin.bean.MsgBody;
import com.example.facebooklogin.bean.MsgSendStatus;
import com.example.facebooklogin.bean.MsgType;
import com.example.facebooklogin.bean.TextMsgBody;
import com.example.facebooklogin.bean.VideoMsgBody;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseDelegateMultiAdapter<Message, BaseViewHolder> {

    static final int TYPE_SEND_TEXT=1;
    static final int TYPE_RECEIVE_TEXT=2;

    static final int SEND_TEXT = R.layout.item_text_send;
    static final int RECEIVE_TEXT = R.layout.item_text_receive;

    /*
    private static final int SEND_LOCATION = R.layout.item_location_send;
    private static final int RECEIVE_LOCATION = R.layout.item_location_receive;*/


    public ChatAdapter(Context context, ArrayList<Message> data) {
        super(data);
        setMultiTypeDelegate(new BaseMultiTypeDelegate<Message>() {
            @Override
            public int getItemType(@NotNull List<? extends Message> list, int i) {
                return 0;
            }
        });
        getMultiTypeDelegate()
                .addItemType(TYPE_SEND_TEXT, SEND_TEXT).addItemType(TYPE_RECEIVE_TEXT,RECEIVE_TEXT);
                /*.registerItemType(TYPE_SEND_TEXT, SEND_TEXT)
                .registerItemType(TYPE_RECEIVE_TEXT,RECEIVE_TEXT);*/
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        setContent(helper, item);
        setStatus(helper, item);
        setOnClick(helper, item);

    }


    private void setStatus(BaseViewHolder helper, Message item) {
        MsgBody msgContent = item.getBody();
        if (msgContent instanceof TextMsgBody
                || msgContent instanceof AudioMsgBody ||msgContent instanceof VideoMsgBody ||msgContent instanceof FileMsgBody) {
            //只需要设置自己发送的状态
            MsgSendStatus sentStatus = item.getSentStatus();
            boolean isSend = item.getSenderId().equals(ChatActivity.mSenderId);
            if (isSend){
                if (sentStatus == MsgSendStatus.SENDING) {
                    helper.setVisible(R.id.chat_item_progress, true).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.FAILED) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, true);
                } else if (sentStatus == MsgSendStatus.SENT) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                }
            }
        } else if (msgContent instanceof ImageMsgBody) {
            boolean isSend = item.getSenderId().equals(ChatActivity.mSenderId);
            if (isSend) {
                MsgSendStatus sentStatus = item.getSentStatus();
                if (sentStatus == MsgSendStatus.SENDING) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.FAILED) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, true);
                } else if (sentStatus == MsgSendStatus.SENT) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                }
            } else {

            }
        }
    }

    private void setContent(BaseViewHolder helper, Message item) {
        if (item.getMsgType().equals(MsgType.TEXT)) {
            TextMsgBody msgBody = (TextMsgBody) item.getBody();
            helper.setText(R.id.chat_item_content_text, msgBody.getMessage());
        }
    }



    private void setOnClick(BaseViewHolder helper, Message item) {
        MsgBody msgContent = item.getBody();
    }

    public void addData(String hello) {
    }
}
