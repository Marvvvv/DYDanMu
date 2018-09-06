import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yukari.dao.AnchorMapper;
import com.yukari.entity.Anchor;
import com.yukari.entity.Gift;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:/config/spring/applicationContext.xml"})
public class getInfoByAPI {

    String roomListUrl = "http://open.douyucdn.cn/api/RoomApi/live/";

    String roomInfoUrl = "http://open.douyucdn.cn/api/RoomApi/room/";


    @Autowired
    AnchorMapper anchorMapper;

    @Test
    public void run () {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {

        }

    }



    public void start () throws IOException, InterruptedException {
        int typeId = 450;
        List<Anchor> anchorList = new ArrayList<>();
        while (true) {
            int page = 0;
            if (typeId > 700) {
                if (!anchorList.isEmpty()) {
                    anchorMapper.insertBatch(anchorList);
                    anchorList.clear();
                }
                break;
            }
            while (true) {
                Connection conn = Jsoup.connect(roomListUrl + typeId).timeout(60 * 1000).ignoreContentType(true).userAgent("Mozilla");
                conn.data("offset",String.valueOf(page* 100));
                conn.data("limit","100");
                System.out.println("====  type: " + typeId + "  page: " + page);
                Document doc = null;
                try {
                    doc = conn.get();
                } catch (HttpStatusException e) {
                    if (!anchorList.isEmpty()) {
                        anchorMapper.insertBatch(anchorList);
                        anchorList.clear();
                    }
                    break;
                }
                JSONObject roomListResult = JSON.parseObject(doc.text());
                if (!"0".equals(roomListResult.get("error").toString())) {
                    if (!anchorList.isEmpty()) {
                        anchorMapper.insertBatch(anchorList);
                        anchorList.clear();
                    }
                    break;
                }

                JSONArray roomList = roomListResult.getJSONArray("data");
                System.out.println("====  roomListSize: " + roomList.size());
                for (int i = 0; i < roomList.size(); i++) {
                    JSONObject roomInfo = roomList.getJSONObject(i);
                    int roomId = Integer.parseInt(roomInfo.get("room_id").toString());  // 房间号
                    String anchorName = roomInfo.get("nickname").toString();    // 主播昵称
                    int uid = Integer.parseInt(roomInfo.get("owner_uid").toString());   // 主播斗鱼uid
                    Anchor anchor = new Anchor(anchorName,roomId,uid);
                    anchorList.add(anchor);
                    if (!anchorList.isEmpty() && anchorList.size() >= 200) {
                        anchorMapper.insertBatch(anchorList);
                        anchorList.clear();
                    }
                }
                page++;
                Thread.sleep(1000);
            }
            typeId ++;
            Thread.sleep(1000);
        }

    }


    @Test
    public void getGiftInfo () {

        List<Integer> roomIdList = anchorMapper.quertAllRoomId();   // 房间号列表

        Map<Integer,Gift> giftInfo = new HashMap<>();    // 存储礼物信息






    }


}
