package com.heiya123.music.service.impl;

import com.heiya123.music.common.RequestHeaders;
import com.heiya123.music.entity.Music;
import com.heiya123.music.entity.QQ.QQLyr;
import com.heiya123.music.entity.QQ.QQMusicBase;
import com.heiya123.music.entity.QQ.QQMusicBase.DataBean.SongBean.ListBean;
import com.heiya123.music.entity.QQ.QQMusicBase.DataBean.SongBean.ListBean.SingerBean;
import com.heiya123.music.entity.QQ.QQMusicVkey;
import com.heiya123.music.entity.vo.SearchRequestVo;
import com.heiya123.music.musicEnum.MusicSourceEnum;
import com.heiya123.music.service.QQMusicService;
import com.heiya123.music.util.OkHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QQMusicServiceImpl implements QQMusicService {
    private static Logger logger = LoggerFactory.getLogger(QQMusicServiceImpl.class);

    /**
     * 分页获取歌曲
     *
     * @param req
     * @return
     */
    @Override
    public List<Music> findMusicByPage(SearchRequestVo req) {
        ArrayList<Music> list = new ArrayList<>();
        String url = "http://soso.music.qq.com/fcgi-bin/search_cp?aggr=0&catZhida=0&lossless=1&sem=1&w="
                + req.getName() + "&n=" + req.getPageSize() + "&t=0&p=" + req.getPageIndex()
                + "&remoteplace=sizer.yqqlist.song&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
        QQMusicBase qqMusicBase = OkHttpUtils.getRequest(url, RequestHeaders.tencentHeaders, null, QQMusicBase.class);
        List<ListBean> songs = qqMusicBase.getData().getSong().getList();
        for (ListBean song : songs) {
            Music music = new Music();
            /** 设置歌名 */
            music.setName(song.getSongname());
            /** 获取歌手 */
            List<SingerBean> singers = song.getSinger();
            String artist = "";
            for (int i = 0; i < singers.size(); i++) {
                artist += singers.get(i).getName();
                if (i != singers.size() - 1) {
                    artist += "、";
                }
            }
            music.setArtist(artist);
            /** 设置专辑 */
            music.setAlbum(song.getAlbumname());
            /** 设置封面图片ID */
            music.setPic_id(song.getAlbummid());
            /** 设置音乐链接ID */
            String quality = song.getSizeflac() > 0 ? "flac" : song.getSizeape() > 0 ? "ape" : song.getSize320() > 0 ? "320" : "128";
            music.setUrl_id(song.getMedia_mid() + ":" + quality);
            music.setId(song.getMedia_mid() + ":" + quality);
            /** 设置歌词ID */
            music.setLyric_id(song.getMedia_mid());
            /** 设置封面 */
            String albummid = song.getAlbummid();
            music.setPic("http://i.gtimg.cn/music/photo/mid_album_500/" + albummid.substring(albummid.length() - 2, albummid.length() - 1) + "/" + albummid.substring(albummid.length() - 1) + "/" + albummid + ".jpg");
            music.setSource(MusicSourceEnum.QQMusic.getSource());
            list.add(music);
        }
        return list;
    }


    /**
     * 获取歌曲URL
     *
     * @param media_mid
     * @return
     */
    @Override
    public String loadMusicUrl(String media_mid) {
        String[] split = media_mid.split(":");
        String prefix = "";
        String suffix = "";
        if ("flac".equals(split[1])) {
            prefix = "F0";
            suffix = "flac";
        } else if ("ape".equals(split[0])) {
            prefix = "A0";
            suffix = "ape";
        }else if ("320".equals(split[0])) {
            prefix = "M8";
            suffix = "mp3";
        } else if("128".equals(split[0])) {
            prefix = "M5";
            suffix = "mp3";
        } else {
            return null;
        }
        String uin = "1008611";
        String guid = "1234567890";
        String url = "http://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?g_tk=0&loginUin="+uin+"&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&cid=205361747&uin="+uin+"&songmid=003a1tne1nSz1Y&filename=C400003a1tne1nSz1Y.m4a&guid="+guid;
        QQMusicVkey qqMusicVkey = OkHttpUtils.getRequest(url, RequestHeaders.tencentHeaders, null, QQMusicVkey.class);
        return "http://streamoc.music.tc.qq.com/"+prefix+"00"+split[0]+"."+suffix+"?vkey="+qqMusicVkey.getData().getItems().get(0).getVkey()+"&guid=1234567890&uin=1008611&fromtag=8";
    }

    /**
     * 获取封面图片
     *
     * @param albummid
     * @return
     */
    @Override
    public String loadPic(String albummid) {
        return "http://i.gtimg.cn/music/photo/mid_album_500/" + albummid.substring(albummid.length() - 2, albummid.length() - 1) + "/" + albummid.substring(albummid.length() - 1) + "/" + albummid + ".jpg";
    }

    /**
     * 获取歌词
     *
     * @param media_mid
     * @return
     */
    @Override
    public String loadLyric(String media_mid) {
        String lyr = "";
        String url = "http://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric.fcg?format=json&nobase64=1&songtype=0&callback=c&songmid=" + media_mid;
        QQLyr qqLyr = OkHttpUtils.getRequest(url, RequestHeaders.tencentHeaders, null, QQLyr.class);
        if (qqLyr != null && qqLyr.getLyric() != null) {
            lyr =  qqLyr.getLyric().replace("&#58;", ":").replace("&#46;", ":").replace("&#10;", "\n").replace("&#32;",
                    " ").replace("&#45;", "").replace("&#13", "");
        }
        return lyr;
    }
}
