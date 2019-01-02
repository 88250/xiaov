/*
 * Copyright (c) 2012-2019, b3log.org & hacpai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.xiaov.service;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.service.annotation.Service;
import org.b3log.xiaov.util.XiaoVs;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Turing query service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.1, Oct 25, 2018
 * @since 1.0.0
 */
@Service
public class TuringQueryService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TuringQueryService.class);

    /**
     * Turing Robot API.
     */
    private static final String TURING_API = XiaoVs.getString("turing.api");

    /**
     * Turing Robot Key.
     */
    private static final String TURING_KEY = XiaoVs.getString("turing.key");

    /**
     * Chat with Turing Robot.
     *
     * @param userName the specified user name
     * @param msg      the specified message
     * @return robot returned message, return {@code null} if not found
     */
    public String chat(final String userName, String msg) {
        try {
            final HttpResponse response = HttpRequest.post(TURING_API).
                    form("key", TURING_KEY).
                    form("info", msg).
                    form("userid", userName).connectionTimeout(5000).timeout(5000).send();
            response.charset("UTF-8");
            final JSONObject data = new JSONObject(response.bodyText());
            final int code = data.optInt("code");

            switch (code) {
                case 40001:
                case 40002:
                case 40007:
                    LOGGER.log(Level.ERROR, data.optString("text"));

                    return null;
                case 40004:
                    return "聊累了，明天请早吧~";
                case 100000:
                    return data.optString("text");
                case 200000:
                    return data.optString("text") + " " + data.optString("url");
                case 302000:
                    String ret302000 = data.optString("text") + " ";
                    final JSONArray list302000 = data.optJSONArray("list");
                    final StringBuilder builder302000 = new StringBuilder();
                    for (int i = 0; i < list302000.length(); i++) {
                        final JSONObject news = list302000.optJSONObject(i);
                        builder302000.append(news.optString("article")).append(news.optString("detailurl"))
                                .append("\n\n");
                    }

                    return ret302000 + " " + builder302000.toString();
                case 308000:
                    String ret308000 = data.optString("text") + " ";
                    final JSONArray list308000 = data.optJSONArray("list");
                    final StringBuilder builder308000 = new StringBuilder();
                    for (int i = 0; i < list308000.length(); i++) {
                        final JSONObject news = list308000.optJSONObject(i);
                        builder308000.append(news.optString("name")).append(news.optString("detailurl"))
                                .append("\n\n");
                    }

                    return ret308000 + " " + builder308000.toString();
                default:
                    LOGGER.log(Level.WARN, "Turing Robot default return [" + data.toString(4) + "]");
            }
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Chat with Turing Robot failed", e);
        }

        return null;
    }
}
