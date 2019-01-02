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

import org.apache.commons.io.IOUtils;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

/**
 * Shows QR code servlet.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.1, Apr 2, 2018
 * @since 2.1.0
 */
public class QRCodeShowServlet extends HttpServlet {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(QRCodeShowServlet.class);

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.addHeader("Cache-Control", "no-store");

        try (final PrintWriter writer = resp.getWriter()) {
            final String filePath = new File("qrcode.png").getCanonicalPath();
            final byte[] data = IOUtils.toByteArray(new FileInputStream(filePath));
            final StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<html><body><img src=\"data:image/png;base64,").
                    append(Base64.getEncoder().encodeToString(data)).append("\"/></body></html>");
            writer.write(htmlBuilder.toString());
            writer.flush();
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "在线显示二维码图片异常", e);
        }
    }
}
