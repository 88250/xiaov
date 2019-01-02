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

/**
 * <a href="http://www.itpk.cn">ITPK</a> bot query service.
 *
 * @author <a href="http://relyn.cn">Relyn</a>
 * @version 1.0.0.1, Oct 25, 2018
 * @since 2.0.1
 */
@Service
public class ItpkQueryService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ItpkQueryService.class);

    /**
     * ITPK Robot URL.
     */
    private static final String ITPK_API = XiaoVs.getString("itpk.api");

    /**
     * ITPK Robot API.
     */
    private static final String ITPK_KEY = XiaoVs.getString("itpk.key");

    /**
     * ITPK Robot Key.
     */
    private static final String ITPK_SECRET = XiaoVs.getString("itpk.secret");

    /**
     * Chat with ITPK Robot.
     *
     * @param msg the specified message
     * @return robot returned message, return {@code null} if not found
     */
    public String chat(String msg) {
        try {
            final HttpResponse response = HttpRequest.post(ITPK_API).
                    form("api_key", ITPK_KEY).
                    form("limit", 8).
                    form("api_secret", ITPK_SECRET).
                    form("question", msg).connectionTimeout(5000).timeout(5000).send();
            response.charset("UTF-8");
            return response.bodyText().substring(1);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Chat with ITPK Robot failed", e);
        }

        return null;
    }
}
