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
package org.b3log.xiaov.processor;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HttpMethod;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.xiaov.service.QQService;
import org.b3log.xiaov.util.XiaoVs;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

/**
 * QQ processor.
 * <ul>
 * <li>Handles QQ message (/qq), POST</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.2.6, Jan 2, 2019
 * @since 1.0.0
 */
@RequestProcessor
public class QQProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(QQProcessor.class);

    /**
     * QQ service.
     */
    @Inject
    private QQService qqService;

    /**
     * Handles QQ message.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/qq", method = HttpMethod.POST)
    public void qq(final RequestContext context) {
        final String key = XiaoVs.getString("qq.bot.key");
        if (!key.equals(context.param("key"))) {
            context.sendError(HttpServletResponse.SC_FORBIDDEN);

            return;
        }

        String msg = context.param("msg");
        if (StringUtils.isBlank(msg)) {
            LOGGER.warn("Empty msg body");
            context.sendError(HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        LOGGER.info("Push QQ groups [msg=" + msg + "]");
        qqService.sendToPushQQGroups(msg);

        final JSONObject ret = new JSONObject();
        context.renderJSON(ret);
        ret.put(Keys.STATUS_CODE, true);
    }
}
