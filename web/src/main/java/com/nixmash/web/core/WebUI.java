package com.nixmash.web.core;

import com.github.mustachejava.functions.TranslateBundleFunction;
import com.google.inject.Inject;
import com.nixmash.jangles.core.JanglesCache;
import com.nixmash.jangles.core.JanglesGlobals;
import com.nixmash.web.dto.PageInfo;
import com.nixmash.web.enums.ActiveMenu;
import com.nixmash.web.exceptions.RestProcessingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.LocaleUtils;

import java.io.*;
import java.util.*;

/**
 * Created by daveburke on 7/1/17.
 */
@SuppressWarnings({"SameParameterValue", "ConstantConditions"})
public class WebUI implements Serializable {

    private static final long serialVersionUID = 9155986213118958079L;
    private static final String NULL_FIELD = "*";
    private static final String USERS_MENU = "users";
    private static final String POSTS_MENU = "posts";
    private static final String BUNDLE = "messages";
    private static final String ERROR_PAGE = "error";


    // region Constructor

    private final JanglesGlobals janglesGlobals;
    private final JanglesCache janglesCache;
    private final WebContext webContext;

    @Inject
    public WebUI(JanglesGlobals janglesGlobals, JanglesCache janglesCache, WebContext webContext) {
        this.janglesGlobals = janglesGlobals;
        this.janglesCache = janglesCache;
        this.webContext = webContext;
    }

    // endregion

    // region Resource Bundle

    public TranslateBundleFunction getResourceBundle() {
        Locale locale = LocaleUtils.toLocale(webContext.config().currentLocale);
        return new TranslateBundleFunction(BUNDLE, locale);
    }

    // endregion

    // region PageInfo

    @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
    public PageInfo getPageInfo(String pagekey) {
        List<PageInfo> pageInfoList = (List<PageInfo>) janglesCache.get(pageInfoCacheKey());
        if (pageInfoList == null) {
            try {
                pageInfoList = loadPageInfoFromCvs();
                janglesCache.put(pageInfoCacheKey(), (Serializable) pageInfoList);
            } catch (IOException e) {
                return new PageInfo();
            }
        }
        Optional<PageInfo> pageInfo = pageInfoList.stream().filter(p -> p.getPage_key().equals(pagekey)).findFirst();
        return pageInfo.orElseGet(PageInfo::new);
        /*
        PageInfo found = pageInfo.orElseGet(PageInfo::new);
        found.setTrans(getResourceBundle());
        return found;
        */
    }

    private List<PageInfo> loadPageInfoFromCvs() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File("pageinfo.cvs");
        FileUtils.copyInputStreamToFile(classLoader.getResourceAsStream("pageinfo.cvs"), file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        Locale locale = LocaleUtils.toLocale(webContext.config().currentLocale);
        String pageTitlePrefix = webContext.config().pageTitlePrefix;
        String line;
        List<PageInfo> pageInfoList = new ArrayList<>();
        int iteration = 0;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            if (iteration == 0) {
                iteration++;
                continue;
            }
            String[] fields = line.split(",");

            int page_id = Integer.parseInt(fields[0]);
            String page_key = fields[1];
            String page_title = String.format("%s : %s", pageTitlePrefix, fields[2]);
            String heading = fields[3].equals(NULL_FIELD) ? null : fields[3];
            String subheading = fields[4].equals(NULL_FIELD) ? null : fields[4];
            String menu = fields[5].equals(NULL_FIELD) ? null : fields[5];

            PageInfo pageInfo = PageInfo.getBuilder(page_id, page_key, page_title)
                    .heading(heading)
                    .subheading(subheading)
                    .inDevelopmentMode(isInDevelopmentMode())
                    .activeMenu(getActiveMenu(menu))
                    .resourceBundle(new TranslateBundleFunction(BUNDLE, locale))
                    .build();

            pageInfoList.add(pageInfo);
        }
        br.close();
        return pageInfoList;
    }

    // endregion

    // region Messages

    public String getMessage(String key, Object... params) {
        return webContext.messages().get(key, params);
    }
    // endregion

    // region CacheKeys

    public String pageInfoCacheKey() {
        return String.format("PageInfoCacheKey-%s", janglesGlobals.cloudApplicationId);
    }

    public String resourceBundleCacheKey() {
        return String.format("ResourceBundleCacheKey-%s", janglesGlobals.cloudApplicationId);
    }

    // endregion

    // region Private Support and Utility Methods

    public Boolean isInDevelopmentMode() {
        return webContext
                .globals()
                .inProductionMode
                .equals(false);
    }

    public ActiveMenu getActiveMenu(String menu) {
        ActiveMenu activeMenu = new ActiveMenu();
        if (menu == null)
            return activeMenu;
        else {
            switch (menu) {
                case USERS_MENU:
                    activeMenu.setUsersMenu(true);
                    break;
                case POSTS_MENU:
                    activeMenu.setPostsMenu(true);
                    break;
                default:
                    break;
            }
        }
        return activeMenu;
    }
    // endregion

    // region Error Page

    public String errorPage(RestProcessingException e) {
        Map<String, Object> model = new HashMap<>();
        model.put("pageinfo", getPageInfo(ERROR_PAGE));
        model.put("errorMessage", e.getMsg());
        return webContext.templatePathResolver().populateTemplate("error.html", model);
    }

    // endregion
}
