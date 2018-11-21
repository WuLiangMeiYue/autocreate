package com.auto.create.utils;

import java.util.HashMap;
import java.util.Map;

public class TransUtil {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private static final String APPID = "20181121000237251";

    private static final String SECURITYKEY = "pX3WxF_ZMjcjw08YJ_ZX";

    public String getTransResult(String query, String to) {
        Map<String, String> params = buildParams(query, to);
        return HttpGetUtil.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String to) {
        Map<String, String> params = new HashMap<String, String>();
        String salt = String.valueOf(System.currentTimeMillis());

        params.put("q", query);
        params.put("from", "auto");
        params.put("to", to);
        params.put("appid", APPID);
        params.put("salt", salt);
        params.put("sign", MD5Util.md5(APPID + query + salt + SECURITYKEY));

        return params;
    }

}
