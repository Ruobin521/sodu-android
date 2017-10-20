package com.ruobin.sodu.Service;

import android.net.rtp.RtpStream;
import android.text.TextUtils;

import com.ruobin.sodu.Model.Book;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ruobin.sodu.Service.SoduSourceUrl.*;

/**
 * Created by ds on 2017/10/20.
 */

public class HtmlAnalysisService {

    public static enum AnalisysType {

        ContentHtml,
        CatalogPageHtml,
        CatalogPageUrl,
    }


    public static Object analysisHtmlData(String url, String html, AnalisysType type, String bookName) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();

            List<String> values = getPropertyInfoArray();

            switch (host) {
                case "www.snwx8.com":
                    if (type == AnalisysType.ContentHtml) {
                        String value = getContentFromHtmlCommon(html, "<div id=\"BookText\">.*?</div>");
                        return value;
                    }
                    if (type == AnalisysType.CatalogPageUrl) {

                        return null;
                    }
                    if (type == AnalisysType.CatalogPageHtml) {
//                    var baseUrl = "http://" + host;
//                    var value = GetCatalogPageDataCommon(url, baseUrl, html,
//                            catalogsRegex: "<div id=\"list\">.*?</div>",
//                        catalogRegex: "<dd><a href=\"(.*?)\".*?>(.*?)</a></dd>",
//                        introRegex: "<div class=\"intro\">(.*?)</div>",
//                        coverRegex: "<div id=\"fmimg\">.*?<img.*?src=\"(.*?)\".*?>",
//                        authorRegex: "<i>作者：(.*?)</i>");
                        return null;
                    }
                    break;
            }
        } catch (Exception ex) {

            return null;
        }
        return null;
    }

    private static String getContentFromHtmlCommon(String html, String regexStr) {
        try {
            String result = null;
            html = html.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
            Matcher matcher = Pattern.compile(regexStr).matcher(html);

            if (matcher == null) {
                return null;
            }
            while (matcher.find()) {
                result = matcher.group();
            }
            if (!TextUtils.isEmpty(result)) {
                result = result.replaceAll("阅读本书最新章节请到.*?敬请记住我们最新网址.*?m", "");
            }

            result = replaceSymbol(result);
            return result;
        } catch (Exception ex) {
            return null;
        }
    }


    public static String replaceSymbol(String html) {
        html = html.replaceAll("<br.*?/>", "\n");
        html = html.replaceAll("<script.*?</script>", "");
        html = html.replaceAll("&nbsp;", " ");
        html = html.replaceAll("<p.*?>", "\n");
        html = html.replaceAll("<.*?>", "");
        html = html.replaceAll("&lt;/script&gt;", "");
        html = html.replaceAll("&lt;/div&gt;", "");
        html = html.replaceAll("  ", "　");
        html = html.replaceAll("\n\n", "\n");
        html = html.replaceAll("　　　　　　", "　　");
        html = html.trim();
        return html;
    }
}
