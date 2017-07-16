package cxworks.api;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsFuture;
import com.alibaba.idst.nls.event.NlsEvent;
import com.alibaba.idst.nls.event.NlsListener;
import com.alibaba.idst.nls.protocol.NlsRequest;
import com.alibaba.idst.nls.protocol.NlsResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

/**
 * Hello world!
 *
 */
public class TTS implements NlsListener
{
    public static void main( String[] args )
    {
        TTS tts =new TTS();
        tts.startTTS("对不起，歌曲下载出错啦");
        tts.shutDown();


    }

    private NlsClient client = new NlsClient();
    private volatile String result;

    public String getResult() {
        if (result==null){
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (result==null) result="出错啦";
        return result;
    }

    public TTS() {

        // 初始化NlsClient
        client.init();
    }

    public void shutDown() {

        // 关闭客户端并释放资源
        client.close();

    }


    public void startTTS(String tts_text) {
        result=null;
        File file = new File("tts.pcm");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        NlsRequest req = new NlsRequest();
        String appkey = "nls-service";
        req.setAppKey(appkey); // 设置语音文件格式
        req.setTtsRequest(tts_text); //传入测试文本，返回语音结果
        req.setTtsEncodeType("pcm");//返回语音数据格式，支持pcm,wav.alaw
        req.setTtsVolume(30);       //音量大小默认50，阈值0-100
        req.setTtsSpeechRate(0);    //语速，阈值-500~500
        req.setTtsBackgroundMusic(1,0);//背景音乐编号,偏移量
        req.authorize("LTAI7n6Em6JjrZNL", "CgARMz72zHCm7EXuD7BXMnyBLqU35k"); // 请替换为用户申请到的Access Key ID和Access Key Secret
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            NlsFuture future = client.createNlsFuture(req, this); // 实例化请求,传入请求和监听器
            int total_len = 0;
            byte[] data ;
            while((data = future.read()) != null) {
                fileOutputStream.write(data, 0, data.length);
                total_len += data.length;
            }
            fileOutputStream.close();
            future.await(10000); // 设置服务端结果返回的超时时间

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMessageReceived(NlsEvent e) {
        //识别结果的回调
        NlsResponse response = e.getResponse();
        String result = "";
        int statusCode = response.getStatus_code();
        if (response.getAsr_ret() != null) {
            result += response.getAsr_ret();
        }
        if (result.length()==0) return;
        if (result != null) {
            JsonObject jsonObject=new JsonParser().parse(result).getAsJsonObject();
            this.result=jsonObject.get("result").getAsString();
        } else {
            System.out.println(response.jsonResults.toString());
        }

    }


    public void onOperationFailed(NlsEvent e) {
        //识别失败的回调
        System.out.print("on operation failed: ");
        System.err.println(e.getErrorMessage());
        result=null;
    }


    public void onChannelClosed(NlsEvent e) {
        //socket 连接关闭的回调
    }

}
