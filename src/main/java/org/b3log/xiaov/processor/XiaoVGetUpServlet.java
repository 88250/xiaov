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

import org.b3log.latke.ioc.BeanManager;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.xiaov.service.QQService;

import javax.servlet.http.HttpServlet;

/**
 * XiaoV, get up!
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.2, Oct 25, 2018
 * @since 2.1.0
 */
public class XiaoVGetUpServlet extends HttpServlet {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(XiaoVGetUpServlet.class);

    /**
     * Bean manager.
     */
    private BeanManager beanManager;

    @Override
    public void init() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (final Exception e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }

            beanManager = BeanManager.getInstance();
            final QQService qqService = beanManager.getReference(QQService.class);
            qqService.initQQClient();
        }).start();
    }
}
