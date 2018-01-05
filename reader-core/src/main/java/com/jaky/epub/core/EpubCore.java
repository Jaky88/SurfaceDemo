/*
The MIT License (MIT)

Copyright (c) 2013, V. Giacometti, M. Giuriato, B. Petrantuono

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package com.jaky.epub.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Environment;
import android.print.PrintDocumentAdapter;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.jaky.sdk.reader_core.R;
import com.jaky.utils.FileUtils;
import com.jaky.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubCore {
    private Book book;
    private String currentPage;
    private int currentLanguage;
    private List<String> availableLanguages;
    private int pageCount;
    private int currentSpineElementIndex;
    private String[] spineElementPaths;
    private List<Boolean> translations;
    private String decompressedFolder;
    private String pathOPF;
    private static Context context;

    private static String location = Environment.getExternalStorageDirectory() + "/epubtemp";
    private String fileName;
    private FileInputStream in;
    private String actualCSS = "";
    private String[][] audio;
    private String url;

    public EpubCore(Context context, String fileName, String folder) throws Exception {
        this.context = context;
        this.fileName = fileName;
        this.decompressedFolder = folder;
        this.currentSpineElementIndex = 0;
        this.currentLanguage = 0;
        translations = new ArrayList<Boolean>();
        availableLanguages = new ArrayList<String>();
        init();
    }

    private void init() throws Exception {
        initBook();
        List<SpineReference> spineList = book.getSpine().getSpineReferences();

        List<String> spineElements = new ArrayList<String>();
        pages(spineList, spineElements);
        pageCount = spineElements.size();

        //解压epub文件
        FileUtils.unzip(fileName, location + decompressedFolder);
        pathOPF = getPathOPF(location + decompressedFolder);

        spineElementPaths = new String[spineElements.size()];
        for (int i = 0; i < spineElements.size(); ++i) {
            spineElementPaths[i] = "file://" + location + decompressedFolder + "/" + pathOPF + "/" + spineElements.get(i);
        }

        if (spineElements.size() > 0) {
            url = gotoPage(0);
        }
        createTocFile();
    }

    private void initBook() throws IOException {
        EpubReader reader = new EpubReader();
        in = new FileInputStream(fileName);
        book = reader.readEpub(in);
    }

    public EpubCore(Context context, String fileName, String folder, int spineIndex, int language) throws Exception {
        List<String> spineElements;
        List<SpineReference> spineList;

        this.context = context;
        this.fileName = fileName;
        this.decompressedFolder = folder;
        this.currentLanguage = language;

        this.in = new FileInputStream(fileName);
        this.book = (new EpubReader()).readEpub(in);


        Spine spine = book.getSpine();
        spineList = spine.getSpineReferences();
        this.currentSpineElementIndex = spineIndex;

        spineElements = new ArrayList<String>();
        pages(spineList, spineElements);
        this.pageCount = spineElements.size();
        this.spineElementPaths = new String[spineElements.size()];

        pathOPF = getPathOPF(location + folder);

        for (int i = 0; i < spineElements.size(); ++i) {
            this.spineElementPaths[i] = "file://" + location + folder + "/" + pathOPF + "/" + spineElements.get(i);
        }
        gotoPage(spineIndex);
    }

    public void setLanguage(int lang) throws Exception {
        if ((lang >= 0) && (lang <= this.availableLanguages.size())) {
            this.currentLanguage = lang;
        }
        gotoPage(this.currentSpineElementIndex);
    }

    // set language from an identifier string
    public void setLanguage(String lang) throws Exception {
        int i = 0;
        while ((i < this.availableLanguages.size())
                && (!(this.availableLanguages.get(i).equals(lang)))) {
            i++;
        }
        setLanguage(i);
    }

    public String[] getLanguages() {
        String[] lang = new String[availableLanguages.size()];
        for (int i = 0; i < availableLanguages.size(); i++) {
            lang[i] = availableLanguages.get(i);
        }
        return lang;
    }

    private void pages(List<SpineReference> spineList, List<String> pages) {
        int langIndex;
        String lang;
        String actualPage;

        for (int i = 0; i < spineList.size(); ++i) {
            //获取每页的链接
            actualPage = (spineList.get(i)).getResource().getHref();
            lang = getPageLanguage(actualPage);//获取该页的语言
            if (!StringUtils.isNullOrEmpty(lang)) {//需要翻译
                langIndex = languageIndexFromID(lang);
                if (langIndex == availableLanguages.size()) {
                    availableLanguages.add(lang);
                }
                if (langIndex == 0) {
                    translations.add(true);
                    pages.add(actualPage);
                }
            } else {//不需要翻译
                translations.add(false);
                pages.add(actualPage);
            }
        }
    }

    private int languageIndexFromID(String id) {
        int i = 0;
        while ((i < availableLanguages.size()) && (!(availableLanguages.get(i).equals(id)))) {
            i++;
        }
        return i;
    }

    //解析xml文件，获取opf文件路径
    private static String getPathOPF(String unzipDir) throws IOException {
        String pathOPF = "";
        String containerFile = unzipDir + "/META-INF/container.xml";
        BufferedReader br = new BufferedReader(new FileReader(containerFile));
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.indexOf("full-path") > -1) {
                int start = line.indexOf("full-path");
                int start2 = line.indexOf("\"", start);
                int stop2 = line.indexOf("\"", start2 + 1);
                if (start2 > -1 && stop2 > start2) {
                    pathOPF = line.substring(start2 + 1, stop2).trim();
                    break;
                }
            }
        }
        br.close();

        if (!pathOPF.contains("/")) {
            return null;
        }

        int last = pathOPF.lastIndexOf('/');
        if (last > -1) {
            pathOPF = pathOPF.substring(0, last);
        }

        return pathOPF;
    }

    public void destroy() throws IOException {
        FileUtils.closeQuietly(in);
        book = null;
        FileUtils.deleteFile(location + decompressedFolder);
    }

    public void changeDirName(String newName) {
        File dir = new File(location + decompressedFolder);
        File newDir = new File(location + newName);
        dir.renameTo(newDir);

        for (int i = 0; i < spineElementPaths.length; ++i) {
            spineElementPaths[i] = spineElementPaths[i].replace("file://" + location + decompressedFolder, "file://" + location + newName);
        }
        decompressedFolder = newName;
        try {
            gotoPage(currentSpineElementIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String gotoPage(int page) throws Exception {
        return gotoPage(page, this.currentLanguage);
    }

    public boolean gotoPage(String page) {
        int index = getPageIndex(page);
        boolean res = false;
        if (index >= 0) {
            String newLang = getPageLanguage(page);
            try {
                gotoPage(index);
                if (newLang != "") {
                    setLanguage(newLang);
                }
                res = true;
            } catch (Exception e) {
                res = false;
                Log.e(getS(R.string.error_goToPage), e.getMessage());
            }
        }
        return res;
    }

    public String gotoPage(int page, int lang) throws Exception {
        String spineElement;
        String extension;
        if (page < 0) {
            page = 0;
        }
        if (page >= this.pageCount) {
            page = this.pageCount - 1;
        }
        this.currentSpineElementIndex = page;

        spineElement = this.spineElementPaths[currentSpineElementIndex];

        if (this.translations.get(page)) {
            extension = spineElement.substring(spineElement.lastIndexOf("."));
            spineElement = spineElement.substring(0, spineElement.lastIndexOf(this.availableLanguages.get(0)));
            spineElement = spineElement + this.availableLanguages.get(lang) + extension;
        }

        currentPage = spineElement;
        audioExtractor(currentPage);
        return spineElement;
    }

    public void drawPage(Bitmap bm, int page, int pageW, int pageH, int patchX, int patchY, int patchW, int patchH) {
        Log.d("", "=========drawPage======="+url);
        try {
            Canvas canvas = new Canvas(bm);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(40f);
            paint.setStyle(Paint.Style.FILL);
            URL fileUrl = new URL(url);
            canvas.drawText(FileUtils.readContentFromFile(fileUrl.getFile()), patchX,patchY,paint);


            WebView web = new WebView(context);
            web.loadUrl(url);
//            bm = convertViewToBitmap(web);
//            web.draw(canvas);
//            Log.d("", "==========web.getTitle==========" + web.);

//            Bitmap bitmap = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());

//            canvas.drawText(content, patchX, patchY, paint);
//            saveImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache(true);
    }

    public void saveImage() {
        WebView webView = new WebView(context);
        webView.loadUrl(url);
        Picture picture = webView.capturePicture();
        Bitmap b = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
        FileUtils.saveBitmapToFile(b, new File("/sdcard/web.jpg"), Bitmap.CompressFormat.JPEG,90);
    }

    public String goToNextChapter() throws Exception {
        return gotoPage(this.currentSpineElementIndex + 1);
    }

    public String goToPreviousChapter() throws Exception {
        return gotoPage(this.currentSpineElementIndex - 1);
    }

    //元数据转成html文件
    public String metadataToHtml() {
        List<String> tmp;
        Metadata metadata = book.getMetadata();
        String html = getS(R.string.htmlBodyTableOpen);

        // Titles
        tmp = metadata.getTitles();
        if (tmp.size() > 0) {
            html += getS(R.string.titlesMeta);
            html += "<td>" + tmp.get(0) + "</td></tr>";
            for (int i = 1; i < tmp.size(); i++) {
                html += "<tr><td></td><td>" + tmp.get(i) + "</td></tr>";
            }
        }

        List<Author> authors = metadata.getAuthors();
        if (authors.size() > 0) {
            html += getS(R.string.authorsMeta);
            html += "<td>" + authors.get(0).getFirstname() + " " + authors.get(0).getLastname() + "</td></tr>";
            for (int i = 1; i < authors.size(); i++) {
                html += "<tr><td></td><td>" + authors.get(i).getFirstname() + " " + authors.get(i).getLastname() + "</td></tr>";
            }
        }

        // Contributors
        authors = metadata.getContributors();
        if (authors.size() > 0) {
            html += getS(R.string.contributorsMeta);
            html += "<td>" + authors.get(0).getFirstname() + " " + authors.get(0).getLastname() + "</td></tr>";
            for (int i = 1; i < authors.size(); i++) {
                html += "<tr><td></td><td>" + authors.get(i).getFirstname() + " " + authors.get(i).getLastname() + "</td></tr>";
            }
        }

        // Language
        html += getS(R.string.languageMeta) + metadata.getLanguage() + "</td></tr>";

        // Publishers
        tmp = metadata.getPublishers();
        if (tmp.size() > 0) {
            html += getS(R.string.publishersMeta);
            html += "<td>" + tmp.get(0) + "</td></tr>";
            for (int i = 1; i < tmp.size(); i++) {
                html += "<tr><td></td><td>" + tmp.get(i) + "</td></tr>";
            }
        }

        // Types
        tmp = metadata.getTypes();
        if (tmp.size() > 0) {
            html += getS(R.string.typesMeta);
            html += "<td>" + tmp.get(0) + "</td></tr>";
            for (int i = 1; i < tmp.size(); i++) {
                html += "<tr><td></td><td>" + tmp.get(i) + "</td></tr>";
            }
        }

        // Descriptions
        tmp = metadata.getDescriptions();
        if (tmp.size() > 0) {
            html += getS(R.string.descriptionsMeta);
            html += "<td>" + tmp.get(0) + "</td></tr>";
            for (int i = 1; i < tmp.size(); i++)
                html += "<tr><td></td><td>" + tmp.get(i) + "</td></tr>";
        }

        // Rights
        tmp = metadata.getRights();
        if (tmp.size() > 0) {
            html += getS(R.string.rightsMeta);
            html += "<td>" + tmp.get(0) + "</td></tr>";
            for (int i = 1; i < tmp.size(); i++) {
                html += "<tr><td></td><td>" + tmp.get(i) + "</td></tr>";
            }
        }

        html += getS(R.string.tablebodyhtmlClose);
        return html;
    }

    public String r_createTocFile(TOCReference e) {
        String childrenPath = "file://" + location + decompressedFolder + "/" + pathOPF + "/" + e.getCompleteHref();
        String html = "<ul><li>" + "<a href=\"" + childrenPath + "\">" + e.getTitle() + "</a>" + "</li></ul>";
        List<TOCReference> children = e.getChildren();
        for (int j = 0; j < children.size(); j++) {
            html += r_createTocFile(children.get(j));
        }

        return html;
    }

    public void createTocFile() {
        List<TOCReference> tmp;
        TableOfContents toc = book.getTableOfContents();
        String html = "<html><body><ul>";

        tmp = toc.getTocReferences();

        if (tmp.size() > 0) {
            html += getS(R.string.tocReference);
            for (int i = 0; i < tmp.size(); i++) {
                String path = "file://" + location + decompressedFolder + "/" + pathOPF + "/" + tmp.get(i).getCompleteHref();
                html += "<li>" + "<a href=\"" + path + "\">" + tmp.get(i).getTitle() + "</a>" + "</li>";
                List<TOCReference> children = tmp.get(i).getChildren();

                for (int j = 0; j < children.size(); j++) {
                    html += r_createTocFile(children.get(j));
                }
            }
        }

        html += getS(R.string.tablebodyhtmlClose);

        String filePath = location + decompressedFolder + "/Toc.html";
        FileUtils.saveContentToFile(html, filePath);
    }

    public String getTocFilePath() {
        return "File://" + location + decompressedFolder + "/Toc.html";
    }

    public int getPageIndex(String page) {
        int result = -1;
        String lang;

        lang = getPageLanguage(page);
        if ((this.availableLanguages.size() > 0) && (lang != "")) {
            page = page.substring(0, page.lastIndexOf(lang)) + this.availableLanguages.get(0) + page.substring(page.lastIndexOf("."));
        }
        for (int i = 0; i < this.spineElementPaths.length && result == -1; i++) {
            if (page.equals(this.spineElementPaths[i])) {
                result = i;
            }
        }
        return result;
    }

    public String getPageLanguage(String page) {
        String[] tmp = page.split("\\.");
        if (tmp.length > 2) {
            String secondFromLastItem = tmp[tmp.length - 2];
            if (secondFromLastItem.matches("[a-z][a-z]")) {
                return secondFromLastItem;
            }
        }
        return "";
    }

    public void addCSS(String[] settings) {
        String css = "<style type=\"text/css\">\n";

        if (!settings[0].isEmpty()) {
            css = css + "body{color:" + settings[0] + ";}";
            css = css + "a:link{color:" + settings[0] + ";}";
        }

        if (!settings[1].isEmpty())
            css = css + "body {background-color:" + settings[1] + ";}";

        if (!settings[2].isEmpty())
            css = css + "p{font-family:" + settings[2] + ";}";

        if (!settings[3].isEmpty())
            css = css + "p{\n\tfont-size:" + settings[3] + "%\n}\n";

        if (!settings[4].isEmpty())
            css = css + "p{line-height:" + settings[4] + "em;}";

        if (!settings[5].isEmpty())
            css = css + "p{text-align:" + settings[5] + ";}";

        if (!settings[6].isEmpty())
            css = css + "body{margin-left:" + settings[6] + "%;}";

        if (!settings[7].isEmpty())
            css = css + "body{margin-right:" + settings[7] + "%;}";

        css = css + "</style>";

        for (int i = 0; i < spineElementPaths.length; i++) {
            String path = spineElementPaths[i].replace("file:///", "");
            String source = readPage(path);

            source = source.replace(actualCSS + "</head>", css + "</head>");

            writePage(path, source);
        }
        actualCSS = css;

    }

    private void adjustAudioLinks() {
        for (int i = 0; i < audio.length; i++) {
            for (int j = 0; j < audio[i].length; j++) {
                if (audio[i][j].startsWith("./")) {
                    audio[i][j] = currentPage.substring(0, currentPage.lastIndexOf("/")) + audio[i][j].substring(1);
                }

                if (audio[i][j].startsWith("../")) {
                    String temp = currentPage.substring(0, currentPage.lastIndexOf("/"));
                    audio[i][j] = temp.substring(0, temp.lastIndexOf("/")) + audio[i][j].substring(2);
                }
            }
        }
    }

    private ArrayList<String> getAudioSources(String audioTag) {
        ArrayList<String> srcs = new ArrayList<String>();
        Pattern p = Pattern.compile("src=\"[^\"]*\"");
        Matcher m = p.matcher(audioTag);
        while (m.find()) {
            srcs.add(m.group().replace("src=\"", "").replace("\"", ""));
        }
        return srcs;
    }

    private ArrayList<String> getAudioTags(String page) {
        ArrayList<String> res = new ArrayList<String>();
        String source = readPage(page);
        Pattern p = Pattern.compile("<audio(?s).*?</audio>|<audio(?s).*?/>");
        Matcher m = p.matcher(source);
        while (m.find()) {
            res.add(m.group(0));
        }
        return res;
    }

    private void audioExtractor(String page) {
        ArrayList<String> tags = getAudioTags(page.replace("file:///", ""));
        ArrayList<String> srcs;
        audio = new String[tags.size()][];

        for (int i = 0; i < tags.size(); i++) {
            srcs = getAudioSources(tags.get(i));
            audio[i] = new String[srcs.size()];
            for (int j = 0; j < srcs.size(); j++) {
                audio[i][j] = srcs.get(j);
            }

        }
        adjustAudioLinks();
    }

    public String[][] getAudio() {
        return audio;
    }

    private String readPage(String path) {
        return FileUtils.readContentFromFile(path);
    }

    private boolean writePage(String path, String xhtml) {
        return FileUtils.saveContentToFile(xhtml, path);
    }

    public int getCurrentSpineElementIndex() {
        return currentSpineElementIndex;
    }

    public String getSpineElementPath(int elementIndex) {
        return spineElementPaths[elementIndex];
    }

    public String getCurrentPageURL() {
        return currentPage;
    }

    public int getCurrentLanguage() {
        return currentLanguage;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDecompressedFolder() {
        return decompressedFolder;
    }

    public static String getS(int id) {
        return context.getResources().getString(id);
    }
}
